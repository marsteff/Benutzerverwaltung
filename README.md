Benutzerverwaltung
==================

Die Benutzerwaltung ist ein in Java 8 geschrieben Anwendung.
Um Sie zu starten, muss man die Main - Methode in der Benutzerverwaltung.java ausführen.

Falls ihr plötzlich nicht mehr die Anwendung starten könnt.
Ihr müsst in der Run Config folgendes ändern
![image](https://cloud.githubusercontent.com/assets/5636969/4827965/6f2c41fc-5f7c-11e4-88b0-126696581b28.png)

Hierzu muss dieser noch entsprechende Parameter mitgegeben werden, um das gewünschte Verhalten zu bekommen !

Dies hat damit zu tun, dass diese Anwendung als 3-Schichten Applikation gebaut wurde, womit es also möglich ist die einzelnen ebenen einfach auszutauschen, ohne dass man andere Klassen anpassen müsste.

Hier mal ein Bild der TUI Abhängigkeiten durch die Implementierung des State-Pattern

![image](https://cloud.githubusercontent.com/assets/5636969/4771141/cd495abc-5b8b-11e4-9039-00e485f6c8d2.png)
