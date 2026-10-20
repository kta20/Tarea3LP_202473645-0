package Simulador;

import Entidades.Equipo;

public class GestorTorneo{

    private JuezCombate juez;
    private String registroTorneo;

    /*
    ***
    None
    ***
    None
    ***
    Crea el gestor del torneo e inicializa el juez de combate
    y el registro general del torneo.
    */
    public GestorTorneo(){
        this.juez= new JuezCombate();
        this.registroTorneo= "";
    }

    /*
    ***
    Parametro 1: Equipo[] equipos
    ***
    Equipo
    ***
    Ejecuta el torneo por eliminación directa, enfrenta a los equipos
    de dos en dos, guarda a los ganadores de cada ronda y retorna
    al equipo campeón.
    */
    public Equipo iniciarTorneo(Equipo[] equipos){

        Equipo[] rondaActual= equipos;
        while (rondaActual.length> 1){
            Equipo[] ganadores=new Equipo[rondaActual.length/ 2];
            int posicionGanador = 0;
            for (int i= 0;i< rondaActual.length;i += 2){
                Equipo equipo1= rondaActual[i];
                Equipo equipo2= rondaActual[i+1];
                Equipo ganador= juez.simularMatch(equipo1,equipo2);
                ganadores[posicionGanador]= ganador;
                posicionGanador++;
                registroTorneo= registroTorneo+juez.getUltimoRegistro();
                registroTorneo= registroTorneo+"\n";
            }
            rondaActual= ganadores;
        }
        Equipo campeon= rondaActual[0];
        String mensajeFinal= "[TORNEO_FIN] Campeón del torneo: "+campeon.getNombre();
        System.out.println(mensajeFinal);
        registroTorneo= registroTorneo+mensajeFinal+"\n";
        return campeon;
    }

    public String getRegistroTorneo(){
        return registroTorneo;
    }
}