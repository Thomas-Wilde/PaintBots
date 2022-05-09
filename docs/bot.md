Paint Bots - Ein eigener Bot
===

Die Hauptaufgabe bei *PaintBots* ist es, einen eigenen Bot zu programmieren --
einen **PaintBot**. Das Spiel bietet dafür eine Schnittstelle in Form einer
[API] an. Lade Dir am einfachsten hier die [aktuelle] Version der
vorkompilierten `jar-Datei` herunter oder clone das [git-Repository]. Starte das
Spiel einmal, wie im [Intro] beschrieben!

[API]: https://de.wikipedia.org/wiki/Programmierschnittstelle
[aktuelle]: https://thomas-wilde.github.io/PaintBots/release/
[git-Repository]: https://github.com/Thomas-Wilde/PaintBots
[Intro]: https://github.com/Thomas-Wilde/PaintBots/docs/intro.md


## Die Bot Klasse

Als Anlage zu dieser Aufgabe findest Du eine Template-Datei, in der das
Grundgrüst für Deinen Bot steht.

1. Deine Datei **muss** zum `package bots;` gehören.
2. Ändere den Klassennamen von `RenameMeBot` so, dass der Klassenname zu Deinem
   Bot passt -- `RenameMeBot` und `PaintBot` sind verboten[^RenameBot].
3. Schreibe **NICHT** `public` vor die Klasse -- die Sichtbarkeit **muss**
   `package-private` bleiben. Außerdem würde der Bot dann sowieso nicht
   kompilieren.
4. Überschreibe die Methoden:
   - `public String getBotName() { return "RenameMeBot"; }` die den Namen
     Deines Bots zurück gibt.
   - `public String getStudent() { return "Your full name here!"; }`
     die Deinen Vor- und Nachnamen zurück gibt.
   - `public int getMatrikel()  { return 123456; }` die Deine
     Matrikelnummer zurück gibt.
   - `public Vector2 getDirection() { return dir; }` die die aktuelle
     Richtung zurück gibt, in die Dein Bot im nächsten Schritt laufen soll.
5. Übernimm die beiden Konstruktoren aus der Template-Datei und passe sie an den
   Klassennamen an (Standard-Konstruktor und Konstruktor mit genau einem
   `String` als Parameter).
6. Implementiere die `initBot()` Methode, wenn Du irgendwas initialisieren
   musst.
7. Berechne in der `void update(GameManager.SecretKey secret)` Methode in welche
   Richtung Dein Bot laufen soll. Rufe dazu am besten weitere Hilfsmethoden, wie
   `myUpdate()`, auf!

[^RenameBot]: Wer eine Klasse einreicht, die `RenameMeBot` oder `PaintBot` heißt, bekommt keine Zulassung zur Klausur, weil er/sie keine Phantasie für einen eigenen Namen hat.


## Wie kann ich den Bot kompilieren?

Beim Starten des Spiels wird ein Unterordner `bots` angelegt. Am einfachsten
kopierst Du die Datei `PaintBot.java` in diesen Unterordner. Dann öffnest Du ein
Terminal und navigierst zu dem Ordner, in dem sich die `paintbots_0_xx.xx.jar`
und der Unterordner befinden. Jetzt kannst Du den Bot mit folgendem Befehl von
der Kommandozeile aus kompilieren:

```
javac -cp paintbots_0_xx.xx.jar ./bots/PaintBot.java
```

Dabei musst Du `paintbots_0_xx.xx.jar` durch den richtigen Dateinamen deiner
PaintBots-Version ersetzen. Im Unterordner `bots` sollten jetzt die
`class`-Dateien erstellt werden, die zu Deinem Bot gehören.


## Wie bekomme ich den Bot ins Spiel?

Dein Bot sollte jetzt beim Starten des Spiels automatisch erkannt werden. Du
kannst ihn anschließend im Menü auswählen. Falls Du den Bot irgendwie anders
kompilierst, z.B. in einer IDE, dann musst Du alle `class`-Dateien, die zu
Deinem Bot gehören in den Unterordner `bots` kopieren.
