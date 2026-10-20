package Simulador;

import Entidades.Equipo;
import Entidades.Javaling;
import MecanicasCombate.Ataque;
import MecanicasCombate.AtaqueEstado;

public class JuezCombate{
    private String ultimoRegistro;
    private int numeroEnfrentamiento= 1;

    /*
    ***
    Parametro 1: Equipo local
    Parametro 2: Equipo visitante
    ***
    Equipo
    ***
    Simula un enfrentamiento completo entre dos equipos, determina
    el orden de las acciones y retorna al equipo ganador.
    */
    public Equipo simularMatch(Equipo local,Equipo visitante){
        local.reiniciarEquipo();
        visitante.reiniciarEquipo();

        RegistroTurno registro= new RegistroTurno();
        int enfrentamientoActual= numeroEnfrentamiento;
        numeroEnfrentamiento++;

        registro.escribir(
            "[MATCH_INICIO] Enfrentamiento "+enfrentamientoActual+": "+local.getNombre()+" vs "+visitante.getNombre());
        int turno= 1;
        while (true){

            registro.escribir("Turno "+turno+":");
            if(turno== 1){
                registro.registrarEntrada(local);
                registro.registrarEntrada(visitante);
            }
            Javaling localActivo= local.getJavalingActivo();
            Javaling visitanteActivo= visitante.getJavalingActivo();
            if (localActivo.getVelocidad()>=visitanteActivo.getVelocidad()){
                boolean visitanteSigue=realizarAccion(local,visitante,registro);
                if (!visitanteSigue){
                    registro.registrarFinMatch(local,visitante,local);
                    ultimoRegistro=registro.getLog();
                    return local;
                }
                boolean localSigue= realizarAccion(visitante,local,registro);
                if (!localSigue){
                    registro.registrarFinMatch(local,visitante,visitante);
                    ultimoRegistro =registro.getLog();
                    return visitante;
                }
            } else{
                boolean localSigue =realizarAccion(visitante,local,registro);
                if(!localSigue){
                    registro.registrarFinMatch(local,visitante,visitante);
                    ultimoRegistro =registro.getLog();
                    return visitante;
                }
                boolean visitanteSigue= realizarAccion(local,visitante,registro);
                if(!visitanteSigue){
                    registro.registrarFinMatch(local,visitante,local);
                    ultimoRegistro= registro.getLog();
                    return local;
                }
            }
            turno++;
        }
    }

    public String getUltimoRegistro() {
        return ultimoRegistro;
    }

    /*
    ***
    Parametro 1: Equipo equipoAtacante
    Parametro 2: Equipo equipoDefensor
    Parametro 3: RegistroTurno registro
    ***
    boolean
    ***
    Decide la acción que realizará el Javaling activo siguiendo
    la heurística de ataque, cambio, estado y mejor ataque disponible.
    */
    private boolean realizarAccion(Equipo equipoAtacante,Equipo equipoDefensor,RegistroTurno registro){
        Javaling atacante =equipoAtacante.getJavalingActivo();
        Javaling defensor =equipoDefensor.getJavalingActivo();

        Ataque ataqueFuerte=buscarAtaqueMayor50(atacante,defensor);
        if(ataqueFuerte!= null) {
            return ejecutarAtaque(equipoAtacante,equipoDefensor,ataqueFuerte,registro);
        }
        int indiceCambio =buscarCambio(equipoAtacante,defensor);
        if(indiceCambio !=-1){
            Javaling anterior=equipoAtacante.getJavalingActivo();
            anterior.reiniciarEstadisticas();
            equipoAtacante.hacerCambio(indiceCambio);
            Javaling nuevo=equipoAtacante.getJavalingActivo();
            registro.registrarCambio(equipoAtacante,anterior,nuevo);
            return true;
        }
        AtaqueEstado ataqueEstado =buscarAtaqueEstado(atacante);
        if (ataqueEstado!= null){
            ataqueEstado.ejecutar(atacante,defensor);
            registro.registrarEstado(atacante,ataqueEstado);
            return true;
        }

        Ataque mejorAtaque =obtenerMejorAtaque(atacante,defensor);
        if (mejorAtaque !=null){
            return ejecutarAtaque(equipoAtacante,equipoDefensor,mejorAtaque,registro);
        }
        return true;
    }

