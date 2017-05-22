package ru.ddyakin.tarantool.sharding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.tarantool.SocketChannelProvider;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientConfig;
import org.tarantool.TarantoolClientImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class ShardedTarantoolTemplateManager {
    private static final Logger log = LoggerFactory.getLogger(ShardedTarantoolTemplateManager.class);

    public final TarantoolClient[] shardedTemplates;
    public TarantoolClient masterTemplate;

    public ShardedTarantoolTemplateManager(TarantoolClient[] shardedTemplates, TarantoolClient masterTemplate) {
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
        shardedTemplates = new TarantoolClient[shards - 1];
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
                    throw new IllegalStateException(e);
                    //todo restart node
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

    public TarantoolClient getShardTemplate(String shardStr) {
        return shardedTemplates[chooseShard(shardStr)];
    }

    public int chooseShard(String shardStr) {
        return Math.abs(shardStr.hashCode()) % getShards();
    }



}
