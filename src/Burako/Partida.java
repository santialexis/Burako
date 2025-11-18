package Burako;

import java.util.*;
import static Burako.Reglas.*;

public class Partida {
    private List<Jugador> jugadores;
    private Mesa mesa;
    private int turno;
    private boolean primerTurno; //en el primer turno puede tomar hasta 2 fichas
    private boolean enCurso;

    public Partida(List<Jugador> jugadores){
        this.jugadores = jugadores;
        this.mesa = new Mesa();
        this.enCurso = true;
        this.primerTurno = true;
        this.turno = turnoInicial(jugadores);
        repartirFichas(jugadores,mesa);
    }

    public void setEnCurso(boolean enCurso){
        this.enCurso = enCurso;
    }

    /* METODOS DE PARTIDA */
    private int turnoInicial(List<Jugador> jugadores){
        Random rand = new Random();
        int mayorNumero = -1;
        Jugador jugadorInicial = null;

        for(Jugador j : jugadores){
            int num = rand.nextInt(1,7);
            System.out.print("\n-> " + j.getNombre() + " tiro el dado y saco un " + num + "! <-");
            if(num > mayorNumero){
                mayorNumero = num;
                jugadorInicial = j;
            }
        }
        System.out.println("\n-> Comienza " + jugadorInicial.getNombre() + " <-");
        System.out.println("\n== Presione ENTER para continuar... ==");
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
        return jugadores.indexOf(jugadorInicial);
    }

    public void iniciarPartida(){
        while(enCurso){
            Jugador actual = jugadores.get(turno);
            jugarTurno(actual); //turno del jugador actual

            if(mesa.pilaVacia() && mesa.pozoVacio() && enCurso){ //verificar fin por falta de fichas
                System.out.println("\n==== " + "No quedan mas fichas en la mesa!!!" + " ====\n");
                enCurso = false;
            }

            turno = (turno + 1) % jugadores.size(); //siguiente turno
        }

        for(Jugador jug : jugadores){ //calcular los puntos de todos los jugadores al terminar partida
            jug.setPuntaje(calcularPuntos(jug));
            System.out.println("-> " + jug);
        }
        continuar();
    }

