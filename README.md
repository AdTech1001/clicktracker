# Click Tracker Application
Click Tracker API is an application for tracking mobile client clicks for various campaigns. It is a Google App Engine project based on Google Cloud Endpoints. 

There are two API endpoints:
  - Admin API: used by admins for campaign management and click analytics,
  - *User API*: endpoint for user clicks.

## Demo
A working demo project is deployed on GAE. API explorer is available on https://clicktracker-mb.appspot.com/_ah/api/explorer

When invoking calls always use **Execute without oath**. Use the *apiKey* property to authenticate. apiKey: dog2dog 
  
## Deploying to App Engine
Edit *pom.xml* and configure the <pre>app.id</pre> property. From project root run:
```
mvn appengine:update
```
Test the deployment with the API explorer: <pre>https://<your-google-app-id>.appspot.com/_ah/api/explorer</pre>

## Local development environment
Prerequisites:
- JDK 7
- [Maven 3](http://maven.apache.org) or greater
- If you want to use *Eclipse*:
  - Install [M2Eclipse plugin](http://www.eclipse.org/m2e/m2e-downloads.html)
  - Import project. For source select *Maven->Existing Maven projects*
  - To run the project from Eclipse use *Run as->Maven build*
  - Access the API locally: https://localhost:8080/_ah/api/explorer