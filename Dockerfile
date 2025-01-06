FROM eclipse-temurin:22-jdk

# Install required utilities
RUN apt-get update && apt-get install -y wget unzip maven && rm -rf /var/lib/apt/lists/*

# Install TomEE
RUN wget https://downloads.apache.org/tomee/tomee-10.0.0/apache-tomee-10.0.0-webprofile.tar.gz && \
    tar -xvzf apache-tomee-10.0.0-webprofile.tar.gz -C /opt/ && \
    ls -l /opt/apache-tomee-webprofile-10.0.0/bin/ && \
    rm apache-tomee-10.0.0-webprofile.tar.gz

# Make catalina.sh executable
RUN chmod +x /opt/apache-tomee-webprofile-10.0.0/bin/catalina.sh

# Set the working directory to the TomEE webapps folder
WORKDIR /opt/apache-tomee-webprofile-10.0.0/webapps/

# Copy WAR file into TomEE webapps directory
COPY target/ghostnet-app-1.0-SNAPSHOT.war .

# Expose TomEE default port (8080)
EXPOSE 8070

# Start TomEE server
CMD ["/bin/sh", "/opt/apache-tomee-webprofile-10.0.0/bin/catalina.sh", "run"]