    /*
    ***
    Parametro 1: Equipo equipoAtacante
    Parametro 2: Equipo equipoDefensor
    Parametro 3: Ataque ataque
    Parametro 4: RegistroTurno registro
    ***
    boolean
    ***
    Ejecuta el ataque seleccionado, aplica el daño al defensor
    y revisa si ocurrió un KO o si quedan Javalings vivos.
    */
    private boolean ejecutarAtaque(Equipo equipoAtacante,Equipo equipoDefensor, Ataque ataque,RegistroTurno registro) {
        Javaling atacante =equipoAtacante.getJavalingActivo();
        Javaling defensor= equipoDefensor.getJavalingActivo();
        int danio =ataque.ejecutar(atacante,defensor);
        defensor.recibirDanio(danio);
        registro.registrarAtaque(atacante,defensor,ataque, danio);
        
        if (defensor.getHpActual()== 0){
            registro.registrarKO(atacante,defensor);
            defensor.reiniciarEstadisticas();

            boolean quedanVivos=equipoDefensor.pasarAlSigVivo();
            if(quedanVivos){
                registro.registrarEntrada(equipoDefensor);
            } else{
                return false;
            }
        }
        return true;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    Parametro 2: Javaling defensor
    ***
    Ataque
    ***
    Busca el ataque que pueda quitar más del 50% de la vida actual
    del defensor y retorna el que produzca mayor daño.
    */
    private Ataque buscarAtaqueMayor50(Javaling atacante,Javaling defensor){
        Ataque[] movimientos =atacante.getMovimientos();
        Ataque mejorAtaque= null;
        int mayorDanio= -1;
        double mitadVida= defensor.getHpActual()*0.5;
        
        for (int i= 0;i< movimientos.length;i++){
            if (!(movimientos[i]instanceof AtaqueEstado)){
                int danioActual =movimientos[i].ejecutar(atacante,defensor);
                if (danioActual> mitadVida && danioActual> mayorDanio){
                    mayorDanio =danioActual;
                    mejorAtaque =movimientos[i];
                }
            }
        }

        return mejorAtaque;
    }

    /*
    ***
    Parametro 1: Equipo equipo
    Parametro 2: Javaling defensor
    ***
    int
    ***
    Busca un compañero vivo cuyo mejor ataque supere el 50% de la
    vida actual del rival y retorna su índice. Si no existe retorna -1.
    */
    private int buscarCambio(Equipo equipo,Javaling defensor){
        Javaling[] integrantes =equipo.getIntegrantes();
        int indiceActual= equipo.getIndiceActivo();
        int mejorIndice =-1;
        int mayorDanio =-1;

        double mitadVida =defensor.getHpActual()*0.5;
        for(int i= 0;i< integrantes.length;i++){
            if (i!= indiceActual && integrantes[i].getHpActual()> 0){
                int mejorDanio=calcularMejorDanio(integrantes[i],defensor);
                if (mejorDanio> mitadVida && mejorDanio> mayorDanio){
                    mayorDanio=mejorDanio;
                    mejorIndice= i;
                }
            }
        }

        return mejorIndice;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    ***
    AtaqueEstado
    ***
    Busca el primer ataque de estado que todavía no haya sido utilizado
    por el Javaling y lo retorna. Si no existe retorna null.
    */
    private AtaqueEstado buscarAtaqueEstado(Javaling atacante){
        Ataque[] movimientos =atacante.getMovimientos();
        for (int i= 0;i< movimientos.length;i++){
            if(movimientos[i] instanceof AtaqueEstado){
                AtaqueEstado ataqueEstado=(AtaqueEstado)movimientos[i];
                if (!ataqueEstado.estaUsado()){
                    return ataqueEstado;
                }
            }
        }
        return null;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    Parametro 2: Javaling defensor
    ***
    Ataque
    ***
    Busca entre los ataques normales del Javaling y retorna
    el que produzca la mayor cantidad de daño.
    */
    private Ataque obtenerMejorAtaque(Javaling atacante,Javaling defensor){
        Ataque[] movimientos =atacante.getMovimientos();
        Ataque mejorAtaque = null;
        int mayorDanio =-1;

        for (int i= 0;i< movimientos.length;i++){
            if(!(movimientos[i] instanceof AtaqueEstado)){
                int danioActual =movimientos[i].ejecutar(atacante,defensor);
                if (danioActual>mayorDanio){
                    mayorDanio=danioActual;
                    mejorAtaque=movimientos[i];
                }
            }
        }
        return mejorAtaque;
    }

    /*
    ***
    Parametro 1: Javaling atacante
    Parametro 2: Javaling defensor
    ***
    int
    ***
    Calcula y retorna el mayor daño que puede realizar el atacante
    contra el defensor utilizando sus ataques normales.
    */
    private int calcularMejorDanio(Javaling atacante,Javaling defensor){
        Ataque[] movimientos =atacante.getMovimientos();
        int mayorDanio= 0;
        for (int i= 0;i< movimientos.length;i++){
            if (!(movimientos[i] instanceof AtaqueEstado)){
                int danio= movimientos[i].ejecutar(atacante,defensor);
                if (danio> mayorDanio){
                    mayorDanio=danio;
                }
            }
        }
        return mayorDanio;
    }

    private class RegistroTurno {
        private String log ="";

        /*
        ***
        Parametro 1: String mensaje
        ***
        None
        ***
        Muestra el mensaje por consola y lo agrega al registro
        del enfrentamiento.
        */
        public void escribir(String mensaje){
            System.out.println(mensaje);
            log= log+mensaje+"\n";
        }

        public String getLog() {
            return log;
        }

        /*
        ***
        Parametro 1: Equipo equipo
        ***
        None
        ***
        Registra la entrada del Javaling activo de un equipo.
        */
        public void registrarEntrada(Equipo equipo){
            Javaling activo =equipo.getJavalingActivo();
            escribir("[ENTRA] "+equipo.getNombre()+" envía a "+activo.nombre+".");
        }

        /*
        ***
        Parametro 1: Equipo equipo
        Parametro 2: Javaling anterior
        Parametro 3: Javaling nuevo
        ***
        None
        ***
        Registra el cambio entre el Javaling que sale y el que entra
        al combate.
        */
        public void registrarCambio(Equipo equipo,Javaling anterior,Javaling nuevo){
            escribir("[CAMBIO] "+equipo.getNombre()+" retira a "+anterior.nombre+" y envía a "+nuevo.nombre+".");
        }

        /*
        ***
        Parametro 1: Javaling atacante
        Parametro 2: AtaqueEstado ataque
        ***
        None
        ***
        Registra el uso de un ataque de estado, mostrando la estadística
        modificada, el multiplicador y su nuevo valor.
        */
        public void registrarEstado(Javaling atacante,AtaqueEstado ataque){
            String stat =ataque.getStatAfectado();
            double multiplicador =ataque.getMultiplicador();
            int valorActual =atacante.getEstadisticaActual(stat);
            String nombreStat =convertirNombreEstadistica(stat);
            escribir("[ESTADO] "+ atacante.nombre + " usa "+ ataque.getNombre()+ " (estado) | "+ nombreStat.toLowerCase()+ " de " +atacante.nombre +" modificado por x" +multiplicador+". "+nombreStat+" actual: " +valorActual+".");
        }

        /*
        ***
        Parametro 1: Javaling atacante
        Parametro 2: Javaling defensor
        Parametro 3: Ataque ataque
        Parametro 4: int danio
        ***
        None
        ***
        Registra el ataque realizado, el daño producido y la vida
        restante del defensor.
        */
        public void registrarAtaque(Javaling atacante,Javaling defensor,Ataque ataque,int danio){
            escribir("[ATAQUE] "+ atacante.nombre +" ataca a " +defensor.nombre +" con " +ataque.getNombre()+ " | Daño: " +danio+" | Vida restante de " +defensor.nombre +": " +defensor.getHpActual());
        }

        /*
        ***
        Parametro 1: Javaling atacante
        Parametro 2: Javaling defensor
        ***
        None
        ***
        Registra cuando un Javaling derrota al Javaling defensor.
        */
        public void registrarKO(
            Javaling atacante,
            Javaling defensor
        ){

            escribir("[KO] "+atacante.nombre +" ha derrotado a " +defensor.nombre +".");
        }

        /*
        ***
        Parametro 1: Equipo local
        Parametro 2: Equipo visitante
        Parametro 3: Equipo ganador
        ***
        None
        ***
        Registra el final del enfrentamiento mostrando al ganador
        y la cantidad de Javalings vivos de cada equipo.
        */
        public void registrarFinMatch(Equipo local,Equipo visitante,Equipo ganador){
            escribir("[MATCH_FIN] Ganador: " +ganador.getNombre()+" | Marcador final: "+local.contarVivos()+" - " +visitante.contarVivos());
        }

        /*
        ***
        Parametro 1: String stat
        ***
        String
        ***
        Convierte el nombre interno de una estadística a un nombre
        más legible para mostrarlo en el registro.
        */
        private String convertirNombreEstadistica(String stat){
            if (stat.equals("ataque")){
                return "Ataque";
            }else if (stat.equals("defensa")){
                return "Defensa";
            } else if (
                stat.equals("ataque_especial")){
                return "Ataque especial";
            } else if (
                stat.equals("defensa_especial")){
                return "Defensa especial";
            } else if (stat.equals("velocidad")){
                return "Velocidad";
            }
            return stat;
        }
    }
}