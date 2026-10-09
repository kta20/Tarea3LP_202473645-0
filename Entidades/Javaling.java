package Entidades;

import MecanicasCombate.TipoElemental;
import MecanicasCombate.Vulnerable;

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

    public Javaling(
        String nombre,
        TipoElemental tipo,
        int hpMaximo,
        int ataque,
        int defensa,
        int ataqueEspecial,
        int defensaEspecial,
        int velocidad
    ) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.hpMaximo = hpMaximo;
        this.hpActual = hpMaximo;
        this.ataque = ataque;
        this.defensa = defensa;
        this.ataqueEspecial = ataqueEspecial;
        this.defensaEspecial = defensaEspecial;
        this.velocidad = velocidad;
    }

    public int getHpActual() {
        return hpActual;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }

    public int getDefensaEspecial() {
        return defensaEspecial;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public TipoElemental getTipo() {
        return tipo;
    }

@Override
public double multiplicadorDebilidad(TipoElemental elementoAtaque) {

    if(elementoAtaque== TipoElemental.FUEGO && this.tipo == TipoElemental.PLANTA){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.FUEGO){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.TIERRA){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.AGUA){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.TIERRA){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.AGUA){
        return 2.0;
    }else if (elementoAtaque== TipoElemental.VOLADOR && this.tipo== TipoElemental.PLANTA){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.VOLADOR){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.FUEGO){
        return 2.0;
    } else if(elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.ELECTRICO){
        return 2.0;
    } else if (elementoAtaque== TipoElemental.FUEGO && this.tipo== TipoElemental.AGUA){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.FUEGO && this.tipo== TipoElemental.TIERRA){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.FUEGO && this.tipo== TipoElemental.FUEGO){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.PLANTA){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.AGUA && this.tipo== TipoElemental.AGUA){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.FUEGO){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.PLANTA){
        return 0.5;
    }else if (elementoAtaque== TipoElemental.PLANTA && this.tipo== TipoElemental.VOLADOR){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.ELECTRICO){
        return 0.5;
    }else if (elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.PLANTA){
        return 0.5;
    } else if (elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.PLANTA){
        return 0.5;
    }else if (elementoAtaque== TipoElemental.VOLADOR && this.tipo== TipoElemental.ELECTRICO){
        return 0.5;
    }else if (elementoAtaque== TipoElemental.TIERRA && this.tipo== TipoElemental.VOLADOR){
        return 0.0;
    } else if (elementoAtaque== TipoElemental.ELECTRICO && this.tipo== TipoElemental.TIERRA){
        return 0.0;
    }

    return 1.0;
}

