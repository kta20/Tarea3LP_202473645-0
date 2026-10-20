# Tarea 3 - Lenguajes de Programación

## Nombre -ROL

Catalina Fernández 
202473645-0

---

### Entidades

Contiene las clases principales que representan los elementos del combate.

- `Javaling.java`
- `Equipo.java`

### MecanicasCombate

Contiene las clases relacionadas con ataques y tipos elementales.

- `Ataque.java`
- `AtaqueFisico.java`
- `AtaqueEspecial.java`
- `AtaqueEstado.java`
- `TipoElemental.java`
- `Vulnerable.java`

### Simulador

Contiene las clases encargadas de gestionar los combates y el torneo.

- `JuezCombate.java`
- `GestorTorneo.java`

Además, en la raíz del proyecto se encuentran:

- `Main.java`
- `makefile`
- `movimientos.json`
- `equipos.json`

---


##  Libreria utilizada

Para realizar la lectura y procesamiento de los archivos JSON se utilizó la librería **Gson**.

Se utilizó Gson porque simplifica la lectura de archivos JSON en Java, permitiendo acceder fácilmente a los datos de movimientos, equipos y Javalings sin implementar un lector JSON manual.

En el entorno utilizado durante el desarrollo, Gson se encuentra en:

```text
/usr/share/java/gson.jar

---

##  Compilacion

```bash
make clean
make
make Javaling

---

## Archivos JSON utilizados

Para probar el funcionamiento del programa se utilizaron los archivos
`equipos.json` y `movimientos.json`.

El archivo `equipos.json` fue construido tomando como referencia la
estructura mostrada en el enunciado de la tarea. Sin embargo, fue
completado con los requerimientos solicitados, incluyendo una cantidad
de equipos válida para el torneo, 3 Javalings por equipo y 3 movimientos
por cada Javaling.

De la misma forma, el archivo `movimientos.json` fue creado siguiendo la
estructura indicada en el enunciado. Se agregaron los movimientos
necesarios para que todos los nombres utilizados en `equipos.json`
existieran previamente en el catálogo de movimientos.

Ambos archivos mantienen el formato solicitado por la tarea y fueron
utilizados para realizar las pruebas del simulador.


