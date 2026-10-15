JAVAC = javac
JAVA = java
GSON = /usr/share/java/gson.jar

SOURCES = Main.java \
          Entidades/*.java \
          MecanicasCombate/*.java \
          Simulador/*.java

all:
	$(JAVAC) -cp .:$(GSON) $(SOURCES)

Javaling:
	$(JAVA) -cp .:$(GSON) Main movimientos.json equipos.json

clean:
	find . -name "*.class" -delete