package Burako;

public class Ficha {
    private final int numero;
    private final Color color;
    private final int puntos;
    private final boolean comodin;

    public Ficha(int numero, Color color){
        this.numero = numero;
        this.color = color;
        this.puntos = puntosFicha();
        this.comodin = numero == 0 || numero == 2;
    }

    private int puntosFicha(){
        int pts = 0;
        if(numero == 0){ pts = 50; }
        else if(numero == 1){ pts = 15; }
        else if(numero == 2){ pts = 20; }
        else if(numero >= 3 && numero <= 7){ pts = 5; }
        else if(numero >= 8 && numero <= 13){ pts = 10; }
        return pts;
    }

    //GETTERS
    public int getNumero(){ return numero; }

    public Color getColor(){ return color; }

    public int getPuntos(){ return puntos; }

    public boolean esComodin(){ return comodin; }

    @Override
    public String toString(){
        if(numero == 0){
            return "Comodin";
        }
        else{
            return numero + " " + color;
        }
    }
}