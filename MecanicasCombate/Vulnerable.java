package MecanicasCombate;

public interface Vulnerable{

    /*
    ***
    Parametro 1: TipoElemental elementoAtaque
    ***
    double
    ***
    Define el método que calcula y retorna el multiplicador de daño
    según el tipo elemental del ataque recibido.
    */
    double multiplicadorDebilidad(TipoElemental elementoAtaque);

}