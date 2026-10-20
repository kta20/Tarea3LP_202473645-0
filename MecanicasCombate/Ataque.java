package MecanicasCombate;

import Entidades.Javaling;

public abstract class Ataque{
    protected String nombre;
    protected TipoElemental tipo;

    /*
    ***
    Parametro 1: String nombre
    Parametro 2: TipoElemental tipo
    ***
    None
    ***
    Crea un ataque con su nombre y tipo elemental.
    */
    public Ataque(String nombre, TipoElemental tipo){
        this.nombre= nombre;
        this.tipo= tipo;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    Parametro 2: Javaling defensor
    ***
    int
    ***
    Método abstracto que ejecuta el ataque entre dos Javalings.
    Cada subclase define su comportamiento y retorna el daño realizado.
    */
    public abstract int ejecutar(Javaling atacante,Javaling defensor);
    public String getNombre() {
        return nombre;
    }
}