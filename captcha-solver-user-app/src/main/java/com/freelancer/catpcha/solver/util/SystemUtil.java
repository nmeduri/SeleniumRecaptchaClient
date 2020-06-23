package com.freelancer.catpcha.solver.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemUtil {

    @Value("${period.seconds}")
    private long period;

    @Value("${delay.seconds}")
    private long delay;

    @Value("${automate}")
    private boolean automate;

    public String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public String[] getCommand(String host) {
        String osName = getOSName();
        String url = "http://" + host + ":9999/index.html";
        if("linux".equalsIgnoreCase(osName) || "mac".equalsIgnoreCase(osName)) {
            return new String[]{"google-chrome", url};
        }
        //TODO 4) Hard coded path of chrome for windows. Change it according to your system
        return new String[]{"C:/Program Files (x86)/Google/Chrome/Application/chrome.exe", url};
    }

    public String getDomainName(String host) {
        String domain = host.replaceAll("http://", "").replaceAll("https://", "").replaceAll("www.", "");
        if(domain.contains("/")) {
            domain = domain.substring(0, domain.indexOf('/'));
        }
        return domain;
    }

    public long getPeriod() {
        return period * 1000;
    }

    public long getDelay() {
        return delay * 1000;
    }

    public boolean isAutomate() {
        return automate;
    }
}