    private void jugarTurno(Jugador jugador){
        limpiar();
        System.out.println("\n==== Turno de " + jugador.getNombre() + " ====");

        int opcionTomar; //opcion para tomar ficha/s
        boolean tomoFicha = false;

        System.out.println("\nATRIL:");
        jugador.mostrarAtril();

        System.out.println("\nJUGADAS:");
        jugador.mostrarJugadas();

        System.out.print("\nPOZO: ");
        mesa.mostrarPozo();

        System.out.println("\n1 -> Levantar ficha de la pila <-");
        System.out.println("2 -> Levantar fichas del pozo <-");

        Ficha fichaPila; //guarda la ficha a tomar si se toma de la pila

        while(!tomoFicha){ //hasta que no tome, no puede continuar con su turno
            System.out.print("-> ");
            opcionTomar = pedirOpcion();
            switch (opcionTomar) {
                case 1: //tomar de la pila
                    fichaPila = mesa.robarDePila();
                    if(fichaPila != null){
                        jugador.tomarFicha(fichaPila);
                        tomoFicha = true;
                        System.out.println("Se levanto un " + fichaPila);
                        if(primerTurno){
                            usarPrimerTurno(jugador,fichaPila);
                        }
                        continuar();
                        limpiar();
                    }
                    break;

                case 2: //tomar el pozo
                    if(mesa.pozoVacio()){
                        System.out.println("\n-> El pozo no tiene fichas para levantar! <-");
                    } else {
                        jugador.tomarPozo(mesa.robarPozo());
                        tomoFicha = true;
                        limpiar();
                    }
                    break;

                default:
                    System.out.println("Opcion invalida!");
                    break;
            }
        }

        int opcionJugar; //opcion para jugar
        boolean descarto = false; //puede jugar hasta que descarte una ficha

        Ficha fichaDescarte; //ficha a descartar
        Jugada jugadaMod; //jugada a modificar (si se quiere)
        Ficha fichaAdd; //ficha a agregar a la jugada (si se quiere)

        while(!descarto && !jugador.atrilVacio()){ //juega hasta que descarte una ficha o se quede sin
            System.out.println("\nATRIL:");
            jugador.mostrarAtril();
            mostrarOpciones();
            opcionJugar = pedirOpcion();
            switch (opcionJugar) {
                case 1:
                    jugador.jugarFichas(jugador.armarJugada());
                    if(jugador.atrilVacio()){
                        jugador.intentarCortar(this);
                    }
                    limpiar();
                    break;

                case 2:
                    if((jugadaMod = jugador.elegirJugada()) != null && (fichaAdd = jugador.elegirFicha()) != null){
                        jugador.agregarFichaAJugada(fichaAdd,jugadaMod);
                        if(jugador.atrilVacio()){
                            jugador.intentarCortar(this);
                        }
                    }
                    limpiar();
                    break;

                case 3:
                    if(jugador.getJugadasEnMesa().isEmpty()){
                        System.out.println("-> No tiene jugadas en mesa... <-");
                    } else {
                        jugador.mostrarJugadas();
                    }
                    continuar();
                    limpiar();
                    break;

                case 4:
                    if((fichaDescarte = jugador.elegirFicha()) != null){
                        mesa.fichaAlPozo(fichaDescarte);
                        jugador.descartarFicha(fichaDescarte);
                        descarto = true;
                        if(jugador.atrilVacio()){
                            jugador.intentarCortar(this);
                        }
                    }
                    limpiar();
                    break;

                case 5:
                    Random rand = new Random();
                    for(Jugador j : jugadores){
                        int p = j.getPuntaje();
                        int num = rand.nextInt(500,2000);
                        j.setPuntaje(p + num);
                    }
                    enCurso = false;
                    descarto = true;
                    break;

                case 6:
                    List<Ficha> fichas = new ArrayList<>();
                    for(int i=2; i < 10; i++){
                        if(i == 5){
                            continue;
                        }
                        fichas.add(new Ficha(i,Color.Amarillo));
                    }
                    Jugada jugada = new Jugada(fichas);
                    jugadaValida(jugada);
                    jugador.jugarFichas(jugada);
                    break;

                case 7:
                    Jugada j = jugador.getJugadasEnMesa().get(0);
                    jugador.agregarFichaAJugada(new Ficha(5,Color.Amarillo),j);
                    break;

                default:
                    System.out.println("Opcion invalida!");
                    break;
            }
        }
    }

    private void usarPrimerTurno(Jugador jugador, Ficha fichaTomada){
        System.out.print("Descartar y recoger otra? (1: Si / 2: No) -> ");
        int opcion = pedirOpcion();

        while(opcion < 1 || opcion > 2){
            System.out.print("Opcion invalida -> ");
            opcion = pedirOpcion();
        }

        if(opcion == 1){
            jugador.descartarFicha(fichaTomada);
            mesa.fichaAlPozo(fichaTomada);
            Ficha fichaNueva = mesa.robarDePila();
            jugador.tomarFicha(fichaNueva);
            System.out.println("Se levanto un " + fichaNueva);
        }
        primerTurno = false;
    }

    private void mostrarOpciones(){
        System.out.println("\n==== Seleccione una opcion ====");
        System.out.println("1 -> Bajar jugada del atril <-");
        System.out.println("2 -> Agregar ficha a jugada <-");
        System.out.println("3 -> Mostrar jugadas <-");
        System.out.println("4 -> Descartar ficha (termina turno) <-");
        System.out.println("5 -> prueba fin partida");
        System.out.println("6 -> prueba validacion canastas");
        System.out.println("7 -> prueba agregar fichas");
        System.out.print("-> ");
    }

    public static void continuar(){
        System.out.println("\n== Presione ENTER para continuar... ==");
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
    }

    private void limpiar() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    private int pedirOpcion(){
        Scanner scan = new Scanner(System.in);
        int opcion;
        while(true){
            String num = scan.nextLine();
            num = num.strip();
            if(num.matches("\\d+")){
                opcion = Integer.parseInt(num);
                return opcion;
            } else {
                System.out.print("Opcion invalida! -> ");
            }
        }
    }
}