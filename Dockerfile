# Build Frontend
FROM node:18 AS frontend-build
WORKDIR /app/frontend
COPY csrm-frontend/package*.json ./
RUN npm install
COPY csrm-frontend/ ./
RUN npm run build

# Build Backend
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app/backend
COPY csrm-backend/pom.xml ./
COPY csrm-backend/src ./src
# Copy frontend build to backend static folder
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
# Build the backend jar
RUN mvn clean package -DskipTests

# Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend-build /app/backend/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
