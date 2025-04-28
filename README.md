
# CyberEnte

Ein spezielles **Minecraft Paper-Plugin** für den privaten CyberEnte-Server.

## Funktionen dieses Plugins
siehe [Funkionen](TODO.md)

## Autoren

- [@CyberEnte](https://www.github.com/cyberente)
- [@KeksGauner](https://www.github.com/keksgauner)

## Kompilieren aus dem Quellcode

Um den CyberEnte Plugin zu kompilieren, benötigen Sie mindestans JDK 21 und eine Internetverbindung. \
Dann klonen Sie dieses Repository und führen `./gradlew build` innerhalb des geklonten Projekts aus. \
Standardmäßig befinden sich die kompilierten Jars in `build/libs` und heißen `<pluginname>-<version>.jar`. \
Wenn es Abhängigkeiten hat dann heißt es `<pluginname>-<version>-shadow.jar`.

## Überprüfen und Formatieren des Codes

Um zu überprüfen, dass Ihr Code fehlerfrei ist, führen Sie `./gradlew spotlessCheck` aus.\
Um Ihren Code zu formatieren, führen Sie `./gradlew spotlessApply` aus.
