# Stage 1: Build the application
FROM openjdk:11-jdk-slim as build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy your project files (including pom.xml) into the container
COPY . /app

# Run Maven clean install to build the application
RUN mvn clean install

# Stage 2: Deploy the WAR file in Tomcat
FROM tomcat:10-jdk11-openjdk

# Set environment variables
ENV APP_HOME=/usr/local/tomcat/webapps

# Copy the WAR file from the build step into Tomcat's webapps folder
COPY --from=build /app/target/ghostnet-app-1.0-SNAPSHOT.war ${APP_HOME}/ROOT.war

# Expose the port Tomcat runs on
EXPOSE 8070

# Start Tomcat in the foreground
CMD ["catalina.sh", "run"]



