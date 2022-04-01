/*
 * Copyright (c) Axway Software, 2017. All Rights Reserved.
 */
package com.axway.st.plugins.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axway.st.plugins.authentication.UserData;

/**
 * Provides implementation of user data.
 */
public class UserDataBean implements UserData {

    /**
     * 
     */
    private static final long serialVersionUID = 8322886229338574456L;

    private String mUsername;
    private int mUID;
    private int mGID;
    private String mEmail;
    private String mHomeDir;
    private Map<String, List<String>> mAdditionalAttributes;

    public UserDataBean(String username, int uid, int gid, String email, String homeDir) {
        mUsername = username;
        mUID = uid;
        mGID = gid;
        mEmail = email;
        mHomeDir = homeDir;
        mAdditionalAttributes = new HashMap<>();
    }

    public UserDataBean(UserData userData) {
        mUsername = userData.getUsername();
        mUID = userData.getUID();
        mGID = userData.getGID();
        mEmail = userData.getEmail();
        mHomeDir = userData.getHomeDir();

        setAdditionalAttributes(userData.getAdditionalAttributes());
    }

    @Override
    public String getUsername() {
        return mUsername;
    }

    @Override
    public int getUID() {
        return mUID;
    }

    @Override
    public int getGID() {
        return mGID;
    }

    @Override
    public String getEmail() {
        return mEmail;
    }

    @Override
    public String getHomeDir() {
        return mHomeDir;
    }

    @Override
    public Map<String, List<String>> getAdditionalAttributes() {
        return new HashMap<>(mAdditionalAttributes);
    }

    public void setAdditionalAttributes(Map<String, List<String>> additionalAttributes) {
        mAdditionalAttributes = new HashMap<>(additionalAttributes);
    }

    /**
     * NullObject pattern for {@link UserDataBean}
     */
    static class NullObject extends UserDataBean {

        NullObject() {
            super("", 0, 0, "", "");
        }
    }
}
