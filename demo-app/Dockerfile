# Use Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY app.java ./src/main/java/App.java

# Package the application
RUN mvn clean package -DskipTests

# Use a lighter JDK image for runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/tracing-app.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

