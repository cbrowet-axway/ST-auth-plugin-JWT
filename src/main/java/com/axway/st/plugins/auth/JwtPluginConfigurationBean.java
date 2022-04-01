/*
 * Copyright (c) Axway Software, 2017. All Rights Reserved.
 */
package com.axway.st.plugins.auth;

import com.axway.st.plugins.authentication.configuration.Description;
import com.axway.st.plugins.authentication.configuration.PluginConfiguration;

/**
 * Sample configuration.
 */
public class JwtPluginConfigurationBean implements PluginConfiguration {

    private static final long serialVersionUID = -6909898098060174228L;

    private String jwksURL;

    private String usernameClaim;

    public JwtPluginConfigurationBean() {
    }

    @Override
    public void initDefault() {
        jwksURL = "https://my.domain/.well-known/jwks.json";
        usernameClaim = "preferred_username";
    }

    @Description(value = "URL to get the JWKS to validate the signature of a JWT")
    public String getJwksURL() {
        return jwksURL;
    }

    public void setJwksURL(String url) {
        this.jwksURL = url;
    }

    @Description(value = "JWT claim containing the username")
    public String getUsernameClaim() {
        return usernameClaim;
    }

    public void setUsernameClaim(String usernameClaim) {
        this.usernameClaim = usernameClaim;
    }

}
