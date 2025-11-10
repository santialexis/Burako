package Burako;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private final String nombre;
    private List<Ficha> atril;
    private List<Ficha> muerto;
    private boolean tomoMuerto;
    private List<Jugada> jugadasEnMesa;
    private int puntaje;

    public Jugador(String nombre){
        this.nombre = nombre;
        this.atril = new ArrayList<>();
        this.muerto = new ArrayList<>();
        this.jugadasEnMesa = new ArrayList<>();
        this.puntaje = 0;
    }

    public void tomarFicha(Ficha ficha){
        if(ficha != null){
            atril.add(ficha);
        }
    }

    public void tomarPozo(List<Ficha> pozo){
        if(pozo != null){
            atril.addAll(pozo);
        }
    }

    public void jugarFichas(Jugada jugada){
        if(jugada != null){
            jugada.ordenarJugada();
            jugadasEnMesa.add(jugada);
        }
    }

    public void agregarFichaAJugada(Ficha ficha, Jugada jugada){
        List<Ficha> copia = new ArrayList<>(jugada.getFichas());
        copia.add(ficha);

        Jugada temp = new Jugada(copia);
        if(temp.jugadaValida()){
            atril.remove(ficha);
            jugada.agregarFicha(ficha);
        }
    }

    public void descartarFicha(Ficha ficha){
        atril.remove(ficha);
    }

    public void intentarCortar(){
        if(!tomoMuerto){
            tomarMuerto();
        } else {
            puntaje += 100;
            System.out.println("El jugador " + nombre + " se ha quedado sin fichas!");
        }
    }

    public void tomarMuerto(){
        tomoMuerto = true;
        atril.addAll(muerto);
        muerto.clear();
    }

    public void mostrarAtril(){
        for(Ficha f : atril){
            System.out.print("[" + f.toString() + "]");
        }
    }

    public void mostrarJugadas(){
        for(int j=0; j < jugadasEnMesa.size(); j++){
            System.out.print("\n" + j+1 + "º jugada ->");
            for(Ficha f : jugadasEnMesa.get(0).getFichas()){
                System.out.print(" [" + f.toString() + "]");
            }
            System.out.println();
        }
    }

    //SETTERS
    public void setAtril(List<Ficha> fichas){
        this.atril = fichas;
    }

    public void setMuerto(List<Ficha> muerto){
        this.muerto = muerto;
    }

    public void setPuntaje(int puntaje){
        this.puntaje = puntaje;
    }

    //GETTERS
    public boolean atrilVacio(){
        return atril.isEmpty();
    }

    public String getNombre(){
        return nombre;
    }

    public List<Ficha> getAtril(){
        return atril;
    }

    public List<Jugada> getJugadasEnMesa(){
        return jugadasEnMesa;
    }

    public int getPuntaje(){
        return puntaje;
    }

    public List<Jugada> getCanastas(){
        return jugadasEnMesa.stream().filter(j -> j.jugadaCanasta()).toList();
    }

    public boolean tomoElMuerto(){
        return tomoMuerto;
    }

    public boolean hizoCanasta(){
        return getCanastas().size() > 1;
    }

    @Override
    public String toString(){
        return nombre + " (" + puntaje + "puntos)";
    }
}
