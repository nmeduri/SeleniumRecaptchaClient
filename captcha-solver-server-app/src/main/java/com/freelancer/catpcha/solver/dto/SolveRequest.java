package com.freelancer.catpcha.solver.dto;

public class SolveRequest {

    private String host;
    private String siteKey;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    @Override
    public String toString() {
        return "SolvedRequest{" +
                "host='" + host + '\'' +
                ", siteKey='" + siteKey + '\'' +
                '}';
    }
}
