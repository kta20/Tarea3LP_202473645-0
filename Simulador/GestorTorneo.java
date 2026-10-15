package Simulador;

import Entidades.Equipo;

public class GestorTorneo {

    private JuezCombate juez;
    private StringBuilder registroTorneo;

    public GestorTorneo() {
        this.juez = new JuezCombate();
        this.registroTorneo = new StringBuilder();
    }

    public Equipo iniciarTorneo(Equipo[] equipos) {

        Equipo[] rondaActual = equipos;

        while (rondaActual.length > 1) {

            Equipo[] ganadores =
                new Equipo[rondaActual.length / 2];

            int posicionGanador = 0;

            for (
                int i = 0;
                i < rondaActual.length;
                i += 2
            ) {

                Equipo equipo1 =
                    rondaActual[i];

                Equipo equipo2 =
                    rondaActual[i + 1];

                Equipo ganador =
                    juez.simularMatch(
                        equipo1,
                        equipo2
                    );

                ganadores[posicionGanador] =
                    ganador;

                posicionGanador++;

                registroTorneo.append(
                    juez.getUltimoRegistro()
                );

                registroTorneo.append("\n");
            }

            rondaActual = ganadores;
        }

        Equipo campeon =
            rondaActual[0];

        String mensajeFinal =
            "[TORNEO_FIN] Campeón del torneo: " +
            campeon.getNombre();

        System.out.println(
            mensajeFinal
        );

        registroTorneo.append(
            mensajeFinal
        ).append("\n");

        return campeon;
    }

    public String getRegistroTorneo() {
        return registroTorneo.toString();
    }
}