package ru.ddyakin.dao;

import ru.ddyakin.tarantool.sharding.ShardedTarantoolTemplateManager;

public class TarantoolWriterImpl implements TarantoolWriterDao {

    private ShardedTarantoolTemplateManager templateManager;

    public TarantoolWriterImpl(ShardedTarantoolTemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public TarantoolWriterImpl() {
    }



    public void setTemplateManager(ShardedTarantoolTemplateManager templateManager) {
        this.templateManager = templateManager;
    }
}
