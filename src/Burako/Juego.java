package Burako;

import java.util.ArrayList;
import java.util.List;

public class Juego {
    private List<Jugador> jugadores;
    private boolean enCurso;

    public Juego(List<Jugador> jugadores){
        this.jugadores = jugadores;
        this.enCurso = true;
    }

    public void iniciar(){
        List<Jugador> ganadores = new ArrayList<>();
        while(enCurso){
            Partida partidaActual = new Partida(jugadores);
            partidaActual.iniciarPartida();

            for(Jugador j : jugadores){
                if(j.getPuntaje() >= 2000){
                    ganadores.add(j);
                }
            }

            if(ganadores.size() == 1){
                System.out.println("===================================================");
                System.out.println("EL JUGADOR " + ganadores.get(0).getNombre().toUpperCase() + " HA GANADO LA PARTIDA!");
                System.out.println("===================================================");
                enCurso = false;
            }
            else if (ganadores.size() > 1){
                System.out.println("===================================================");
                System.out.println("HA HABIDO UN EMPATE ENTRE LOS JUGADORES:");
                for(Jugador j : ganadores){
                    System.out.println(j);
                }
                System.out.println("===================================================");
                enCurso = false;
            }
        }
    }
}
