FROM maven
WORKDIR /app
ARG BACKEND_MONGO_URL
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
CMD mvn spring-boot:run