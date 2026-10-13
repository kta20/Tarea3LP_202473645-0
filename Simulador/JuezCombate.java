package Simulador;

import Entidades.Equipo;
import Entidades.Javaling;
import MecanicasCombate.Ataque;
import MecanicasCombate.AtaqueEstado;

public class JuezCombate {

public Equipo simularMatch(Equipo local, Equipo visitante) {

    RegistroTurno registro = new RegistroTurno();

    System.out.println(
        "[MATCH_INICIO] Enfrentamiento: " +
        local.getNombre() +
        " vs " +
        visitante.getNombre()
    );

    registro.registrarEntrada(local);
    registro.registrarEntrada(visitante);

    int turno = 1;

    while (true) {

        System.out.println("Turno " + turno + ":");

        Javaling localActivo = local.getJavalingActivo();
        Javaling visitanteActivo = visitante.getJavalingActivo();

        if (localActivo.getVelocidad() >= visitanteActivo.getVelocidad()) {

            boolean visitanteSigue =
                realizarAtaque(local, visitante, registro);

            if (!visitanteSigue) {
                registro.registrarFinMatch(local);
                return local;
            }

            boolean localSigue =
                realizarAtaque(visitante, local, registro);

            if (!localSigue) {
                registro.registrarFinMatch(visitante);
                return visitante;
            }

        } else {

            boolean localSigue= realizarAtaque(visitante, local, registro);

            if (!localSigue) {
                registro.registrarFinMatch(visitante);
                return visitante;
            }

            boolean visitanteSigue= realizarAtaque(local, visitante, registro);

            if (!visitanteSigue){
                registro.registrarFinMatch(local);
                return local;
            }
        }

        turno++;
    }
}

private boolean realizarAtaque(
    Equipo equipoAtacante,
    Equipo equipoDefensor,
    RegistroTurno registro
) {

    Javaling atacante = equipoAtacante.getJavalingActivo();
    Javaling defensor = equipoDefensor.getJavalingActivo();

    Ataque ataque = obtenerMejorAtaque(atacante, defensor);

    int danio = ataque.ejecutar(atacante, defensor);
    defensor.recibirDanio(danio);

    registro.registrarAtaque(
        atacante,
        defensor,
        ataque,
        danio
    );

    if (defensor.getHpActual() == 0) {

        registro.registrarKO(atacante, defensor);

        boolean quedanVivos =
            equipoDefensor.pasarAlSigVivo();

        if (quedanVivos) {
            registro.registrarEntrada(equipoDefensor);
        } else {
            return false;
        }
    }

    return true;
}

private class RegistroTurno {
    public void registrarFinMatch(Equipo ganador) {
    System.out.println("[MATCH_FIN] Ganador: "+ganador.getNombre());
    }
    public void registrarKO(Javaling atacante, Javaling defensor) {
    System.out.println("[KO] "+atacante.nombre+" ha derrotado a "+defensor.nombre);
    }
    public void registrarEntrada(Equipo equipo) {
        Javaling activo = equipo.getJavalingActivo();
        System.out.println("[ENTRA] "+equipo.getNombre()+" envía a "+activo.nombre);
    }

    public void registrarAtaque(
    Javaling atacante,
    Javaling defensor,
    Ataque ataque,
    int danio
    ) {
    System.out.println("[ATAQUE] "+atacante.nombre+" ataca a "+defensor.nombre+" | Daño: "+danio +" | Vida restante: "+defensor.getHpActual());
        }
    }

    private Ataque obtenerMejorAtaque(
        Javaling atacante,
        Javaling defensor
    ) {

        Ataque[] movimientos = atacante.getMovimientos();

        Ataque mejorAtaque = null;
        int mayorDanio = -1;

        for (int i = 0; i < movimientos.length; i++) {

            if (!(movimientos[i] instanceof AtaqueEstado)) {

                int danioActual =
                    movimientos[i].ejecutar(atacante, defensor);

                if (danioActual > mayorDanio) {
                    mayorDanio = danioActual;
                    mejorAtaque = movimientos[i];
                }
            }
        }

        // Caso temporal: si todos fueran ataques de estado
        if (mejorAtaque == null) {
            mejorAtaque = movimientos[0];
        }

        return mejorAtaque;
    }
}