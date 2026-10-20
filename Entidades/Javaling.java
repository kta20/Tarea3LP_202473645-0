package Entidades;

import MecanicasCombate.TipoElemental;
import MecanicasCombate.Vulnerable;
import MecanicasCombate.Ataque;
import MecanicasCombate.AtaqueEstado;

public class Javaling implements Vulnerable {
    public String nombre;

    private TipoElemental tipo;
    private int hpMaximo;
    private int hpActual;

    private int ataque;
    private int defensa;
    private int ataqueEspecial;
    private int defensaEspecial;
    private int velocidad;

    private int ataqueBase;
    private int defensaBase;
    private int ataqueEspecialBase;
    private int defensaEspecialBase;
    private int velocidadBase;

    private Ataque[] movimientos;

    /*
    ***
    Parametro 1: String nombre
    Parametro 2: TipoElemental tipo
    Parametro 3: int hpMaximo
    Parametro 4: int ataque
    Parametro 5: int defensa
    Parametro 6: int ataqueEspecial
    Parametro 7: int defensaEspecial
    Parametro 8: int velocidad
    Parametro 9: Ataque[] movimientos
    ***
    None
    ***
    Crea un Javaling con sus estadísticas, tipo elemental y movimientos.
    También guarda las estadísticas base para poder restaurarlas posteriormente.
    */
    public Javaling(String nombre,TipoElemental tipo,int hpMaximo,int ataque,int defensa,int ataqueEspecial,int defensaEspecial,int velocidad,Ataque[] movimientos){

        this.nombre= nombre;
        this.tipo= tipo;
        this.hpMaximo= hpMaximo;
        this.hpActual= hpMaximo;
        this.ataque= ataque;
        this.defensa= defensa;
        this.ataqueEspecial= ataqueEspecial;
        this.defensaEspecial= defensaEspecial;
        this.velocidad= velocidad;
        this.ataqueBase= ataque;
        this.defensaBase= defensa;
        this.ataqueEspecialBase= ataqueEspecial;
        this.defensaEspecialBase= defensaEspecial;
        this.velocidadBase= velocidad;
        this.movimientos= movimientos;
    }

    public int getHpActual(){
        return hpActual;
    }

    public int getDefensa(){
        return defensa;
    }

    public int getAtaque(){
        return ataque;
    }

    public int getAtaqueEspecial(){
        return ataqueEspecial;
    }

    public int getDefensaEspecial(){
        return defensaEspecial;
    }

    public int getVelocidad(){
        return velocidad;
    }

    public TipoElemental getTipo(){
        return tipo;
    }

    public Ataque[] getMovimientos(){
        return movimientos;
    }

    /*
    ***
    Parametro 1: String stat
    ***
    int
    ***
    Retorna el valor actual de la estadística indicada por el nombre recibido.
    Si la estadística no existe retorna 0.
    */
    public int getEstadisticaActual(String stat){

        if(stat.equals("ataque")){
            return ataque;
        } else if(stat.equals("defensa")){
            return defensa;
        } else if(stat.equals("ataque_especial")){
            return ataqueEspecial;
        } else if(stat.equals("defensa_especial")){
            return defensaEspecial;
        } else if(stat.equals("velocidad")){
            return velocidad;
        }

        return 0;
    }

    /*
    ***
    Parametro 1: double multiplicador
    ***
    None
    ***
    Modifica la estadística de ataque utilizando el multiplicador recibido.
    */
    public void modifAtaque(double multiplicador){
        ataque= (int)(ataque*multiplicador);
    }

    /*
    ***
    Parametro 1: double multiplicador
    ***
    None
    ***
    Modifica la estadística de defensa utilizando el multiplicador recibido.
    */
    public void modifDefensa(double multiplicador){
        defensa= (int)(defensa*multiplicador);
    }

    /*
    ***
    Parametro 1: double multiplicador
    ***
    None
    ***
    Modifica la estadística de ataque especial utilizando el multiplicador recibido.
    */
    public void modifAtaqueEspecial(double multiplicador){
        ataqueEspecial= (int)(ataqueEspecial*multiplicador);
    }

