FROM maven:3.8.3-openjdk-17

WORKDIR /app

COPY pom.xml .

RUN mvn verify clean --fail-never

COPY . .

RUN mvn package

ENTRYPOINT ["java", "-jar", "target/publisher-1.0-SNAPSHOT.jar"]