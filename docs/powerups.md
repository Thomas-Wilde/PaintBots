PaintBots - PowerUps
===

Im Spiel werden PowerUps generiert. Die PowerUps...

- tauchen zu zufälligen Zeiten an zufälligen Positionen auf,
- haben eine zufällige Lebensdauer, während der sie gesammelt werden können,
- haben einen zufälligen Effekt.

Es werden folgende PowerUps unterschieden:

1. Der *flinke Schuh* kann eingesammelt werden und erhöht die Geschwindigkeit
   dauerhaft um 15%.
2. Das *Radius PowerUp* (das ist kein Fadenkreuz) kann ebenfalls eingesammelt
   werden und erhöht den Malradius um 20%. Ein größerer Malradius verbraucht
   entsprechend mehr Farbe.
3. Der *Rucksack* kann eingesammelt werden und erhöht die Menge der Farbe, die
   getragen werden kann um 20%.
4. Die *Sanduhr* kann eingesammelt werden und verringert die Zeit, die zum
   Auffüllen der Farbe benötigt wird um 50%.
5. Die *Farbflasche* wird beim Drüberlaufen verbraucht und füllt sofort die gesamte
   Farbe auf.
6. Die *Farbexplosion 1* wird beim Drüberlaufen verbraucht und markiert einen
   großen Kreis um die aktuelle Position mit der eigenen Farbe. Dabei wird keine
   Farbe aus dem eigenen Vorrat verbraucht.
7. Die *Farbexplosion 2* wird beim Drüberlaufen verbraucht und verteilt 20
   kleine Farbkreise auf dem gesamten Spielfeld. Dabei wird keine Farbe aus dem
   eigenen Vorrat verbraucht.

![PaintBotsGame](/figures/PaintBots_powerups.png)

Jeder Spieler kann biz zu 2 permanente PowerUps (1.-4.) einsammeln, um deren
Effekt dauerhaft zu bekommen. Wenn mehrfach das gleiche PowerUp eingesammelt
wird, dann werden die Effekte *multipliziert* (nicht addiert) -- zum Beispiel
erhöht das Radius PowerUp den Malradius auf den Wert `1.2*1.2=1.44`. Es gibt
*keine* Möglichkeit einmal eingesammelte PowerUps wieder abzulegen.

![PaintBotsGame](/figures/PaintBots_powerups_collected.png){:width="10%"}



{:comment
%%% Local Variables:
%%% ispell-local-dictionary: "de"
%%% coding: utf-8
%%% End:
}
