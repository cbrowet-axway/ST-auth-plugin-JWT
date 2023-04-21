JWT Authentication Plug-in
==================================

Introduction
------------

The sample is a simple Custom Authentication implementation that 
allows users to log in to SecureTransport through a JWT token.
After the deployment of the sample, an administrator could assign
admins and/or end-users to authenticate through this plug-in.

Source Structure
----------------

      - src
        + java          source files of the custom authentication plug-in
        + resources     plug-in's manifest
      README.md         this file
      pom.xml           maven build file


Building the Sample Application
-------------------------------

0. Prerequisites:
    * JDK 1.7+
    * Pluggable authentication libraries installed locally
      * run `mvn install -f lib/pom.xml` to install the libraries

1. Build this authentication plug-in with maven.


Deploy
------

1.  Copy the built jar file to the  
    `{$FILEDRIVE_HOME}/plugins/authentication/` directory, where
    `{$FILEDRIVE_HOME}` is the SecureTransport installation
    directory (e.g. `/opt/Axway/SecureTransport`).
  
2.  Restart the SecureTransport admin and tm daemons.
3.  Enable the plugin ("plugin-jwtauth") in the ST configuration ("Plugins.Authentication.EndUser.Registry")

Usage
-----

* The plugin expects the JWT token in a "X-JWT-TOKEN" header.  
  * Per ST's requirements, a Basic authentication header *must* be present. The user/password used is not taken into account, though.
* If not found, and if the username of basic auth is `x-jwt-token`, the password is assumed to be the JWT
* For this plugin to work, the users's "Password is stored locally" setting must be disabled, or a "tried to authenticate virtual account with login name 'axway' whose password is stored locally. Cannot be authenticated externally" error occurs.

Parameters
----------

- `Plugins.Authentication.plugin-jwtauth.jwksURL`: The URL of the JWKS. "file:///" protocol is supported.
- `Plugins.Authentication.plugin-jwtauth.usernameClaim`; The JWT claim containing the username passed to ST.