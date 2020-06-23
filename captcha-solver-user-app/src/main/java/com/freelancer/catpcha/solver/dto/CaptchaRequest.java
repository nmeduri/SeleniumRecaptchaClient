package com.freelancer.catpcha.solver.dto;

public class CaptchaRequest {

    private Long requestId;

    private String hostName;

    private String siteKey;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    @Override
    public String toString() {
        return "CaptchaRequest{" +
                "requestId=" + requestId +
                ", hostName='" + hostName + '\'' +
                ", siteKey='" + siteKey + '\'' +
                '}';
    }
}
