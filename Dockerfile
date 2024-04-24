FROM eclipse-temurin
COPY /target/job-huntv1.jar /job-huntv1.jar
ENTRYPOINT ["java", "-jar", "/job-huntv1.jar"]
