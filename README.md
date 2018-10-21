# Jenkins_pipeline

Jenkins pipeline which do the following jobs automatically:

1. Checkout code from remote repository.
  
2. Building code (maven project).
  
3. Testing the code using mvn tests.
  
4. Triggering job and fetching artefact after finishing. Jobs are described using DSL in jobs.groovy file in this repo.

5. Packaging and Publishing results (into Nexus repository).
  
6. Asking for manual approval.
  
7. Deployment to Apache Tomcat application server.
