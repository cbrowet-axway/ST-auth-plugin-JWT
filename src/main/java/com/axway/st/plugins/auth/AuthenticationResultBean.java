/*
 * Copyright (c) Axway Software, 2017. All Rights Reserved.
 */
package com.axway.st.plugins.auth;

import com.axway.st.plugins.authentication.AuthenticationResult;
import com.axway.st.plugins.authentication.UserData;

/**
 * Authentication result implementation for authentication.
 */
public class AuthenticationResultBean implements AuthenticationResult {

    private String mResultMsg;
    private UserData mUserData;
    private ExitCode mExitCode;

    public AuthenticationResultBean(String resultMsg, ExitCode exitCode) {
        this(resultMsg, new UserDataBean.NullObject(), exitCode);
    }

    public AuthenticationResultBean(String resultMsg, UserData userData, ExitCode exitCode) {
        mResultMsg = resultMsg;
        mUserData = userData;
        mExitCode = exitCode;
    }

    @Override
    public String getMessage() {
        return mResultMsg;
    }

    @Override
    public UserData getResult() {
        return new UserDataBean(mUserData);
    }

    @Override
    public ExitCode getExitCode() {
        return mExitCode;
    }

}
