package com.freelancer.catpcha.solver.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CaptchaRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;

    private String hostName;

    private String siteKey;

    private LocalDateTime requestTimeStamp;

    private LocalDateTime solutionTimeStamp;

    @Column(length = 2000)
    private String solution;

    private boolean assigned;

    public Long getRequestId() {
        return requestId;
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

    public LocalDateTime getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(LocalDateTime requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public LocalDateTime getSolutionTimeStamp() {
        return solutionTimeStamp;
    }

    public void setSolutionTimeStamp(LocalDateTime solutionTimeStamp) {
        this.solutionTimeStamp = solutionTimeStamp;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

}
