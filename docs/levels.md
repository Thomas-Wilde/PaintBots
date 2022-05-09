Paint Bots - Das Spielfeld & Eigene Levels
===

**PaintBots** bietet die Möglichkeit, dass Du relativ einfach eigene Levels
erstellen kannst. Die Levels werden in einer Textdatei mit der Endung `.lvl`
abgespeichert. In der Datei wird die Art des Gegenstands, dessen Position auf
dem Spielfeld und seine Größe angegeben. Jede Zeile definiert dabei einen
Gegenstand. Zeilen, die mit einem `#` beginnen, werden als Kommentar betrachtet
und ignoriert. Zeilen, die einen Syntaxfehler enthalten werden ebenfalls
ignoriert.

Eine Gegenstand wird über 5 Werte definiert: `T,x,y,sx,sy`.

- `T` ist der Typ,
- `x,y` beschreiben die Position auf dem Spielfeld,
- `sx,sy` ist eine Skalierungsfaktor.

Die Zeile `TS,500,500,0.25,0.25` definiert z.B. einen *kleinen Baum* *(**T**ree
**S**mall)* in der Mitte des Spielfelds und mit 1/4 der Originalgröße.


## Das Spielfeld

Das Spielfeld ist in einzelne Zellen unterteilt; jeweils 1000 in x und y
Richtung. Du kannst dir das Feld auch als Bild mit 1000x1000 Pixel vorstellen.
Jede Zelle auf dem Spielfeld hat genau *einen* Index. Der Index definiert die
mögliche Interaktion und wird beim Erstellen des Gegenstands automatisch
gesetzt. Dazu gehören:

- `NONE` hier wird gelaufen und gemalt,
- `OBSTACLE` hier wird nicht gelaufen aber gemalt,
- `BLOCKED` hier wird nicht gelaufen und nicht gemalt,
- `REFILL` hier können alle ihre Farbe auffüllen,
- `REFILL_GREEN` hier kann *grün* auffüllen,
- `REFILL_PURPLE` hier kann *lila* auffüllen,
- `REFILL_BLUE` hier kann *blau* auffüllen,
- `REFILL_ORANGE` hier kann *orange* auffüllen.


## Mögliche Gegenstände

In der `lvl`-Datei kannst Du folgende Kürzel für Gegenstände verwenden:

- `PB` Farbstand mit allen Farben (**P**aint**B**ooth),
- `RO` Farbe auffüllen für *orange* (**R**efill **O**range),
- `RG` Farbe auffüllen für *grün* (**R**efill **G**reen),
- `RB` Farbe auffüllen für *blau* (**R**efill **B**lue),
- `RP` Farbe auffüllen für *lila* (**R**efill **P**urple),
- `FH` horizontaler Zaun (**F**ence **H**orizontal),
- `FV` vertikaler Zaun (**F**ence **V**ertical),
- `TS` kleiner Baum (**T**ree **S**mall),
- `TM` mittlerer Baum (**T**ree **M**edium),
- `TL` großer Baum (**T**ree **L**arge),
- `BSH` ein horizontaler Stapel mit Fässern (**B**arrel **S**tack **H**orizontal).


## Startposition der Spieler

Die Startpositionen der Spieler werden über folgende Kürzel gesetzt:

- `P0` Startposition für Spieler 0,
- `P1` Startposition für Spieler 1,
- `P2` Startposition für Spieler 2,
- `P3` Startposition für Spieler 3.

Für die Startposition der Spieler definierst Du Position und Startrichtung, in
die der Spieler läuft. `P0,500,500,1.0,0.0` platziert den grünen Spieler in der
Mitte des Feldes und er läuft nach rechts.


## Wie bekomme ich das Level ins Spiel?

Wenn Du **PaintBots** startest, werden im Verzeichnis der `jar-Datei` zwei
Unterordner angelegt. Einer von beiden heißt `levels`. Du müsst jetzt nur noch
die `lvl`-Datei mit Deiner Kreation in diesen Ordner kopieren. Beim Start des
Spiels wird der `levels` Ordner auf `lvl`-Dateien geparst. Im Hauptmenü kannst
Du dann das Level auswählen, was Du spielen willst.


## Reihenfolge der Gegenstände und Spielmechanik

Die Level-Datei wird von oben nach unten abgearbeitet. Das Spielfeld besteht am
Anfang aus 1000x1000 *"leeren"* Zellen -- dort kann einfach gemalt werden. Wenn
ein Gegenstand erstellt wird, beeinflusst er, was an dieser Stelle auf dem
Spielfeld passiert, z.B. wird die Farbe aufgefüllt oder man kann da nicht lang
laufen. **Der Gegenstand, der zuerst platziert wird, bestimmt den Index der
Interaktion.** Du kannst z.B. folgende Zeilen definieren:

```
RG,500,500,0.5,0.5
RO,500,500,0.5,0.5
```

Dabei werden zwei Auffüllstationen -- grün und orange -- an dieselbe Position
gelegt. Im Spiel wirst Du auch beide Stationen an der gleiche Stelle sehen. Da
die grüne Station (`RG`) allerdings zuerst erstellt wird, bestimmt diese auch,
dass dort grün aufgefüllt werden kann. Orange (`RO`) geht weitestgehend leer
aus.
