package Burako;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private List<Ficha> atril;
    private List<Jugada> jugadasEnMesa;
    private int puntaje;

    public Jugador(String nombre){
        this.nombre = nombre;
        this.atril = new ArrayList<>();
        this.jugadasEnMesa = new ArrayList<>();
        this.puntaje = 0;
    }

    public void tomarFicha(Ficha ficha){
        if(ficha != null){
            atril.add(ficha);
        }
    }

    public void descartarFicha(Ficha ficha){

    }

    public void jugarFichas(Jugada jugada){
        if(jugada.jugadaValida()){
            jugadasEnMesa.add(jugada);
            atril.removeAll(jugada.getFichas());
        }
    }

    public void agregarFichaAJugada(Ficha ficha, Jugada jugada){ /**/
        List<Ficha> copia = new ArrayList<>(jugada.getFichas());
        copia.add(ficha);

        Jugada temp = new Jugada(copia);
        if(temp.jugadaValida()){
            atril.remove(ficha);
            jugada.agregarFicha(ficha);
        }
    }

    public boolean atrilVacio(){
        return atril.isEmpty();
    }

    public int calcularPuntosRonda(){
        int puntosJugadas = jugadasEnMesa.stream().mapToInt(Jugada::getPuntos).sum();
        int puntosAtril = atril.stream().mapToInt(Ficha::getPuntos).sum();
        return puntosJugadas - puntosAtril;
    }

    public void sumarPuntos(){
        this.puntaje += calcularPuntosRonda();
    }

    public String getNombre(){
        return nombre;
    }

    public List<Ficha> getAtril(){
        return atril;
    }

    public int getPuntaje(){
        return puntaje;
    }

    @Override
    public String toString(){
        return nombre + " (" + puntaje + "puntos) ";
    }
}
