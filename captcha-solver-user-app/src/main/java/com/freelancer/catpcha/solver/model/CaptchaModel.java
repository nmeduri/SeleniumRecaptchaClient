package com.freelancer.catpcha.solver.model;

import java.util.StringJoiner;

public class CaptchaModel {

    private final String id;
    private final String host;
    private final String siteKey;

    public CaptchaModel(Long id, String host, String siteKey) {
        this.id = String.valueOf(id);
        this.host = host;
        this.siteKey = siteKey;
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public String getSiteKey() {
        return siteKey;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("    ");
        joiner.add(String.valueOf(getId()));
        joiner.add(getHost());
        return joiner.toString();
    }

}
