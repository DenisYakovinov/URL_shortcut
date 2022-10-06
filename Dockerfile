FROM maven:3.8-openjdk-17 as maven
WORKDIR shortcut
COPY . /shortcut
RUN mvn install

FROM openjdk:17.0.2-jdk
WORKDIR shortcut
COPY --from=maven /shortcut/target/job4j_url_shortcut-0.0.1-SNAPSHOT.jar shortcut_app.jar
CMD java -jar shortcut_app.jar
