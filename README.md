
# CyberEnte

Ein Minecraft Paper Plugin für den Privaten CyberEnte Server


## Autoren

- [@cyberente](https://www.github.com/cyberente)

## Kompilieren aus dem Quellcode

Um die EnderNation Plugins zu kompilieren, benötigen Sie JDK 21 und eine Internetverbindung. \
Dann klonen Sie dieses Repository und führen `./gradlew` oder `./gradlew build` innerhalb des geklonten Projekts aus. \
Standardmäßig befinden sich die kompilierten Jars in `build/libs` und heißen `<pluginname>-<version>.jar`. \
Wenn es Abhängigkeiten hat dann heißt es `<pluginname>-<version>-shadow.jar`.

## Überprüfen und Formatieren des Codes

Um zu überprüfen, dass Ihr Code fehlerfrei ist, führen Sie `./gradlew spotlessCheck` aus.\
Um Ihren Code zu formatieren, führen Sie `./gradlew spotlessApply` aus.
