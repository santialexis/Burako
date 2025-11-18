package Burako;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Burako {
    public static void main(String[] args) {
        List<Jugador> jugadores = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        System.out.println("==================== BURAKO ====================");
        System.out.println("\nIngrese los nombres de los jugadores:");

        for(int i=0; i < 2; i++){
            System.out.print("Jugador " + (i+1) + ": ");
            String nombre = scan.nextLine();
            nombre = nombre.strip();
            jugadores.add(new Jugador(nombre));
        }

        Juego juego = new Juego(jugadores);
        juego.iniciar();
    }
}