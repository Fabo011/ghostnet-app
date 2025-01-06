# Use a multi-stage build to reduce the final image size
FROM eclipse-temurin:22-jdk AS build-stage

# Install Maven and required utilities
RUN apt-get update && apt-get install -y wget unzip maven && rm -rf /var/lib/apt/lists/*

# Set the working directory for the build
WORKDIR /app

# Copy the Maven project files to the build stage
COPY pom.xml .
COPY src ./src

# Run Maven to build the WAR file
RUN mvn clean install

# Use a lightweight base image for the runtime
FROM eclipse-temurin:22-jdk

# Install TomEE
RUN wget https://downloads.apache.org/tomee/tomee-10.0.0/apache-tomee-10.0.0-webprofile.tar.gz && \
    tar -xvzf apache-tomee-10.0.0-webprofile.tar.gz -C /opt/ && \
    ls -l /opt/apache-tomee-webprofile-10.0.0/bin/ && \
    rm apache-tomee-10.0.0-webprofile.tar.gz

# Make catalina.sh executable
RUN chmod +x /opt/apache-tomee-webprofile-10.0.0/bin/catalina.sh

# Set the working directory to the TomEE webapps folder
WORKDIR /opt/apache-tomee-webprofile-10.0.0/webapps/

# Copy the WAR file from the build stage
COPY --from=build-stage /app/target/ghostnet-app-1.0-SNAPSHOT.war .

# Expose TomEE default port (8070)
EXPOSE 8070

# Start TomEE server
CMD ["/bin/sh", "/opt/apache-tomee-webprofile-10.0.0/bin/catalina.sh", "run"]
