package Entidades;

public class Equipo {
    private String nombre;
    private Javaling[] integrantes;
    private int indiceActivo;

    /*
    ***
    Parametro 1: String nombre
    Parametro 2: Javaling[] integrantes
    ***
    None
    ***
    Crea un equipo con un nombre, sus integrantes y deja activo al
    primer Javaling del arreglo.
    */
    public Equipo(String nombre, Javaling[] integrantes){
        this.nombre= nombre;
        this.integrantes= integrantes;
        this.indiceActivo= 0;
    }
    public Javaling getJavalingActivo(){
        return integrantes[indiceActivo];
    }

    /*
    ***
    Parametro 1: int nuevoIndice
    ***
    None
    ***
    Cambia el Javaling activo del equipo utilizando el indice recibido.
    */
    public void hacerCambio(int nuevoIndice){
        indiceActivo= nuevoIndice;
    }

    /*
    ***
    None
    ***
    boolean
    ***
    Busca al primer Javaling vivo del equipo, lo deja como activo y
    retorna true. Si no quedan Javalings vivos retorna false.
    */
    public boolean pasarAlSigVivo(){
        for(int i= 0; i< integrantes.length; i++){
            if(integrantes[i].getHpActual()> 0){
                indiceActivo= i;
                return true;
            }
        }
        return false;
    }

    /*
    ***
    None
    ***
    int
    ***
    Cuenta los Javalings que tienen HP mayor a 0 y retorna la cantidad
    de integrantes que siguen vivos.
    */
    public int contarVivos(){
        int vivos= 0;
        for(int i= 0; i< integrantes.length; i++) {
            if(integrantes[i].getHpActual()> 0) {
                vivos++;
            }
        }
        return vivos;
    }

    /*
    ***
    None
    ***
    None
    ***
    Reinicia el equipo para comenzar un nuevo combate, dejando activo
    al primer Javaling y reiniciando a todos los integrantes.
    */
    public void reiniciarEquipo(){
        indiceActivo= 0;

        for(int i= 0; i< integrantes.length; i++) {
            integrantes[i].reiniciar();
        }
    }
    public String getNombre(){
        return nombre;
    }
    public Javaling[] getIntegrantes(){
        return integrantes;
    }
    public int getIndiceActivo(){
        return indiceActivo;
    }
}