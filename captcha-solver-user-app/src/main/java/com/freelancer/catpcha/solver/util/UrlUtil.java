package com.freelancer.catpcha.solver.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {

    @Value("${captcha.server.host}")
    private String captchaServerHost;

    @Value("${captcha.server.unsolved.url}")
    private String unsolvedUrl;

    @Value("${captcha.server.solution.url}")
    private String solutionUrl;

    @Value("${captcha.server.accept.url}")
    private String acceptUrl;

    private String getCaptchaServerHost() {
        return captchaServerHost;
    }

    public String getUnsolvedUrl() {
        return getCaptchaServerHost() + unsolvedUrl;
    }

    public String getSolutionUrl() {
        return getCaptchaServerHost() + solutionUrl;
    }

    public String getAcceptUrl() {
        return getCaptchaServerHost() + acceptUrl;
    }

}
