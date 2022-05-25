PaintBots - Klausurzulassung, Regeln & Wettbewerb
===

Die Programmierung des Bots ist Teil der Klausurzulassung für unsere
Vorlesung. Alle anderen Bots, die am Wettbewerb teilnehmen sollen, können per
eMail an **Thomas** ([thomas@isg.cs.ovgu.de](mailto:thomas@isg.cs.ovgu.de))
geschickt werden. Die Einreichung muss bis zum **19. Juni** erfolgen.

**Gruppenarbeit ist nicht erlaubt - Plagiate bekommen keine Klausrzulassung.**

Ein Template für den Bot findest Du im git-Repository [hier].
Wie Du den Bot aufbaust und kompilierst steht auf einer extra [Seite].

[hier]: https://github.com/Thomas-Wilde/PaintBots/blob/main/bots/PaintBot.java
[Seite]: https://github.com/Thomas-Wilde/PaintBots/blob/main/docs/bot.md


## Klausurzulassung (diese Regeln gelten auch für externe Bots)

* Du sollst genau eine KI programmieren.
* Die Programmierung muss in Java erfolgen.
* In jeder (Spiel-) Sekunde wird dein Bot 30 mal geupdated.
* Dein Bot muss das Level *Admission* in 8 von 12 Spielen gewinnen.
  * Dein Bot spielt gegen 3 *RandomBot*.
  * Die Spielzeit beträgt 120 (Spiel-) Sekunden.
  * Das Spiel wird mit 3 verschiedenen Random-Seeds wiederholt.
  * Dein Bot startet der Reihe nach auf allen 4 Startpositionen.
  * Im Spiel werden 14 zufällige PowerUps auftauchen.
* Dein Bot gewinnt, wenn er die meiste Fläche mit seiner Farbe angemalt hat.
* Dein Bot verliert[^Rechner], wenn...
  * nicht die meiste Fläche angemalt hat,
  * die Initialisierung länger als *1000ms* dauert,
  * ein Updateschritt deines Bots länger als *5ms* dauert,
  * dein Bot eine Exception wirft, die er nicht selbst behandelt.
* Das Level und die KI können im Spielmenü gewählt werden.
* Die Startseeds sind `1337`, `2674` und `4011`.
* Du kannst das Ergebnis vor dem Upload bereits zuhause bestimmten. Verwende
  dazu den Startbefehl[^replaceVersion]:

```
java -jar paintbots_0.xx.xx.jar -admission -time 120 -seed 1337 -version
```

[^replaceVersion]: ersetze `0.xx.xx` durch Deine Version des Spiels
[^Rechner]: Die Berechnung findet auf einem (sehr) leistungsfähigem Rechner
    statt.


## Regeln

Alle Bots, die für den Wettbewerb eingereicht werden, müssen folgende Regeln
einhalten. Bots, die sich nicht an die Regeln halten, werden disqualifiziert.
**Eine Disqualifikation führt zum Nichterhalt der Prüfungszulassung!**

* Die Verwendung externen Bibliotheken ist nicht erlaubt.
* Es sind keine Threads oder ähnliche Konstrukte erlaubt.
* Der Bot darf keine `Reflection`s verwenden.
* Der Bot darf keine Netzwerkverbindungen aufbauen.
* Anstößige, rechtswidrige, rassistische, beleidigende und sexistische
  Bot-Namen sind verboten.
* Dein Bot darf keine anderen Bots instanziieren.
* Plagiate sind verboten! Wenn wir erkennen, dass eingereichte Bots
  voneinander abgeschrieben sind, werden beide (!) Bots disqualifiziert und
  die Prüfungszulassung gilt als nicht erlangt.

**Entwickelt den Bot im Sinne des Spiels und schummelt nicht!**

Wenn Du nicht sicher bist, ob Du gerade schummelst oder nicht, dann schummelst
Du wahrscheinlich. Wenn Du dir immer noch nicht sicher bist schreib am besten
**Thomas** ([thomas@isg.cs.ovgu.de](mailto:thomas@isg.cs.ovgu.de)) eine eMail
und frag nach. Wenn Du einen Bug findest, der das Spiel aushebelt, schreib
Thomas eine eMail. Wir behalten uns vor, die Regeln auch während des
Wettbewerbes zu ändern -- das wird aber nur in Ausnahmefällen passieren.


## Wettbewerb

Jeder Bot, der die Klausurzulassung erreicht, kommt danach in den Wettbewerb.
Dabei lassen wir eure Bots in mehreren Spielen gegeneinander antreten. In den
einzelnen Spielen variieren wir:

- Gegner
- Spielzeit
- Levels

Der Wettbewerb findet in mehreren Runden statt. Die jeweils besten Bots kommen
in die nächste Runde. Die Gegner-Zuteilung erfolgt in den ersten Runden
randomisiert. Die besten Bots treten am Ende im Round-Robin-Prinzip
gegeneinander an. Wie genau die einzelnen Runden aussehen können wir erst
festlegen, wenn wir wissen, wieviele Bots abgegeben wurden.


## Kreativwettbewerb

Beim Kreativwettbewerb ist **alles** erlaubt... auch Schummeln!

**Wir wünschen Dir viel Spaß & Erfolg!**

{:comment
%%% Local Variables:
%%% ispell-local-dictionary: "de"
%%% coding: utf-8
%%% End:
}
