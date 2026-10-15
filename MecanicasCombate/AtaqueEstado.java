package MecanicasCombate;

import Entidades.Javaling;

public class AtaqueEstado extends Ataque {

    private String statAfectado;
    private double multiplicador;
    private boolean fueUsado;

    public AtaqueEstado(
        String nombre,
        TipoElemental tipo,
        String statAfectado,
        double multiplicador
    ) {
        super(nombre, tipo);

        this.statAfectado = statAfectado;
        this.multiplicador = multiplicador;
        this.fueUsado = false;
    }

    @Override
    public int ejecutar(
        Javaling atacante,
        Javaling defensor
    ) {

        if (fueUsado) {
            return 0;
        }

        aplicarCambio(atacante);

        fueUsado = true;

        return 0;
    }

    public void aplicarCambio(
        Javaling atacante
    ) {

        if (statAfectado.equals("ataque")) {

            atacante.modifAtaque(
                multiplicador
            );

        } else if (statAfectado.equals("defensa")) {

            atacante.modifDefensa(
                multiplicador
            );

        } else if (
            statAfectado.equals("ataque_especial")
        ) {

            atacante.modifAtaqueEspecial(
                multiplicador
            );

        } else if (
            statAfectado.equals("defensa_especial")
        ) {

            atacante.modifDefensaEspecial(
                multiplicador
            );

        } else if (
            statAfectado.equals("velocidad")
        ) {

            atacante.modifVelocidad(
                multiplicador
            );
        }
    }

    public boolean estaUsado() {
        return fueUsado;
    }

    public void reiniciarUso() {
        fueUsado = false;
    }

    public String getStatAfectado() {
        return statAfectado;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    public AtaqueEstado copiar() {

        return new AtaqueEstado(
            nombre,
            tipo,
            statAfectado,
            multiplicador
        );
    }
}