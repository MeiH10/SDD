# Pucknotes

## Spring Boot Server

Before starting the server, you must:

1. Clone this repository. (`git clone https://github.com/MeiH10/SDD.git`)
2. Rename `application.example.properties` to `application.properties`.
3. Inside `application.properties`, replace `spring.data.mongodb.uri` with your corresponding MongoDB URI.

Then, to load the Spring Boot Server, just run:

```sh
cd server
./mvnw spring-boot:run
```

Or, if you have [`task`](https://taskfile.dev/), just run `task server` to boot up the task server.
