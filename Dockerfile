FROM node:18-alpine AS client
WORKDIR /app
COPY ./client/package.json .
RUN npm install --verbose
COPY ./client .
EXPOSE 8080
RUN [ "npm", "run", "build" ]

# Use the official Maven image to build the application
FROM maven:3.8.7-openjdk-18 AS builder
WORKDIR /app
COPY ./server/pom.xml .
RUN mvn dependency:go-offline
COPY ./server/src ./src
COPY --from=client /app/dist ./src/main/resources/public
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:24-slim-bullseye AS runner
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]