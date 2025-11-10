package Burako;

import java.util.List;

public class Juego {
    private List<Jugador> jugadores;
    private boolean enCurso;

    public Juego(List<Jugador> jugadores){
        this.jugadores = jugadores;
        this.enCurso = true;
    }

    public void iniciar(){
        while(enCurso){
            Partida partidaActual = new Partida(jugadores);
            partidaActual.iniciarPartida();

            for(Jugador j : jugadores){
                if(j.getPuntaje() >= 2000){
                    System.out.println("===================================================");
                    System.out.println("EL JUGADOR " + j.getNombre().toUpperCase() + " HA GANADO LA PARTIDA!!!");
                    System.out.println("===================================================");
                    enCurso = false;
                }
            }
        }
    }
}
