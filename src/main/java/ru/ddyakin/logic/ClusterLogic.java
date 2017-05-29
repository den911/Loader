package ru.ddyakin.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ddyakin.tarantool.sharding.ShardedTarantoolTemplateManager;

@Service
public class ClusterLogic {
    private static final Logger log = LoggerFactory.getLogger(ClusterLogic.class);

    @Autowired
    ShardedTarantoolTemplateManager templateManager;

    public void addServer(String url, String port) throws Exception {
        String[] urls = {url};
        String[] ports = {port};
        templateManager.addServer(urls, ports);
    }

}
