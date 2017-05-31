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
import ru.ddyakin.jdo.*;

@Controller
public class TarantoolWriterController {
    private static final Logger log = LoggerFactory.getLogger(TarantoolLoaderController.class);

    @Autowired
    TarantoolWriterDao writerDao;

    @RequestMapping(path = "/addStruct", method = RequestMethod.POST)
    @ResponseBody
    AddStructResponse addStruct(@RequestBody Struct request) {
        try{
            return new AddStructResponse(writerDao.processStructRequest(request));
        } catch (Exception ex) {
            log.error("err ", ex);
            return new AddStructResponse(ex.getMessage());
        }
    }

    @RequestMapping(path = "/writeData", method = RequestMethod.POST)
    @ResponseBody
    WriteResponse write(@RequestBody WriteRequest request) {
        try {
            return writerDao.processWrite(request);
        } catch (Exception ex) {
            log.error("err ", ex);
            return new WriteResponse(ex.getMessage());
        }
    }

    @RequestMapping(path = "/updateData", method = RequestMethod.POST)
    @ResponseBody
    UpdateResponse update(@RequestBody UpdateRequest request) {
        try {
            return writerDao.processUpdate(request);
        } catch (Exception ex) {
            log.error("err ", ex);
            return new UpdateResponse(ex.getMessage());
        }
    }

    @RequestMapping(path = "/removeData", method = RequestMethod.POST)
    @ResponseBody
    RemoveResponse remove(@RequestBody RemoveRequest request) {
        try {
            return writerDao.processRemove(request);
        } catch (Exception ex) {
            log.error("err ", ex);
            return new RemoveResponse(ex.getMessage());
        }
    }
}
