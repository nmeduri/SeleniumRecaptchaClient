package com.freelancer.catpcha.solver.controller;

import com.freelancer.catpcha.solver.dto.Response;
import com.freelancer.catpcha.solver.dto.SolutionRequest;
import com.freelancer.catpcha.solver.dto.SolveRequest;
import com.freelancer.catpcha.solver.entity.CaptchaRequest;
import com.freelancer.catpcha.solver.repository.CaptchaRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class CaptchaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class.getName());

    @Autowired
    private CaptchaRequestRepository captchaRequestRepository;

    @PostMapping(path = "/solve")
    public CaptchaRequest solve(@RequestBody SolveRequest solveRequest) {
        LOGGER.info("Solved request {}", solveRequest);
        CaptchaRequest request = new CaptchaRequest();
        request.setHostName(solveRequest.getHost());
        request.setSiteKey(solveRequest.getSiteKey());
        request.setRequestTimeStamp(LocalDateTime.now());
        request = captchaRequestRepository.save(request);
        return request;
    }

    @PostMapping(path = "/solution")
    public Response solution(@RequestBody SolutionRequest solutionRequest) {
        LOGGER.info("Solution request {}", solutionRequest);
        CaptchaRequest request = captchaRequestRepository.findOneByRequestId(solutionRequest.getId());
        request.setSolution(solutionRequest.getSolution());
        request.setSolutionTimeStamp(LocalDateTime.now());
        request = captchaRequestRepository.save(request);
        Response response = new Response();
        if(request.getSolution() != null && !request.getSolution().isEmpty()) {
            response.setMessage("success");
            response.setStatus(200);
        } else {
            response.setMessage("failed");
            response.setStatus(400);
        }
        return response;
    }

    @GetMapping(path = "/solution/{id}")
    public CaptchaRequest solution(@PathVariable Long id) {
        return captchaRequestRepository.findOneByRequestId(id);
    }

    @GetMapping(path = "/unsolved")
    public CaptchaRequest unsolved() {
        return captchaRequestRepository.findFirstBySolutionTimeStampIsNullAndAssigned(false);
    }

    @GetMapping(path = "/acceptChallenge/{id}")
    public Response acceptChallenge(@PathVariable Long id) {
        LOGGER.info("Accepting challenge for {}", id);
        Response response = new Response();
        response.setStatus(400);
        response.setMessage("Failed");
        CaptchaRequest request = captchaRequestRepository.findOneByRequestId(id);
        if(request != null) {
            if(request.isAssigned()) {
                response.setStatus(400);
                response.setMessage("already assigned");
            } else {
                request.setAssigned(true);
                request = captchaRequestRepository.save(request);
                if (request.isAssigned()) {
                    response.setStatus(200);
                    response.setMessage("success");
                }
            }
        }
        return response;
    }

}
