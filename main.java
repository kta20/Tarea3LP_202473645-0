import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Entidades.Equipo;
import Entidades.Javaling;

import MecanicasCombate.Ataque;
import MecanicasCombate.AtaqueFisico;
import MecanicasCombate.AtaqueEspecial;
import MecanicasCombate.AtaqueEstado;
import MecanicasCombate.TipoElemental;

import Simulador.GestorTorneo;

public class Main {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println(
                "Uso: java Main <movimientos.json> <equipos.json>"
            );
            return;
        }

        String rutaMovimientos = args[0];
        String rutaEquipos = args[1];

        try {

            // =====================================
            // LEER MOVIMIENTOS.JSON
            // =====================================

            FileReader archivoMovimientos =
                new FileReader(rutaMovimientos);

            JsonArray movimientos =
                JsonParser.parseReader(archivoMovimientos)
                          .getAsJsonArray();

            HashMap<String, Ataque> catalogo =
                new HashMap<>();

            for (int i = 0; i < movimientos.size(); i++) {

                JsonObject movimiento =
                    movimientos.get(i).getAsJsonObject();

                String nombre =
                    movimiento.get("nombre").getAsString();

                String categoria =
                    movimiento.get("categoria").getAsString();

                if (categoria.equals("Físico")) {

                    String elemento =
                        movimiento.get("elemento").getAsString();

                    int potencia =
                        movimiento.get("potencia").getAsInt();

                    TipoElemental tipo =
                        convertirTipo(elemento);

                    Ataque ataque =
                        new AtaqueFisico(
                            nombre,
                            tipo,
                            potencia
                        );

                    catalogo.put(nombre, ataque);

                } else if (categoria.equals("Especial")) {

                    String elemento =
                        movimiento.get("elemento").getAsString();

                    int potencia =
                        movimiento.get("potencia").getAsInt();

                    TipoElemental tipo =
                        convertirTipo(elemento);

                    Ataque ataque =
                        new AtaqueEspecial(
                            nombre,
                            tipo,
                            potencia
                        );

                    catalogo.put(nombre, ataque);

                } else if (categoria.equals("Estado")) {

                    String statAfectado =
                        movimiento.get("stat_afectado")
                                  .getAsString();

                    double multiplicador =
                        movimiento.get("multiplicador")
                                  .getAsDouble();

                    Ataque ataque =
                        new AtaqueEstado(
                            nombre,
                            null,
                            statAfectado,
                            multiplicador
                        );

                    catalogo.put(nombre, ataque);
                }
            }

            archivoMovimientos.close();

            System.out.println(
                "Cantidad de movimientos leídos: " +
                movimientos.size()
            );

            System.out.println(
                "Ataques cargados en catálogo: " +
                catalogo.size()
            );


            // =====================================
            // LEER EQUIPOS.JSON
            // =====================================

            FileReader archivoEquipos =
                new FileReader(rutaEquipos);

            JsonArray equiposJson =
                JsonParser.parseReader(archivoEquipos)
                          .getAsJsonArray();

            Equipo[] equipos =
                new Equipo[equiposJson.size()];


            // =====================================
            // CREAR EQUIPOS
            // =====================================

            for (int i = 0; i < equiposJson.size(); i++) {

                JsonObject equipoJson =
                    equiposJson.get(i)
                               .getAsJsonObject();

                String nombreEquipo =
                    equipoJson.get("nombre_equipo")
                              .getAsString();

                JsonArray javalingsJson =
                    equipoJson.get("javalings")
                              .getAsJsonArray();

                Javaling[] integrantes =
                    new Javaling[javalingsJson.size()];


                // =================================
                // CREAR JAVALINGS
                // =================================

                for (int j = 0; j < javalingsJson.size(); j++) {

                    JsonObject javalingJson =
                        javalingsJson.get(j)
                                    .getAsJsonObject();

                    String nombre =
                        javalingJson.get("nombre")
                                    .getAsString();

                    String tipoString =
                        javalingJson.get("tipo")
                                    .getAsString();

                    TipoElemental tipo =
                        convertirTipo(tipoString);


                    // Estadísticas

                    JsonObject stats =
                        javalingJson.get("stats")
                                    .getAsJsonObject();

                    int hp =
                        stats.get("hp").getAsInt();

                    int ataque =
                        stats.get("ataque").getAsInt();

                    int defensa =
                        stats.get("defensa").getAsInt();

                    int ataqueEspecial =
                        stats.get("ataque_especial")
                             .getAsInt();

                    int defensaEspecial =
                        stats.get("defensa_especial")
                             .getAsInt();

                    int velocidad =
                        stats.get("velocidad")
                             .getAsInt();


                    // =================================
                    // MOVIMIENTOS DEL JAVALING
                    // =================================

                    JsonArray movimientosJson =
                        javalingJson.get("movimientos")
                                    .getAsJsonArray();

                    Ataque[] movimientosJavaling =
                        new Ataque[movimientosJson.size()];

                    for (
                        int k = 0;
                        k < movimientosJson.size();
                        k++
                    ) {

                        String nombreMovimiento =
                            movimientosJson.get(k)
                                           .getAsString();

                        Ataque ataqueCatalogo =
                            catalogo.get(
                                nombreMovimiento
                            );

                        if (
                            ataqueCatalogo
                            instanceof AtaqueEstado
                        ) {

                            AtaqueEstado ataqueEstado =
                                (AtaqueEstado)
                                ataqueCatalogo;

                            movimientosJavaling[k] =
                                ataqueEstado.copiar();

                        } else {

                            movimientosJavaling[k] =
                                ataqueCatalogo;
                        }
                    }


                    // Crear Javaling

                    integrantes[j] =
                        new Javaling(
                            nombre,
                            tipo,
                            hp,
                            ataque,
                            defensa,
                            ataqueEspecial,
                            defensaEspecial,
                            velocidad,
                            movimientosJavaling
                        );
                }


                // Crear Equipo

                equipos[i] =
                    new Equipo(
                        nombreEquipo,
                        integrantes
                    );
            }

            archivoEquipos.close();


            // =====================================
            // MOSTRAR EQUIPOS CARGADOS
            // =====================================

            System.out.println(
                "Cantidad de equipos: " +
                equipos.length
            );

            System.out.println(
                "Equipos cargados:"
            );

            for (int i = 0; i < equipos.length; i++) {

                System.out.println(
                    "- " +
                    equipos[i].getNombre()
                );
            }


            // =====================================
            // INICIAR TORNEO
            // =====================================

            if (equipos.length >= 2) {

                GestorTorneo torneo =
                    new GestorTorneo();

                Equipo campeon =
                    torneo.iniciarTorneo(
                        equipos
                    );


                // =================================
                // GUARDAR RESULTADO DEL TORNEO
                // =================================

                Files.writeString(
                    Path.of(
                        "resultados_torneo.txt"
                    ),
                    torneo.getRegistroTorneo()
                );

                System.out.println(
                    "Resultados guardados en resultados_torneo.txt"
                );

            } else {

                System.out.println(
                    "Se necesitan al menos 2 equipos para iniciar el torneo."
                );
            }


        } catch (IOException e) {

            System.out.println(
                "Error al leer o escribir archivos."
            );

            System.out.println(
                e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                "Error en el formato de los JSON."
            );

            System.out.println(
                e.getMessage()
            );
        }
    }


    // =============================================
    // CONVERTIR STRING A TipoElemental
    // =============================================

    private static TipoElemental convertirTipo(
        String tipo
    ) {

        if (tipo.equals("Fuego")) {

            return TipoElemental.FUEGO;

        } else if (tipo.equals("Agua")) {

            return TipoElemental.AGUA;

        } else if (tipo.equals("Planta")) {

            return TipoElemental.PLANTA;

        } else if (tipo.equals("Tierra")) {

            return TipoElemental.TIERRA;

        } else if (tipo.equals("Eléctrico")) {

            return TipoElemental.ELECTRICO;

        } else if (tipo.equals("Volador")) {

            return TipoElemental.VOLADOR;
        }

        return null;
    }
}