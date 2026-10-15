package Simulador;

import Entidades.Equipo;
import Entidades.Javaling;
import MecanicasCombate.Ataque;
import MecanicasCombate.AtaqueEstado;

public class JuezCombate {

    private String ultimoRegistro;
    private int numeroEnfrentamiento = 1;

    public Equipo simularMatch(
        Equipo local,
        Equipo visitante
    ) {

        // Cada combate comienza desde cero
        local.reiniciarEquipo();
        visitante.reiniciarEquipo();

        RegistroTurno registro =
            new RegistroTurno();

        int enfrentamientoActual =
            numeroEnfrentamiento;

        numeroEnfrentamiento++;

        registro.escribir(
            "[MATCH_INICIO] Enfrentamiento " +
            enfrentamientoActual +
            ": " +
            local.getNombre() +
            " vs " +
            visitante.getNombre()
        );

        int turno = 1;

        while (true) {

            registro.escribir(
                "Turno " + turno + ":"
            );

            // En el primer turno entran
            // los Javaling iniciales
            if (turno == 1) {

                registro.registrarEntrada(
                    local
                );

                registro.registrarEntrada(
                    visitante
                );
            }

            Javaling localActivo =
                local.getJavalingActivo();

            Javaling visitanteActivo =
                visitante.getJavalingActivo();


            // Si empatan en velocidad,
            // el equipo local actúa primero
            if (
                localActivo.getVelocidad()
                >=
                visitanteActivo.getVelocidad()
            ) {

                boolean visitanteSigue =
                    realizarAccion(
                        local,
                        visitante,
                        registro
                    );

                if (!visitanteSigue) {

                    registro.registrarFinMatch(
                        local,
                        visitante,
                        local
                    );

                    ultimoRegistro =
                        registro.getLog();

                    return local;
                }


                boolean localSigue =
                    realizarAccion(
                        visitante,
                        local,
                        registro
                    );

                if (!localSigue) {

                    registro.registrarFinMatch(
                        local,
                        visitante,
                        visitante
                    );

                    ultimoRegistro =
                        registro.getLog();

                    return visitante;
                }

            } else {

                boolean localSigue =
                    realizarAccion(
                        visitante,
                        local,
                        registro
                    );

                if (!localSigue) {

                    registro.registrarFinMatch(
                        local,
                        visitante,
                        visitante
                    );

                    ultimoRegistro =
                        registro.getLog();

                    return visitante;
                }


                boolean visitanteSigue =
                    realizarAccion(
                        local,
                        visitante,
                        registro
                    );

                if (!visitanteSigue) {

                    registro.registrarFinMatch(
                        local,
                        visitante,
                        local
                    );

                    ultimoRegistro =
                        registro.getLog();

                    return local;
                }
            }

            turno++;
        }
    }


    public String getUltimoRegistro() {
        return ultimoRegistro;
    }


    /*
     * Decide la acción del Javaling.
     *
     * 1. Ataque que haga más del 50%.
     * 2. Cambio a un compañero conveniente.
     * 3. Ataque de Estado disponible.
     * 4. Ataque de mayor daño.
     */
    private boolean realizarAccion(
        Equipo equipoAtacante,
        Equipo equipoDefensor,
        RegistroTurno registro
    ) {

        Javaling atacante =
            equipoAtacante
                .getJavalingActivo();

        Javaling defensor =
            equipoDefensor
                .getJavalingActivo();


        // 1. Ataque que supere el 50%

        Ataque ataqueFuerte =
            buscarAtaqueMayor50(
                atacante,
                defensor
            );

        if (ataqueFuerte != null) {

            return ejecutarAtaque(
                equipoAtacante,
                equipoDefensor,
                ataqueFuerte,
                registro
            );
        }


        // 2. Buscar un cambio

        int indiceCambio =
            buscarCambio(
                equipoAtacante,
                defensor
            );

        if (indiceCambio != -1) {

            Javaling anterior =
                equipoAtacante
                    .getJavalingActivo();

            anterior.reiniciarEstadisticas();

            equipoAtacante.hacerCambio(
                indiceCambio
            );

            Javaling nuevo =
                equipoAtacante
                    .getJavalingActivo();

            registro.registrarCambio(
                equipoAtacante,
                anterior,
                nuevo
            );

            // El cambio consume la acción
            return true;
        }


        // 3. Ataque de Estado

        AtaqueEstado ataqueEstado =
            buscarAtaqueEstado(
                atacante
            );

        if (ataqueEstado != null) {

            ataqueEstado.ejecutar(
                atacante,
                defensor
            );

            registro.registrarEstado(
                atacante,
                ataqueEstado
            );

            return true;
        }


        // 4. Ataque de mayor daño

        Ataque mejorAtaque =
            obtenerMejorAtaque(
                atacante,
                defensor
            );

        if (mejorAtaque != null) {

            return ejecutarAtaque(
                equipoAtacante,
                equipoDefensor,
                mejorAtaque,
                registro
            );
        }

        return true;
    }


