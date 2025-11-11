package Burako;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Mesa {
    private Stack<Ficha> pila;
    private List<Ficha> pozo;

    public Mesa(){
        this.pila = new Stack<>();
        this.pozo = new ArrayList<>();
    }

    public void repartirFichas(List<Jugador> jugadores){
        List<Ficha> fichasTodas = crearFichas();
        Collections.shuffle(fichasTodas);

        for(Jugador j : jugadores){
            //repartir muertos
            List<Ficha> fichasMuerto = new ArrayList<>();
            for(int m=0; m < 11; m++){
                fichasMuerto.add((fichasTodas.remove(0)));
            }
            j.setMuerto(fichasMuerto);
            //repartir atril
            List<Ficha> fichasIniciales = new ArrayList<>();
            for(int a=0; a < 12; a++){
                fichasIniciales.add(fichasTodas.remove(0));
            }
            j.setAtril(fichasIniciales);
        }
        pila.addAll(fichasTodas);
    }

    public List<Ficha> crearFichas() {
        List<Ficha> fichasJuego = new ArrayList<>();

        for (int j = 0; j < 2; j++){
            for (Color col : Color.values()){
                if(col.equals(Color.NINGUNO)){continue;}
                for (int num = 1; num <= 13; num++){
                    fichasJuego.add(new Ficha(num,col));
                }
            }
            fichasJuego.add(new Ficha(0, Color.NINGUNO));
        }
        return fichasJuego;
    }

    public Ficha robarDePila(){
        if(pila.isEmpty()){
            System.out.println("Ya no hay mas fichas en la pila!");
            return null;
        }
        return pila.pop();
    }

    public List<Ficha> robarPozo(){
        if(pozo.isEmpty()){
            System.out.println("No hay fichas en el pozo!");
            return null;
        }
        List<Ficha> p = new ArrayList<>(pozo);
        pozo.clear();
        return p;
    }

    public void fichaAlPozo(Ficha ficha){
        pozo.add(ficha);
    }

    public boolean pilaVacia(){
        return pila.isEmpty();
    }

    public boolean pozoVacio(){
        return pozo.isEmpty();
    }

    public void mostrarPozo(){
        if(pozo.isEmpty()){
            System.out.println("El pozo esta vacio...");
        } else {
            for(Ficha f : pozo){
                System.out.print("[" + f.toString() + "]");
            }
        }
        System.out.println();
    }
}