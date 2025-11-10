package Burako;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Burako {
    public static void main(String[] args) {
        List<Jugador> jugadores = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        for(int i=0; i < 2; i++){
            System.out.print("Jugador " + i + ": ");
            String nombre = scan.nextLine();
            jugadores.add(new Jugador(nombre));
        }

        Juego juego = new Juego(jugadores);
        juego.iniciar();
    }
}
