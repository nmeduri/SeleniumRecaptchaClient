package com.freelancer.catpcha.solver.util;

import com.freelancer.catpcha.solver.dto.CaptchaRequest;
import com.freelancer.catpcha.solver.dto.Response;
import com.freelancer.catpcha.solver.dto.SolutionModel;
import com.freelancer.catpcha.solver.ui.communicator.Communicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtil.class.getName());

    @Autowired
    private UrlUtil urlUtil;

    @Autowired
    private Communicator communicator;

    public CaptchaRequest getNextCaptchaChallenge() {
        String unsolvedUrl = urlUtil.getUnsolvedUrl();
        LOGGER.info("Unsolved url {}", unsolvedUrl);
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<CaptchaRequest> response = template.getForEntity(unsolvedUrl, CaptchaRequest.class);
            if(!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("Captcha Server returned some error code {}", response.getStatusCodeValue());
                LOGGER.error("Captcha Server returned some error code {}", response.getBody());
                return null;
            }
            CaptchaRequest request = response.getBody();
            if(request != null) {
                LOGGER.info("Got next task to process {}", request);
                return request;
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in calling unsolved url ", ex);
        }
        return null;
    }

    //TODO improve this method to return some output and show message to user.
    public void submitSolution(String id, String solution) {
        String solutionUrl = urlUtil.getSolutionUrl();
        LOGGER.info("Solution url {}", solutionUrl);
        RestTemplate template = new RestTemplate();
        try {
            SolutionModel solutionModel = new SolutionModel();
            solutionModel.setId(id);
            solutionModel.setSolution(solution);
            ResponseEntity<Response> response = template.postForEntity(solutionUrl, solutionModel, Response.class);
            if(!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("Captcha Server returned some error code {}", response.getStatusCodeValue());
                LOGGER.error("Captcha Server returned some error code {}", response.getBody());
                return;
            }
            LOGGER.info("response from submit solution {}", response.getBody());
            communicator.enableEverything();
        } catch (Exception ex) {
            LOGGER.error("Exception in calling unsolved url ", ex);
        }
    }

    public Response acceptChallenge(String id) {
        String acceptUrl = urlUtil.getAcceptUrl() + "/" + id;
        LOGGER.info("AcceptUrl url {}", acceptUrl);
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<Response> response = template.getForEntity(acceptUrl, Response.class);
            if(!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("Captcha Server returned some error code {}", response.getStatusCodeValue());
                LOGGER.error("Captcha Server returned some error code {}", response.getBody());
                return null;
            }
            LOGGER.info("response from accept challenge {}", response.getBody());
            communicator.disableEverything();
            return response.getBody();
        } catch (Exception ex) {
            LOGGER.error("Exception in calling unsolved url ", ex);
        }
        return null;
    }

}
