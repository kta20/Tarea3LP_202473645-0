package MecanicasCombate;
import Entidades.Javaling;

public class AtaqueEspecial extends Ataque{
    private int potencia;

    public AtaqueEspecial(String nombre, TipoElemental tipo, int potencia){
        super(nombre, tipo);
        this.potencia= potencia;
    }

    @Override
    public int ejecutar(Javaling atacante, Javaling defensor){
        double multiplicadorTipo= defensor.multiplicadorDebilidad(this.tipo);
        double stab= 1.0;

        if(atacante.getTipo()== this.tipo) {
            stab= 1.5;
        }
        double daño= (potencia*((double) atacante.getAtaqueEspecial()/defensor.getDefensaEspecial())*multiplicadorTipo*stab)/ 2.0;
        return (int)daño;
    }
}