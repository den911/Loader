package ru.ddyakin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ddyakin.dao.TarantoolWriterDao;
import ru.ddyakin.jdo.AddStructRequest;
import ru.ddyakin.jdo.AddStructResponse;

@Controller
public class TarantoolWriterController {
    private static final Logger log = LoggerFactory.getLogger(TarantoolLoaderController.class);

    @Autowired
    TarantoolWriterDao writerDao;

    @RequestMapping(path = "/addStruct", method = RequestMethod.POST)
    @ResponseBody
    AddStructResponse addStruct(@RequestBody AddStructRequest request) {
        return null;
    }
}
