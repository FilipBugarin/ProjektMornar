# Use the official OpenJDK image as the base image
FROM openjdk:17

# Create a group and user
RUN groupadd -r vi_group && useradd -r -g vi_group vi_user

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the Jenkins workspace into the container
COPY target/*.jar /app/app.jar

# Change the ownership of the /app directory
RUN chown -R vi_user:vi_group /app

# Use the non-root user to run your application
USER vi_user

# Expose the port the app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
