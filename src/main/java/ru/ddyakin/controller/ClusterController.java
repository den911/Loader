package ru.ddyakin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ddyakin.logic.ClusterLogic;

@Controller
public class ClusterController {
    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    ClusterLogic clusterLogic;

    @RequestMapping(path = "/cluster/add", method = RequestMethod.GET)
    public void addServer(@RequestParam("url") String serverUrl, @RequestParam("port") String serverPort) {
        try{
            clusterLogic.addServer(serverUrl, serverPort);
        } catch (Exception ex) {
            log.error("err ", ex);
        }
    }

}
