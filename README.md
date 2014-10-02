Benutzerverwaltung
==================

Die Benutzerwaltung ist ein in Java 8 geschrieben Anwendung.
Um Sie zu starten, muss man die Main - Methode in der Benutzerverwaltung.java ausführen.

Hier hat man mehrere Möglichkeiten diese zu starten.

1.
```java
new Gui(
                ConceptFactory.buildConcept(
                        ConceptMethod.STANDARD_CONCEPT,
                        PersistanceMethod.SQLITE
                )
        );
```
2.
```java
new Gui(
                ConceptFactory.buildConcept(
                        ConceptMethod.STANDARD_CONCEPT,
                        PersistanceMethod.MONGODB
                )
        );
```
3.
```java
new Tui(
                ConceptFactory.buildConcept(
                        ConceptMethod.STANDARD_CONCEPT,
                        PersistanceMethod.SQLITE
                )
        );
```
4.
```java
new Tui(
                ConceptFactory.buildConcept(
                        ConceptMethod.STANDARD_CONCEPT,
                        PersistanceMethod.MONGODB
                )
        );
```

Dies hat damit zu tun, dass diese Anwendung als 3-Schichten Applikation gebaut wurde, womit es also möglich ist die einzelnen
