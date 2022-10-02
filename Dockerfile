FROM openjdk
WORKDIR shortcut
ADD target/job4j_url_shortcut-0.0.1-SNAPSHOT.jar shortcut_app.jar
ENTRYPOINT java -jar shortcut_app.jar
