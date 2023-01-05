/*
 * Copyright (c) Axway Software, 2017. All Rights Reserved.
 */
package com.axway.st.plugins.auth;

import com.axway.st.plugins.authentication.UserData;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Factory for {@link UserData objects}
 */
public class UserManager {

    private static final String TAG = "tag";

    private static final String TAG_VALUE = "sampleUser";

    public UserData getAdmin(String username) {
        UserDataBean admin = new UserDataBean(username, 0, 0, "", "");
        addTag(admin);
        return admin;
    }

    public UserData getUser(String username) {
        UserDataBean user = new UserDataBean(
                username,
                1234, 2345,
                username + "@axway.com",
                "/usr/stusers/" + username);
        addTag(user);
        return user;
    }

    public UserData getUser(String username, int uid, int gid) {
        UserDataBean user = new UserDataBean(
                username,
                uid, gid,
                username + "@axway.com",
                "/usr/stusers/" + username);
        addTag(user);
        return user;
    }

    /**
     * Checks if <code>user</code> was created by this factory.
     */
    public boolean isOurUser(UserData user) {
        Map<String, List<String>> additionalAttributes = user.getAdditionalAttributes();
        List<String> tagValues = additionalAttributes.get(TAG);
        return ((tagValues != null) && tagValues.contains(TAG_VALUE));
    }

    /**
     * Tags the <code>user</code> as created by this factory.
     */
    private void addTag(UserDataBean user) {
        Map<String, List<String>> additionalAttributes = user.getAdditionalAttributes();
        additionalAttributes.put(TAG, Collections.singletonList(TAG_VALUE));
        user.setAdditionalAttributes(additionalAttributes);
    }
}