    /*
     * Ejecuta un ataque normal.
     */
    private boolean ejecutarAtaque(
        Equipo equipoAtacante,
        Equipo equipoDefensor,
        Ataque ataque,
        RegistroTurno registro
    ) {

        Javaling atacante =
            equipoAtacante
                .getJavalingActivo();

        Javaling defensor =
            equipoDefensor
                .getJavalingActivo();

        int danio =
            ataque.ejecutar(
                atacante,
                defensor
            );

        defensor.recibirDanio(
            danio
        );

        registro.registrarAtaque(
            atacante,
            defensor,
            ataque,
            danio
        );


        if (
            defensor.getHpActual() == 0
        ) {

            registro.registrarKO(
                atacante,
                defensor
            );

            defensor.reiniciarEstadisticas();

            boolean quedanVivos =
                equipoDefensor
                    .pasarAlSigVivo();

            if (quedanVivos) {

                registro.registrarEntrada(
                    equipoDefensor
                );

            } else {

                return false;
            }
        }

        return true;
    }


    /*
     * Busca un ataque que haga más
     * del 50% del HP actual rival.
     */
    private Ataque buscarAtaqueMayor50(
        Javaling atacante,
        Javaling defensor
    ) {

        Ataque[] movimientos =
            atacante.getMovimientos();

        Ataque mejorAtaque = null;

        int mayorDanio = -1;

        double mitadVida =
            defensor.getHpActual()
            * 0.5;


        for (
            int i = 0;
            i < movimientos.length;
            i++
        ) {

            if (
                !(movimientos[i]
                instanceof AtaqueEstado)
            ) {

                int danioActual =
                    movimientos[i]
                        .ejecutar(
                            atacante,
                            defensor
                        );

                if (
                    danioActual > mitadVida &&
                    danioActual > mayorDanio
                ) {

                    mayorDanio =
                        danioActual;

                    mejorAtaque =
                        movimientos[i];
                }
            }
        }

        return mejorAtaque;
    }


    /*
     * Busca un compañero vivo cuyo
     * mejor ataque supere el 50%.
     */
    private int buscarCambio(
        Equipo equipo,
        Javaling defensor
    ) {

        Javaling[] integrantes =
            equipo.getIntegrantes();

        int indiceActual =
            equipo.getIndiceActivo();

        int mejorIndice = -1;

        int mayorDanio = -1;

        double mitadVida =
            defensor.getHpActual()
            * 0.5;


        for (
            int i = 0;
            i < integrantes.length;
            i++
        ) {

            if (
                i != indiceActual &&
                integrantes[i]
                    .getHpActual() > 0
            ) {

                int mejorDanio =
                    calcularMejorDanio(
                        integrantes[i],
                        defensor
                    );

                if (
                    mejorDanio > mitadVida &&
                    mejorDanio > mayorDanio
                ) {

                    mayorDanio =
                        mejorDanio;

                    mejorIndice =
                        i;
                }
            }
        }

        return mejorIndice;
    }


    /*
     * Busca un movimiento de Estado
     * todavía no utilizado.
     */
    private AtaqueEstado buscarAtaqueEstado(
        Javaling atacante
    ) {

        Ataque[] movimientos =
            atacante.getMovimientos();

        for (
            int i = 0;
            i < movimientos.length;
            i++
        ) {

            if (
                movimientos[i]
                instanceof AtaqueEstado
            ) {

                AtaqueEstado ataqueEstado =
                    (AtaqueEstado)
                    movimientos[i];

                if (
                    !ataqueEstado
                        .estaUsado()
                ) {

                    return ataqueEstado;
                }
            }
        }

        return null;
    }


