/*
 * Copyright (c) Axway Software, 2017. All Rights Reserved.
 */
package com.axway.st.plugins.auth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.axway.st.plugins.authentication.AuthenticationResult;
import com.axway.st.plugins.authentication.BasicAuthenticationRequest;
import com.axway.st.plugins.authentication.BasicAuthenticator;
import com.axway.st.plugins.authentication.UserData;
import com.axway.st.plugins.authentication.services.LoggingService;

import javax.inject.Inject;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Sample authenticator implementation for basic authentication.
 */
public class JwtAuthenticatorImpl implements BasicAuthenticator {

    /**
     * Provides logging service.
     */
    @Inject
    private LoggingService mLogger;

    @Inject
    private JwtPluginConfigurationBean mConfiguration;

    public JwtAuthenticatorImpl() {
    }

    @Override
    public AuthenticationResult authenticate(BasicAuthenticationRequest authRequest) {

        mLogger.debug("Start JWT authentication process...");

        switch (authRequest.getProtocol().toLowerCase()) {
            case "http":
                break;
            default:
                return new AuthenticationResultBean(
                    String.format("Unsupported protocol %s.", authRequest.getProtocol().toLowerCase()),
                        AuthenticationResult.ExitCode.CONTINUE);
        }

        Map<String, List<String>> headers = authRequest.getAdditionalParameters();
        mLogger.debug(String.format("headers: %s.", headers));

        String theJWT = "";
        List<String> tokens = headers.get("X-JWT-TOKEN");
        if (tokens != null) {
            theJWT = tokens.get(0);
        }

        if (theJWT.isEmpty() && authRequest.getUsername().equalsIgnoreCase("x-jwt-token")) {
            mLogger.info("X-JWT-TOKEN user found. Assuming password = token.");
            theJWT = new String(authRequest.getPassword());
        }

        // /* This works for the plugin, but makes ST hiccup with a "java.lang.StackOverflowError" */
        // if (theJWT.isEmpty()) {
        //     mLogger.warn("No X-JWT-TOKEN header found. Assuming username = token.");
        //     theJWT = authRequest.getUsername();
        // }

        mLogger.debug(String.format("JWT: %s.", theJWT));
        if (theJWT == null || theJWT.isEmpty()) {
            mLogger.error("No JWT found");
            return new AuthenticationResultBean("No JWT found",
                    AuthenticationResult.ExitCode.FAILURE);
        }

        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(theJWT);
            JwkProvider provider = new UrlJwkProvider(new URL(mConfiguration.getJwksURL()));
            Jwk jwk = provider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);
        } catch (Exception e) {
            mLogger.error(String.format("Error validating JWT. %s", e));
            return new AuthenticationResultBean("Error validating JWT.",
                    AuthenticationResult.ExitCode.FAILURE);
        }

        Date cur_date = new Date();
        Date notBefore = jwt.getNotBefore();
        Date expiresAt = jwt.getExpiresAt();

        if (notBefore == null)
            mLogger.warn("no 'nbf' (not_before) in JWT");
        else if (notBefore.after(cur_date)) {
            mLogger.error(String.format("Error validating JWT: not_before: %s", notBefore));
            return new AuthenticationResultBean(String.format("Error validating JWT: not_before: %s", notBefore),
                    AuthenticationResult.ExitCode.FAILURE);
        }
        if (expiresAt == null)
           return new AuthenticationResultBean("Error validating JWT: invalide 'exp' (expiration date)",
                AuthenticationResult.ExitCode.FAILURE);

        else if (expiresAt.before(cur_date)) {
            mLogger.error(String.format("Error validating JWT: expired: %s", expiresAt));
            return new AuthenticationResultBean(String.format("Error validating JWT: expired: %s", expiresAt),
                    AuthenticationResult.ExitCode.FAILURE);
        }

        // String issuer = jwt.getIssuer();
        // String subject = jwt.getSubject();
        // List<String> audience = jwt.getAudience();

        // String cl_email = jwt.getClaim("email").asString();

        Claim cl_username = jwt.getClaim(mConfiguration.getUsernameClaim());
        Claim cl_uid = jwt.getClaim("uid");
        Claim cl_gid = jwt.getClaim("gid");

        UserDataBean user;
        UserManager userManager = new UserManager();
        user = userManager.getUser(cl_username.asString(), cl_uid.isNull() ? 1234 : cl_uid.asInt(), cl_gid.isNull() ? 2345 : cl_gid.asInt());

        mLogger.info(String.format("User '%s' authenticated successfully.", cl_username.asString()));
        return new AuthenticationResultBean(String.format("User '%s' authenticated successfully.", cl_username.asString()), user,
                AuthenticationResult.ExitCode.SUCCESS);

    }
}
