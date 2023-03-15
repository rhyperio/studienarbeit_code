FROM maven:3.8.7-openjdk-18 AS build

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package -DskipTests --quiet

FROM openjdk:17-alpine

COPY --from=build /home/app/target/studienarbeit_code-0.0.1-SNAPSHOT-jar-with-dependencies.jar /studienarbeit.jar

CMD ["java", "-jar", "/studienarbeit.jar"]