    /*
     * Obtiene el ataque normal
     * de mayor daño.
     */
    private Ataque obtenerMejorAtaque(
        Javaling atacante,
        Javaling defensor
    ) {

        Ataque[] movimientos =
            atacante.getMovimientos();

        Ataque mejorAtaque = null;

        int mayorDanio = -1;


        for (
            int i = 0;
            i < movimientos.length;
            i++
        ) {

            if (
                !(movimientos[i]
                instanceof AtaqueEstado)
            ) {

                int danioActual =
                    movimientos[i]
                        .ejecutar(
                            atacante,
                            defensor
                        );

                if (
                    danioActual >
                    mayorDanio
                ) {

                    mayorDanio =
                        danioActual;

                    mejorAtaque =
                        movimientos[i];
                }
            }
        }

        return mejorAtaque;
    }


    /*
     * Calcula el mejor daño posible
     * de un Javaling.
     */
    private int calcularMejorDanio(
        Javaling atacante,
        Javaling defensor
    ) {

        Ataque[] movimientos =
            atacante.getMovimientos();

        int mayorDanio = 0;


        for (
            int i = 0;
            i < movimientos.length;
            i++
        ) {

            if (
                !(movimientos[i]
                instanceof AtaqueEstado)
            ) {

                int danio =
                    movimientos[i]
                        .ejecutar(
                            atacante,
                            defensor
                        );

                if (
                    danio > mayorDanio
                ) {

                    mayorDanio =
                        danio;
                }
            }
        }

        return mayorDanio;
    }


    /*
     * Clase interna encargada
     * de registrar el combate.
     */
    private class RegistroTurno {

        private StringBuilder log =
            new StringBuilder();


        public void escribir(
            String mensaje
        ) {

            System.out.println(
                mensaje
            );

            log.append(
                mensaje
            ).append(
                "\n"
            );
        }


        public String getLog() {

            return log.toString();
        }


        public void registrarEntrada(
            Equipo equipo
        ) {

            Javaling activo =
                equipo
                    .getJavalingActivo();

            escribir(
                "[ENTRA] " +
                equipo.getNombre() +
                " envía a " +
                activo.nombre +
                "."
            );
        }


        public void registrarCambio(
            Equipo equipo,
            Javaling anterior,
            Javaling nuevo
        ) {

            escribir(
                "[CAMBIO] " +
                equipo.getNombre() +
                " retira a " +
                anterior.nombre +
                " y envía a " +
                nuevo.nombre+"."
            );
        }


        public void registrarEstado(
            Javaling atacante,
            AtaqueEstado ataque
        ) {

            String stat =
                ataque.getStatAfectado();

            double multiplicador =
                ataque.getMultiplicador();

            int valorActual =
                atacante
                    .getEstadisticaActual(
                        stat
                    );

            String nombreStat =
                convertirNombreEstadistica(
                    stat
                );

            escribir(
                "[ESTADO] " +
                atacante.nombre +
                " usa " +
                ataque.getNombre() +
                " (estado) | " +
                nombreStat.toLowerCase() +
                " de " +
                atacante.nombre +
                " modificado por x" +
                multiplicador +
                ". " +
                nombreStat +
                " actual: " +
                valorActual +
                "."
            );
        }


        public void registrarAtaque(
            Javaling atacante,
            Javaling defensor,
            Ataque ataque,
            int danio
        ) {

            escribir(
                "[ATAQUE] " +
                atacante.nombre +
                " ataca a " +
                defensor.nombre +
                " con " +
                ataque.getNombre() +
                " | Daño: " +
                danio +
                " | Vida restante de " +
                defensor.nombre +
                ": " +
                defensor.getHpActual()
            );
        }


        public void registrarKO(
            Javaling atacante,
            Javaling defensor
        ) {

            escribir(
                "[KO] " +
                atacante.nombre +
                " ha derrotado a " +
                defensor.nombre +
                "."
            );
        }


        public void registrarFinMatch(
            Equipo local,
            Equipo visitante,
            Equipo ganador
        ) {

            escribir(
                "[MATCH_FIN] Ganador: " +
                ganador.getNombre() +
                " | Marcador final: " +
                local.contarVivos() +
                " - " +
                visitante.contarVivos()
            );
        }


        /*
         * Convierte el nombre utilizado
         * internamente a uno más legible.
         */
        private String convertirNombreEstadistica(
            String stat
        ) {

            if (
                stat.equals("ataque")
            ) {

                return "Ataque";

            } else if (
                stat.equals("defensa")
            ) {

                return "Defensa";

            } else if (
                stat.equals(
                    "ataque_especial"
                )
            ) {

                return "Ataque especial";

            } else if (
                stat.equals(
                    "defensa_especial"
                )
            ) {

                return "Defensa especial";

            } else if (
                stat.equals("velocidad")
            ) {

                return "Velocidad";
            }

            return stat;
        }
    }
}