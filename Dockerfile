FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY ./target/MyAuthorisationSystemTest-1.0.jar /app/MyAuthorisationSystemTest-1.0.jar
EXPOSE 8889
ENTRYPOINT ["java", "-jar" , "MyAuthorisationSystemTest-1.0.jar"]
