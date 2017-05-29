package ru.ddyakin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ddyakin.dao.TarantoolWriterDao;
import ru.ddyakin.logic.LoaderLogic;

@Controller
public class TarantoolLoaderController {
    private static final Logger log = LoggerFactory.getLogger(TarantoolLoaderController.class);

    @Autowired
    LoaderLogic loaderLogic;

    @Autowired
    TarantoolWriterDao writer;

    @RequestMapping("/")
    public String main(Model model) {

        model.addAttribute("structs", loaderLogic.getRows());

        return "rows";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editJob(@RequestParam("name") String structure,
                          Model model) {
//        model.addAttribute("struct", batchLogic.getJob(jobName));
//        model.addAttribute("environment", environment);

        return "editJob";
    }


//    @RequestMapping("/rows")
//    public String getAllJobList(Model model) {
////        model.addAttribute("jobsGroupName", "Dev jobs");
////        model.addAttribute("jobs", batchLogic.getJobs());
////        model.addAttribute("environment", environment);
//
//
//    }


}
