package Burako;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Jugada {
    private List<Ficha> jugada;
    private boolean esCanasta;

    public Jugada(List<Ficha> jugada){
        this.jugada = new ArrayList<>(jugada);
    }

    public void agregarFicha(Ficha ficha){
        jugada.add(ficha);
    }

    public List<Ficha> getFichas(){
        return jugada;
    }

    public int getPuntos(){
        return jugada.stream().mapToInt(Ficha::getPuntos).sum();
    }

    //validar jugada
    public boolean jugadaValida(){
        if(jugada.size() < 3){
            return false;
        }

        List<Ficha> fichasComunes = jugada.stream()
                .filter(f->!f.esComodin())
                .sorted(Comparator.comparingInt(Ficha::getNumero))
                .toList();

        int comodines = (int) jugada.stream().filter(f->f.getNumero()==0).count();
        int comodinesDos = (int) jugada.stream().filter(f->f.getNumero()==2).count();
        int totalComodines = comodines + comodinesDos;

        if(jugadaEscalera(fichasComunes,totalComodines)){
            return true;
        }
        if(jugadaPierna(fichasComunes, totalComodines)){
            return true;
        }
        return false;
    }

    //validar escalera
    private boolean jugadaEscalera(List<Ficha> fichasComunes, int totalComodines){
        if(fichasComunes.isEmpty()){
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
        for(int i=1; i < numeros.size(); i++){
            huecos += numeros.get(i) - numeros.get(i-1) - 1;
        }

        return huecos <= totalComodines;
    }

    //validar pierna
    private boolean jugadaPierna(List<Ficha> fichasComunes, int totalComodines){
        if(fichasComunes.isEmpty()){
            return false;
        }

        int numPierna = fichasComunes.get(0).getNumero();
        for(Ficha f : fichasComunes){
            if(f.getNumero() != numPierna){
                return false;
            }
        }
        int totalFichas = fichasComunes.size() + totalComodines;
        return totalFichas >= 3;
    }
}