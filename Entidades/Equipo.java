package Entidades;

public class Equipo {

    private String nombre;
    private Javaling[] integrantes;
    private int indiceActivo;

    public Equipo(String nombre, Javaling[] integrantes) {
        this.nombre = nombre;
        this.integrantes = integrantes;
        this.indiceActivo = 0;
    }

    public Javaling getJavalingActivo() {
        return integrantes[indiceActivo];
    }

    public void hacerCambio(int nuevoIndice) {
        indiceActivo = nuevoIndice;
    }

    public boolean pasarAlSigVivo() {

        for (int i = 0; i < integrantes.length; i++) {

            if (integrantes[i].getHpActual() > 0) {
                indiceActivo = i;
                return true;
            }
        }

        return false;
    }

    public int contarVivos() {

        int vivos = 0;

        for (int i = 0; i < integrantes.length; i++) {

            if (integrantes[i].getHpActual() > 0) {
                vivos++;
            }
        }

        return vivos;
    }

    public void reiniciarEquipo() {

        indiceActivo = 0;

        for (int i = 0; i < integrantes.length; i++) {
            integrantes[i].reiniciar();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Javaling[] getIntegrantes() {
        return integrantes;
    }

    public int getIndiceActivo() {
        return indiceActivo;
    }
}