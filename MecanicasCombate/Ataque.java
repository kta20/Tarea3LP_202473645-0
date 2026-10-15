package MecanicasCombate;
import Entidades.Javaling;

public abstract class Ataque {
    protected String nombre;
    protected TipoElemental tipo;

    public Ataque(String nombre, TipoElemental tipo) {
        this.nombre= nombre;
        this.tipo= tipo;
    }
    public abstract int ejecutar(Javaling atacante, Javaling defensor);
    public String getNombre() {
    return nombre;
    }
}
