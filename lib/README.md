Pluggable Authentication Libraries
===================================

These libraries contain the classes you need for 
implementation of authentication plugins.

Source Structure
----------------

      securetransport-plugins-authentication.pom            maven pom file for packaging libraries and samples
      securetransport-plugins-authentication-samples.pom    maven pom file for sample packaging
      securetransport-plugins-authentication-spi.jar        library with authentication spi classes
      securetransport-plugins-authentication-services.jar   library with authentication service classes
      README.md                                             this file
      pom.xml                                               maven script file for installing the libraries

Install libraries in local maven repository
-------------------------------------------
- execute command: mvn package