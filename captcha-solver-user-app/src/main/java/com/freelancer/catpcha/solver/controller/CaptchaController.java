package com.freelancer.catpcha.solver.controller;

import com.freelancer.catpcha.solver.ui.communicator.Communicator;
import com.freelancer.catpcha.solver.util.HostUtil;
import com.freelancer.catpcha.solver.util.HtmlUtil;
import com.freelancer.catpcha.solver.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class.getName());

    @Autowired
    private HtmlUtil htmlUtil;

    @Autowired
    private HostUtil hostUtil;

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private Communicator communicator;

    @PostMapping(path = "/solved")
    public String captchaSolved(@RequestParam String solution, @RequestParam String host, @RequestParam String id) {
        LOGGER.info("Solution success {}", solution);
        LOGGER.info("Removing host {} from hosts file", host);
        if(!hostUtil.removeHostEntry(host)) {
            return "failed";
        }
        restUtil.submitSolution(id, solution);
        communicator.solutionSubmitted();
        return "success";
    }

}
