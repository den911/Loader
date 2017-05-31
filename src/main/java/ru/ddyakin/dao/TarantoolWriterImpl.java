package ru.ddyakin.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tarantool.TarantoolClientImpl;
import ru.ddyakin.jdo.*;
import ru.ddyakin.tarantool.sharding.ShardedTarantoolTemplateManager;

import java.util.*;

@Component
public class TarantoolWriterImpl implements TarantoolWriterDao {
    private static final Logger log = LoggerFactory.getLogger(TarantoolWriterImpl.class);

    private final static String STATUS_OK = "Ok";

    private final static Set<String> SET_OF_KEY_TYPE = new HashSet<>(Arrays.asList("hash", "tree", "bitset", "rtree"));
    private final static Set<String> SET_OF_P_KEY_TYPE = new HashSet<>(Arrays.asList("unsigned", "string", "", ""));

    private ShardedTarantoolTemplateManager templateManager;

    public TarantoolWriterImpl(ShardedTarantoolTemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public TarantoolWriterImpl() {
    }

    public String processStructRequest(Struct request) {
        String status = validateStruct(request);
        if (status.equals(STATUS_OK)) {
            final StringBuilder spaceKeyConfig = new StringBuilder();

            spaceKeyConfig.append("s = box.schema.space.create('")
                    .append(request.getSpaceName())
                    .append("sKey', {if_not_exists = true})\n")
                    .append("p = s:create_index('primary', {type = 'hash', parts = {1, 'unsigned'}, if_not_exists = true})\n");
            StringBuilder spaceConfig = createStructure(templateManager.masterTemplate, request);

            templateManager.masterTemplate.syncOps().eval(spaceKeyConfig.toString());
            String spaceC = StringUtils.replace(spaceConfig.toString(), "'", "\\'");
            spaceC = StringUtils.replace(spaceC, "\n", " ");
            templateManager.masterTemplate.syncOps().eval("box.space.spaces_config:auto_increment{'"+ request.getSpaceName() +"','"+spaceC+"'}");
            templateManager.masterTemplate.syncOps().eval("box.space.spaces:auto_increment{"+request.getSpaceName()+"}");
        }
        return status;
    }

    private StringBuilder createStructure(TarantoolClientImpl template, Struct structure) {
        final StringBuilder spaceConfig = new StringBuilder();

        spaceConfig.append("s = box.schema.space.create('")
                .append(structure.getSpaceName())
                .append("', {if_not_exists = true})\n")
                .append("p = s:create_index('primary', {type = 'hash', parts = {1, 'unsigned'}, if_not_exists = true})\n")
                .append("s:create_index('secondary', {")
                .append(" type = 'tree',")
                .append(" unique = false,")
                .append(" parts = {1, 'unsigned'},")
                .append(" if_not_exists = true")
                .append("})\n");

        if (structure.getKeys() != null && !structure.getKeys().isEmpty())
            structure.getKeys().forEach(key ->{
                if (SET_OF_P_KEY_TYPE.contains(key.getKey()))
                    spaceConfig.append("s:create_index('secondary', {" +
                            " type = 'tree'," +
                            " unique = "+ key.getUnique().toString() +"," +
                            " parts = {1, '" + key.getKey() + "'}," +
                            " if_not_exists = true" +
                            "})\n");
            });

        template.syncOps().eval(spaceConfig.toString());
        return spaceConfig;
    }

    private String validateStruct(Struct struct) {
        if (StringUtils.isEmpty(struct.getSpaceName()))
            return "space name is empty!";
        if (!SET_OF_KEY_TYPE.contains(struct.getTypeKey()))
            return "unsopported key type";
        return STATUS_OK;
    }

    @Override
    public WriteResponse processWrite(WriteRequest request) {
        return null;
    }

    @Override
    public UpdateResponse processUpdate(UpdateRequest request) {
        return null;
    }

    @Override
    public RemoveResponse processRemove(RemoveRequest request) {
        return null;
    }

    //    public String processInsertRequest(InsertRequest request) {
//        String status = null;
//        status = validateInsertRequest(request);
//        if (status.equals(STATUS_OK)) {
//            NodeInspector inspector = templateManager.getLastInspector(templateManager.getInspector());
//            if (inspector != null) {
//                List<String> keysForShard = new ArrayList<>();
//                for (int i=0; i<request.getShardKeys().size(); i++)
//                    keysForShard.add(request.getKeys().get(request.getShardKeys().get(i)).getKey());
//                TarantoolClientImpl shardClient  = templateManager.getShardTemplate(makeShardKey(keysForShard));
//                shardClient.syncOps().insert(getLastIndexIdInSpace(request.getSpaceName(), shardClient) + 1,
//                        request.getFields());
//            } else {
//                status = "inspector must be initialize";
//                log.info(status);
//            }
//        }
//        return status;
//    }

//    private Integer getLastIndexIdInSpace(String space, TarantoolClientImpl client) {
//        Integer count = 1;
//        List<?> result = client.syncOps().eval("return box.space."+space+":count()");
//        if (result == null) {
//            log.error("err - space not created");
//        }
//        List<String> l = TarantoolUtils.getTarantoolUniqueResult(result);
//        if (l != null)
//        return Integer.valueOf(l.get(0));
//    }

    private static String makeShardKey(List<String> keys) {
        if (keys == null || keys.isEmpty()) return null;
        final StringBuilder shardKey = new StringBuilder("");
        keys.forEach(key -> shardKey.append(key).append(":"));
        return shardKey.substring(0, shardKey.length() - 1);
    }

//    private static String calcShardKey(Struct request) {
//        List<String> keysForShard = new ArrayList<>();
//        for (int i=0; i<request.getShardKeys().size(); i++)
//            keysForShard.add(request.getKeys().get(request.getShardKeys().get(i)).getKey());
//        return makeShardKey(keysForShard);
//    }

    @Autowired
    public void setTemplateManager(ShardedTarantoolTemplateManager templateManager) {
        this.templateManager = templateManager;
    }

//    private String validateStruct(Struct struct) {
//        final String[] status = new String[1];
//        status[0] = STATUS_OK;
//        if (StringUtils.isEmpty(struct.getSpaceName()))
//            return "space name is empty!";
//        if (!struct.getShardKeys().isEmpty() && struct.getKeys() != null && !struct.getKeys().isEmpty()) {
//            struct.getShardKeys().forEach(key -> {
//                if (key > struct.getKeys().size() - 1 || key < struct.getKeys().size() - 1)
//                    status[0] = "shard keys are not the same with keys";
//            });
//        }
//        if (struct.getFields() == null || struct.getFields().isEmpty())
//            return "fields is empty!";
//        if (!SET_OF_KEY_TYPE.contains(struct.getTypeKey()))
//            return "unsopported key type";
//
//        return status[0];
//    }
}
