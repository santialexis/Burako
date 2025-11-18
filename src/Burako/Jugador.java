package Burako;

import java.util.*;

import static Burako.Partida.continuar;
import static Burako.Reglas.*;

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
        if(validarAgregarFicha(ficha,jugada,this)) {
            jugada.agregarFicha(ficha);
            atril.remove(ficha);
            System.out.println("-> Ficha agregada! <-");
        } else {
            System.out.println("-> No se puede agregar la ficha a la jugada! <-");
        }
        continuar();
    }

    public Jugada armarJugada(){
        List<Ficha> fichasJugada = new ArrayList<>(); //fichas a jugar
        Scanner scan = new Scanner(System.in);

        System.out.println("\n-> Seleccione las fichas de la jugada (numero + color) (\"comodin\" para comodines de 50) <-");
        System.out.println("-> No digite nada para terminar <-");

        while(true){ //seleccionar las fichas a jugar
            mostrarAtril();
            System.out.print("-> ");
            String str = scan.nextLine();
            String ficha = str.strip();

            if(ficha.isEmpty()){ //termino de seleccionar
                break;
            }

            Optional<Ficha> fichaEnAtril = atril.stream() //Optional: puede contener la ficha deseada o no
                    .filter(f -> f.toString().equalsIgnoreCase(ficha))
                    .findFirst();

            if(fichaEnAtril.isPresent()){ //si contiene la ficha, se agrega a la posible jugada
                Ficha fichita = fichaEnAtril.get();
                fichasJugada.add(fichita);
                atril.remove(fichita);
            } else {
                System.out.println("-> La ficha no esta en el atril! <-");
            }
        }

        Jugada jugada = new Jugada(fichasJugada);

        //si la jugada no es valida o se quedo sin fichas, pero no puede cortar, se devuelven las fichas al atril
        if(!jugadaValida(jugada) || (atril.size() <= 1 && !puedeCortar(this))){
            if(!jugadaValida(jugada)){
                System.out.println("-> La jugada no es valida. Se devolveran las fichas al atril <-");
            } else {
                System.out.println("-> No puede cortar hasta que haya hecho al menos 1 canasta! <-");
            }
            atril.addAll(fichasJugada);
            fichasJugada.clear();
            continuar();
            return null;
        }
        System.out.println("\n-> Jugada bajada! <-");
        continuar();
        return jugada; //si es valida y se puede bajar, se bajan las fichas
    }

    public Jugada elegirJugada(){
        Scanner scan = new Scanner(System.in);
        int jugada;
        System.out.println("\n-> Seleccione una jugada (nro. de jugada) <-");
        System.out.println("-> No digite nada para cancelar <-");
        mostrarJugadas();

        while(true){
            String num = scan.nextLine();
            num = num.strip();

            if(num.isEmpty()){
                return null;
            }

            if(num.matches("\\d+")){
                jugada = Integer.parseInt(num);
                if(jugada < 1 || jugada > jugadasEnMesa.size()){
                    System.out.println("-> La jugada no existe! <-");
                    System.out.print("-> ");
                } else {
                    break;
                }
            } else {
                System.out.println("-> La jugada no existe! <-");
                System.out.print("-> ");
            }
        }
        return jugadasEnMesa.get(jugada-1);
    }

    public Ficha elegirFicha(){
        Scanner scan = new Scanner(System.in);
        System.out.println("\n-> Seleccione una ficha (numero + color) <-");
        System.out.println("-> No digite nada para cancelar <-");

        System.out.println();
        mostrarAtril();

        while(true){
            System.out.print("-> ");
            String str = scan.nextLine();
            String ficha = str.strip();

            if(ficha.isEmpty()){
                return null;
            }

            Optional<Ficha> existeFicha = atril.stream()
                    .filter(f -> f.toString().equalsIgnoreCase(ficha))
                    .findFirst();

            if(existeFicha.isPresent()){
                return existeFicha.get();
            } else {
                System.out.print("La ficha no esta en el atril! ");
            }
        }
    }

    public void descartarFicha(Ficha ficha){
        atril.remove(ficha);
    }

    public void intentarCortar(Partida partida){
        if(!tomoMuerto){
            tomarMuerto();
            System.out.println("-> " + nombre + " ha tomado su muerto <-");
        } else {
            puntaje += 100;
            partida.setEnCurso(false);
            System.out.println("\n==== " + nombre + " gano la ronda!!! ====");
        }
        continuar();
    }

    public void tomarMuerto(){
        tomoMuerto = true;
        atril.addAll(muerto);
        muerto.clear();
    }

    public void mostrarAtril(){
        Map<Color,List<Ficha>> gruposFichas = new LinkedHashMap<>();
        for(Color c : Color.values()){
            gruposFichas.put(c, new ArrayList<>());
        }

        for(Ficha f : atril){
            gruposFichas.get(f.getColor()).add(f);
        }

        for(List<Ficha> grupo : gruposFichas.values()){
            grupo.sort(Comparator.comparingInt(Ficha::getNumero));
        }

        for(Color c : Color.values()){
            List<Ficha> grupo = gruposFichas.get(c);
            if(!grupo.isEmpty()){
                System.out.print(c.emoji() + " ");
                for(Ficha f : grupo){
                    System.out.print("[" + f.toString() + "]");
                }
                System.out.println();
            }
        }
    }

    public void mostrarJugadas(){
        for(int j=0; j < jugadasEnMesa.size(); j++){
            System.out.println((j+1) + "º jugada:");
            Jugada jugada = jugadasEnMesa.get(j);
            for(Ficha f : jugada.getFichas()){
                System.out.print(" [" + f.toString() + "]");
            }
            if(jugada.esCanasta()){
                if(esCanastaPura(jugada)){
                    System.out.print(" (Canasta pura)");
                } else {
                    System.out.print(" (Canasta impura)");
                }
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
        return jugadasEnMesa.stream().filter(j -> j.esCanasta()).toList();
    }

    public boolean tomoElMuerto(){
        return tomoMuerto;
    }

    public boolean hizoCanasta(){
        return !getCanastas().isEmpty();
    }

    @Override
    public String toString(){
        return nombre + " (" + puntaje + " puntos)";
    }
}
