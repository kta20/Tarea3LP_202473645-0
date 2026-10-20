package MecanicasCombate;

import Entidades.Javaling;

public class AtaqueEstado extends Ataque {

    private String statAfectado;
    private double multiplicador;
    private boolean fueUsado;

    /*
    ***
    Parametro 1: String nombre
    Parametro 2: TipoElemental tipo
    Parametro 3: String statAfectado
    Parametro 4: double multiplicador
    ***
    None
    ***
    Crea un ataque de estado indicando su nombre, tipo,
    estadística afectada y multiplicador.
    */
    public AtaqueEstado(
        String nombre,
        TipoElemental tipo,
        String statAfectado,
        double multiplicador
    ) {
        super(nombre, tipo);

        this.statAfectado= statAfectado;
        this.multiplicador= multiplicador;
        this.fueUsado= false;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    Parametro 2: Javaling defensor
    ***
    int
    ***
    Ejecuta el ataque de estado si todavía no ha sido utilizado,
    aplica el cambio al atacante y retorna 0 de daño.
    */
    @Override
    public int ejecutar(
        Javaling atacante,
        Javaling defensor
    ) {

        if(fueUsado) {
            return 0;
        }

        aplicarCambio(atacante);
        fueUsado= true;
        return 0;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    ***
    None
    ***
    Aplica el multiplicador sobre la estadística indicada
    del Javaling atacante.
    */
    public void aplicarCambio(
        Javaling atacante
    ) {

        if(statAfectado.equals("ataque")) {
            atacante.modifAtaque(multiplicador);
        } else if(statAfectado.equals("defensa")) {
            atacante.modifDefensa(multiplicador);
        } else if(statAfectado.equals("ataque_especial")){
            atacante.modifAtaqueEspecial(multiplicador);
        } else if(statAfectado.equals("defensa_especial")){
            atacante.modifDefensaEspecial(multiplicador);
        } else if(statAfectado.equals("velocidad")){
            atacante.modifVelocidad(multiplicador);
        }
    }

    public boolean estaUsado(){
        return fueUsado;
    }

    /*
    ***
    None
    ***
    None
    ***
    Reinicia el estado de uso del ataque para permitir
    que pueda ser utilizado en un nuevo combate.
    */
    public void reiniciarUso() {
        fueUsado= false;
    }

    public String getStatAfectado() {
        return statAfectado;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    /*
    ***
    None
    ***
    AtaqueEstado
    ***
    Crea y retorna una copia nueva del ataque de estado
    con los mismos datos y sin haber sido utilizada.
    */
    public AtaqueEstado copiar() {
        return new AtaqueEstado(nombre,tipo,statAfectado,multiplicador);
    }
}