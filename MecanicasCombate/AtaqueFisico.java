package MecanicasCombate;
import Entidades.Javaling;

public class AtaqueFisico extends Ataque{
    private int potencia;

    /*
    ***
    Parametro 1: String nombre
    Parametro 2: TipoElemental tipo
    Parametro 3: int potencia
    ***
    None
    ***
    Crea un ataque físico con su nombre, tipo elemental y potencia.
    */
    public AtaqueFisico(String nombre, TipoElemental tipo, int potencia){
        super(nombre, tipo);
        this.potencia= potencia;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    Parametro 2: Javaling defensor
    ***
    int
    ***
    Calcula el daño de un ataque físico considerando ataque, defensa,
    multiplicador de tipo y STAB, y retorna el daño realizado.
    */
    @Override
    public int ejecutar(Javaling atacante, Javaling defensor){
        double multiplicadorTipo= defensor.multiplicadorDebilidad(this.tipo);
        double stab= 1.0;
        if(atacante.getTipo()== this.tipo){
            stab= 1.5;
        }
        double daño =(potencia*((double) atacante.getAtaque()/ defensor.getDefensa())*multiplicadorTipo*stab)/ 2.0;
        return (int)daño;
    }
}