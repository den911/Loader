package ru.ddyakin.tarantool.sharding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.tarantool.*;
import ru.ddyakin.jdo.KeyCommon;
import ru.ddyakin.jdo.Structure;
import ru.ddyakin.logic.NodeInspector;
import ru.ddyakin.utils.TarantoolUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static ru.ddyakin.utils.TarantoolUtils.convertKey;
import static ru.ddyakin.utils.TarantoolUtils.convertRow;

@SuppressWarnings("unchecked")
public class ShardedTarantoolTemplateManager {
    private static final Logger log = LoggerFactory.getLogger(ShardedTarantoolTemplateManager.class);

    private NodeInspector inspector;
    public TarantoolClientImpl[] shardedTemplates;
    public TarantoolClientImpl masterTemplate;
    private TarantoolClientConfig commonConfig;

    public ShardedTarantoolTemplateManager(TarantoolClientImpl[] shardedTemplates, TarantoolClientImpl masterTemplate) {
        this.shardedTemplates = shardedTemplates;
        this.masterTemplate = masterTemplate;
    }

    public ShardedTarantoolTemplateManager(Resource clusterConfig) {
        Properties properties = new Properties();
        try {
            properties.load(clusterConfig.getInputStream());
        } catch (IOException e) {
            log.error("Failed to load properties.", e);
        }
        int shards = Integer.valueOf(properties.getProperty("tarantool.shards.number"));
        shardedTemplates = new TarantoolClientImpl[shards - 1];
        TarantoolClientConfig config = initTarantoolClusterConfig(properties);
        SocketChannelProvider[] providers = initProviders(properties, shards);
        masterTemplate = new TarantoolClientImpl(providers[0], config);
        for (int i=0; i<shards-1; i++) {
            shardedTemplates[i] = new TarantoolClientImpl(providers[i+1], config);
        }
    }

    private TarantoolClientConfig initTarantoolClusterConfig(Properties props) {
        TarantoolClientConfig config = new TarantoolClientConfig();
        config.username = props.getProperty("tarantool.user");
        config.password = props.getProperty("tarantool.password");
        commonConfig = config;
        return config;
    }

    private SocketChannelProvider[] initProviders(Properties props, int shards) {
        SocketChannelProvider[] providers = new SocketChannelProvider[shards];
        for (int i=0; i<shards; i++) {
            log.info("init tarantool shard number {}", i);
            final int k = i;
            providers[i] = (retryNumber, lastError) -> {
                if (lastError != null) log.error("err ", lastError);
                try {
                    return SocketChannel.open(new InetSocketAddress(props.getProperty("tarantool.node."+ k +".host"),
                            Integer.valueOf(props.getProperty("tarantool.node." + k +".port"))));
                } catch (IOException e) {
                    this.inspector = new NodeInspector(getShards() - 1);
                    throw new IllegalStateException(e);
                }
            };
        }
        return providers;
    }

    public int getShards() {
        return this.shardedTemplates.length;
    }

    public TarantoolClient getShardTemplate(int index) {
        return shardedTemplates[index];
    }

    public TarantoolClientImpl getShardTemplate(String shardStr) {
        return shardedTemplates[chooseShard(shardStr)];
    }

    public int chooseShard(String shardStr) {
        return Math.abs(shardStr.hashCode()) % getShards();
    }

    public List<Object> getSpaces() {
        List<?> result = masterTemplate.syncOps().eval("return box.space.spaces:select{}");
        if (result == null) {
            log.info("can't find spaces");
            return null;
        }
        List<Object> spaces = (List<Object>) result;
        log.info("get spaces {}", spaces);
        return spaces;
    }

    @Autowired
    public void setInspector(NodeInspector inspector) {
        this.inspector = inspector;
        this.inspector.setTimestamp(System.currentTimeMillis());
    }

    public String addServer(String[] urls, String[] ports) throws Exception {
        if (urls.length == 0 || ports.length == 0 || urls.length != ports.length) {
            String status = "Can't add server to cluster. Check urls and ports";
            log.info(status);
            return status;
        }
        NodeInspector lastIns = getLastInspector(inspector);
        shardedTemplates = Arrays.copyOf(shardedTemplates, shardedTemplates.length + urls.length);
        int k = 0;
        for (String url :
                urls) {
            final int r = k;
            SocketChannelProvider provider = (retryNumber, lastError) -> {
                if (lastError != null) log.error("err ", lastError);
                try {
                    return SocketChannel.open(new InetSocketAddress(url, Integer.valueOf(ports[r])));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            };
            log.info("init new tarantool shard url {}:{}", url, ports[k]);

            TarantoolClientImpl newClient = new TarantoolClientImpl(provider, commonConfig);
            shardedTemplates[shardedTemplates.length - urls.length + k] = newClient;
            k++;
        }
        if (lastIns == null) {
            NodeInspector prevIns = new NodeInspector(shardedTemplates.length);
            prevIns.setTimestamp(System.currentTimeMillis());
            inspector.setPreviousInspector(prevIns);
            reloadData(inspector);
        } else {
            NodeInspector prevIns = new NodeInspector(shardedTemplates.length);
            prevIns.setTimestamp(System.currentTimeMillis());
            lastIns.setPreviousInspector(prevIns);
            reloadData(lastIns);
        }
        return "start to reload data";
    }

    @Async
    private void reloadData(NodeInspector inspector) throws Exception {
        List<List> spaces = TarantoolUtils.getTarantoolManyResults(getSpaces());
        if (spaces == null)
            return;
        List<Structure> rows = new ArrayList<>(spaces.size());
        for (List fields: spaces) {
            rows.add(convertRow(fields));
        }
        for (int i=0; i<shardedTemplates.length; i++) {
            final int k = i;
            rows.forEach(space -> {
                List<?> keysObj = shardedTemplates[k].syncOps().eval("return box.space." + space.name + ":select{}");
                List<List> keysList = TarantoolUtils.getTarantoolManyResults(keysObj);
                if (keysList != null) {
                    List<KeyCommon> keys = new ArrayList<KeyCommon>(keysList.size());
                    for (List fields : keysList) {
                        keys.add(convertKey(fields));
                    }
                    keys.forEach(key -> {
                        if (inspector.getTimestamp() > key.getTimestamp()) {
                            String command = "return box.space." + space.name + ":select{" + key.getId() + "}";
                            List<List> dataPortion = TarantoolUtils
                                    .getTarantoolManyResults(shardedTemplates[k].syncOps().eval(command));

                            StringBuilder data = new StringBuilder("{");
                            if (dataPortion != null && !dataPortion.isEmpty()) {
                                for(List fields: dataPortion) {
                                    for (Object field : fields) {
                                        data.append(field + ", ");
                                    }
                                }
                                data.delete(data.length()-3, data.length()-1);
                                data.append("}");
                            }
                            getShardTemplate(key.getId().toString()).syncOps().eval("box.space." + space.name +
                                    ":insert(" + data.toString() + ")");
                            shardedTemplates[k].syncOps().eval("box.space." + space + ":delete{" + key.getId() + "}");
                        }
                    });
                }
            });
        }
        inspector.setPreviousInspector(null);
        log.info("resharding is done");
    }

    public NodeInspector getInspector() {
        return inspector;
    }

    public NodeInspector getLastInspector(NodeInspector inspector) {
        NodeInspector lastIns = inspector;
        while (inspector.getPreviousInspector() != null) {
            lastIns = inspector.getPreviousInspector();
        }
        return lastIns;
    }
}
