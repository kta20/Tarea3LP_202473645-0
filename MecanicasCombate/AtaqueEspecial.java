package MecanicasCombate;
import Entidades.Javaling;

public class AtaqueEspecial extends Ataque{
    private int potencia;

    /*
    ***
    Parametro 1: String nombre
    Parametro 2: TipoElemental tipo
    Parametro 3: int potencia
    ***
    None
    ***
    Crea un ataque especial con su nombre, tipo elemental y potencia.
    */
    public AtaqueEspecial(String nombre, TipoElemental tipo, int potencia){
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
    Calcula el daño de un ataque especial considerando ataque especial,
    defensa especial, multiplicador de tipo y STAB, y retorna el daño.
    */
    @Override
    public int ejecutar(Javaling atacante, Javaling defensor){
        double multiplicadorTipo= defensor.multiplicadorDebilidad(this.tipo);
        double stab= 1.0;

        if(atacante.getTipo()== this.tipo){
            stab= 1.5;
        }
        double daño= (potencia*((double) atacante.getAtaqueEspecial()/defensor.getDefensaEspecial())*multiplicadorTipo*stab)/ 2.0;
        return (int)daño;
    }
}