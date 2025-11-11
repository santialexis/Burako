package Burako;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Jugada {
    private List<Ficha> jugada;
    private boolean esEscalera;

    public Jugada(List<Ficha> jugada){
        this.jugada = new ArrayList<>(jugada);
    }

    public void agregarFicha(Ficha ficha){
        jugada.add(ficha);
        ordenarJugada();
    }

    public List<Ficha> getFichas(){ return jugada; }

    public boolean jugadaCanasta(){ return jugada.size() >= 7; }

    public boolean canastaPura(){
        return jugada.stream().filter(Ficha::esComodin).toList().isEmpty();
    }

    public void ordenarJugada(){
        if(esEscalera){
            jugada.sort(Comparator.comparing(Ficha::getNumero));
        }
    }

    //validar jugada
    public boolean jugadaValida(){
        if(jugada.size() < 3){
            return false;
        }

        List<Ficha> fichasComunes = jugada.stream().filter(f->!f.esComodin()).toList();
        int comodines = (int) jugada.stream().filter(f->f.esComodin()).count();

        if(jugadaEscalera(fichasComunes,comodines)){
            this.esEscalera = true;
            return true;
        }
        else if(jugadaPierna(fichasComunes)){
            this.esEscalera = false;
            return true;
        }
        return false;
    }

    //validar escalera
    private boolean jugadaEscalera(List<Ficha> fichasComunes, int totalComodines){
        if(fichasComunes.size() < 2){
            return false;
        }

        Color color = fichasComunes.get(0).getColor();
        for(Ficha f : fichasComunes){
            if(!f.getColor().equals(color)){
                return false;
            }
        }

        List<Integer> numeros = fichasComunes.stream().map(Ficha::getNumero).sorted().toList();
        int huecos = 0;
        int cant;

        for(int i=1; i < numeros.size(); i++){
            cant = numeros.get(i) - numeros.get(i-1) - 1;
            if(cant > 1){ return false; }
            huecos += cant;
        }

        return huecos <= totalComodines;
    }

    //validar pierna
    private boolean jugadaPierna(List<Ficha> fichasComunes){
        if(fichasComunes.size() < 2){
            return false;
        }

        int numPierna = fichasComunes.get(0).getNumero();
        for(Ficha f : fichasComunes){
            if(f.getNumero() != numPierna){
                return false;
            }
        }

        return true;
    }
}