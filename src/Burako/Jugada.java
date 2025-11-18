package Burako;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Jugada {
    private List<Ficha> jugada;

    public Jugada(List<Ficha> jugada){
        this.jugada = new ArrayList<>(jugada);
    }

    public void agregarFicha(Ficha ficha){
        jugada.add(ficha);
        ordenarJugada();
    }

    public void ordenarJugada(){
        if(Reglas.esEscalera(this)){
            jugada.sort(Comparator.comparing(Ficha::getNumero));
        }
    }

    public List<Ficha> getFichas(){ return jugada; }

    public boolean esEscalera(){  return Reglas.esEscalera(this); }

    public boolean esPierna(){ return Reglas.esPierna(this); }

    public boolean esCanasta(){ return jugada.size() >= 7; }

    public boolean esCanastaPura(){ return Reglas.esCanastaPura(this); }
}