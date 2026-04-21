# 🦆 CyberEnte

[![Build](https://img.shields.io/github/actions/workflow/status/keksgauner/minecraft-cyberente/ci.yaml?branch=master&label=Build&style=for-the-badge)](https://github.com/keksgauner/minecraft-cyberente/actions)
[![Release](https://img.shields.io/github/v/release/keksgauner/minecraft-cyberente?label=Release&style=for-the-badge)](https://github.com/keksgauner/minecraft-cyberente/releases)
[![Java](https://img.shields.io/badge/Java-25+-orange?style=for-the-badge&logo=openjdk)](https://jdk.java.net/25/)
[![License](https://img.shields.io/github/license/keksgauner/minecraft-cyberente?style=for-the-badge)](https://github.com/keksgauner/minecraft-cyberente/blob/main/LICENSE)

> 🛠️ Ein maßgeschneidertes **Minecraft Paper-Plugin** für den privaten CyberEnte-Server.

---

## 📦 Funktionen

Eine vollständige Übersicht findest du in der Datei: [📄 Funktionen](TODO.md)

---

## 👥 Autoren

- 🐤 [@CyberEnte](https://www.github.com/cyberente)
- 🍪 [@KeksGauner](https://www.github.com/keksgauner)

---

## 📥 Installation

1. Lade die neueste Version von der [**Releases-Seite**](https://github.com/keksgauner/minecraft-cyberente/releases) herunter.
2. Kopiere die `CyberEnte-paper.jar` in den `plugins`-Ordner deines Paper-Servers.
3. Installiere das  [Extended-Plugin](https://github.com/keksgauner/minecraft-cyberente-extended) der Abhängigkeiten für das Paper-Plugin **CyberEnte** enthält.
4. Starte den Server neu.

---

## ⚙️ Konfiguration
Die Konfiguration befindet sich in der Datei `plugins/CyberEnte/config.yml`.
Die Standardwerte sind:

```json
{
}
```

---

Wenn eine eigene (Custom) Welt verwendet wird, muss die `bukkit.yml` entsprechend angepasst werden.
Füge am Ende der Datei folgenden Abschnitt hinzu:

```yaml
worlds:
  <name>:
    generator: CyberEnte:MOON
```

Ersetze `<name>` durch den Namen der Welt und `MOON` durch den gewünschten Generator.

---

## 🧪 Kompilieren aus dem Quellcode

**Voraussetzungen:**

- ✅ JDK 25 oder höher
- 🌐 Internetverbindung

**Schritte:**

```bash
git clone https://github.com/keksgauner/minecraft-cyberente.git
cd minecraft-cyberente
./gradlew build
```

🗃️ Das fertige Plugin befindet sich anschließend in `build/libs` und trägt den Namen:
`CyberEnte-paper.jar`

---

## 🧹 Code-Check & Formatierung

- 🔍 Code prüfen:
  `./gradlew spotlessCheck`

- 🎨 Code formatieren:
  `./gradlew spotlessApply`

---
