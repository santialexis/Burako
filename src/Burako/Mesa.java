package Burako;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Mesa {
    private Stack<Ficha> pila;
    private List<Ficha> pozo;

    public Mesa(){
        this.pila = new Stack<>();
        this.pozo = new ArrayList<>();
    }

    public Ficha robarDePila(){
        if(pila.isEmpty()){
            System.out.println("Ya no hay mas fichas en la pila!");
            return null;
        }
        return pila.pop();
    }

    public List<Ficha> robarPozo(){
        List<Ficha> pozovich = new ArrayList<>(pozo);
        pozo.clear();
        return pozovich;
    }

    public void fichaAlPozo(Ficha ficha){
        pozo.add(ficha);
    }

    public void setPila(List<Ficha> fichas){
        pila.addAll(fichas);
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
    }
}