    /*
    ***
    Parametro 1: double multiplicador
    ***
    None
    ***
    Modifica la estadística de defensa especial utilizando el multiplicador recibido.
    */
    public void modifDefensaEspecial(double multiplicador){
        defensaEspecial= (int)(defensaEspecial*multiplicador);
    }

    /*
    ***
    Parametro 1: double multiplicador
    ***
    None
    ***
    Modifica la estadística de velocidad utilizando el multiplicador recibido.
    */
    public void modifVelocidad(double multiplicador){
        velocidad= (int)(velocidad*multiplicador);
    }

    /*
    ***
    Parametro 1: int danio
    ***
    None
    ***
    Resta el daño recibido al HP actual del Javaling.
    Si el HP baja de 0 se deja en 0.
    */
    public void recibirDanio(int danio){
        hpActual= hpActual-danio;

        if(hpActual< 0) {
            hpActual= 0;
        }
    }

    /*
    ***
    None
    ***
    None
    ***
    Restaura las estadísticas modificables del Javaling a sus valores originales
    sin modificar su HP actual.
    */
    public void reiniciarEstadisticas(){
        ataque= ataqueBase;
        defensa= defensaBase;
        ataqueEspecial= ataqueEspecialBase;
        defensaEspecial= defensaEspecialBase;
        velocidad= velocidadBase;
    }

    /*
    ***
    None
    ***
    None
    ***
    Reinicia completamente al Javaling para un nuevo combate,
    restaurando su HP, estadísticas y ataques de estado utilizados.
    */
    public void reiniciar(){
        hpActual= hpMaximo;
        reiniciarEstadisticas();

        for (int i= 0;i< movimientos.length;i++) {
            if (movimientos[i] instanceof AtaqueEstado){
                AtaqueEstado ataqueEstado= (AtaqueEstado)movimientos[i];
                ataqueEstado.reiniciarUso();
            }
        }
    }

    /*
    ***
    Parametro 1: TipoElemental elementoAtaque
    ***
    double
    ***
    Calcula y retorna el multiplicador de daño según el tipo elemental
    del ataque recibido y el tipo del Javaling.
    */
    @Override
    public double multiplicadorDebilidad(
        TipoElemental elementoAtaque
    ) {
        if (elementoAtaque== TipoElemental.FUEGO && this.tipo== TipoElemental.PLANTA){
            return 2.0;
        } else if(elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.FUEGO){
            return 2.0;
        } else if(elementoAtaque == TipoElemental.AGUA && this.tipo == TipoElemental.TIERRA){
            return 2.0;
        } else if (elementoAtaque== TipoElemental.PLANTA && this.tipo == TipoElemental.AGUA){
            return 2.0;
        } else if(elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.TIERRA){
            return 2.0;
        } else if(elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.AGUA){
            return 2.0;
        } else if (elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.VOLADOR){
            return 2.0;
        } else if (elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.FUEGO){
            return 2.0;
        } else if(elementoAtaque== TipoElemental.TIERRA && this.tipo == TipoElemental.ELECTRICO){
            return 2.0;
        } else if(elementoAtaque== TipoElemental.VOLADOR && this.tipo == TipoElemental.PLANTA){
            return 2.0;

        } else if (elementoAtaque== TipoElemental.FUEGO && this.tipo == TipoElemental.AGUA){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.FUEGO && this.tipo== TipoElemental.TIERRA){
            return 0.5;
        } else if (elementoAtaque == TipoElemental.FUEGO && this.tipo == TipoElemental.FUEGO){
            return 0.5;
        } else if(elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.PLANTA){
            return 0.5;
        } else if(elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.AGUA){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.FUEGO){
            return 0.5;
        } else if(elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.PLANTA){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.PLANTA && this.tipo == TipoElemental.VOLADOR){
            return 0.5;
        } else if(elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.ELECTRICO){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.PLANTA){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.PLANTA){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.VOLADOR && this.tipo== TipoElemental.ELECTRICO){
            return 0.5;
        } else if (elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.VOLADOR){
            return 0.0;
        } else if (elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.TIERRA){
            return 0.0;
        }

        return 1.0;
    }
}