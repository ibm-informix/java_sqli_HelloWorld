#Java SQLI Hello World Sample Application

##Where are the important files?

###Relevant files:

####src/com/ibm/informix/java_sqli_HelloWorld.java

This file contains all of the sample data interacting with the database.

####manifest.yml

If deploying to bluemix, this file gives details about the application.

####build.gradle

This file gathers dependencies and will build a war file if needed to deploy to Bluemix.

##What can I do with this example?

###Option 1: Deploy to Bluemix

####Requirements:

Git - Used to download the application.

Gradle -  Used to get dependencies and build the application.

CloudFoundry CLI -  Used to push the application to Bluemix.

####Procedure:

 * Step 1: Clone repository to local machine

 * Step 2: Download driver and place it in the WebContent/WEB-INF/lib/ directory. You may need to create a 'lib' folder.

[Informix JDBC driver (SQLI) Download](https://www-01.ibm.com/marketing/iwm/tnd/search.jsp?go=y&rs=ifxjdbc)

 * Step 3: Use gradle to build a war file.
	
 * Step 4: Push application to Bluemix using CloudFoundry CLI.

###Option 2: Run locally

####Requirements:

Git - Used to download the application.

Gradle -  Used to get dependencies.

####Procedure:

 * Step 1: Clone repository to local machine
 
 * Step 2: Specify the connection information
 
 * Step 3: Download driver
 
SQLI Download -> https://www-01.ibm.com/marketing/iwm/tnd/search.jsp?go=y&rs=ifxjdbc

 * Step 4: Use gradle to copy runtime dependencies

 * Step 5: Deploy as a web application
