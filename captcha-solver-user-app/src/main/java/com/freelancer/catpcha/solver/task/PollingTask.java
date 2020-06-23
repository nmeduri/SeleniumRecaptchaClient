package com.freelancer.catpcha.solver.task;

import com.freelancer.catpcha.solver.ui.Dashboard;
import com.freelancer.catpcha.solver.ui.communicator.Communicator;
import com.freelancer.catpcha.solver.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.TimerTask;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class PollingTask extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollingTask.class.getName());

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private Communicator communicator;

    @Override
    public void run() {
        LOGGER.info("Polling task started. {}", LocalDateTime.now());
        communicator.addRequest(restUtil.getNextCaptchaChallenge());
    }

}
