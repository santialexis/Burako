package Burako;

import java.util.*;

public class Partida {
    private List<Jugador> jugadores;
    private Mesa mesa;
    private int turno;

    public Partida(List<Jugador> jugadores){
        this.jugadores = jugadores;
        this.mesa = new Mesa();
        this.turno = turnoInicial(jugadores);
        mesa.repartirFichas(jugadores);
    }

    private int turnoInicial(List<Jugador> jugadores){
        Random rand = new Random();
        int mayorNumero = -1;
        Jugador jugadorInicial = null;

        for(Jugador j : jugadores){
            int num = rand.nextInt(1,7);
            System.out.print("\n->" + j.getNombre() + " tiro el dado y saco un " + num + "! <-");
            if(num > mayorNumero){
                mayorNumero = num;
                jugadorInicial = j;
            }
        }
        System.out.println("\n-> Comienza " + jugadorInicial.getNombre() + " <-");
        return jugadores.indexOf(jugadorInicial);
    }

    public void iniciarPartida(){
        while(true){
            Jugador actual = jugadores.get(turno);
            jugarTurno(actual);
            if(actual.atrilVacio() && actual.hizoCanasta()){
                System.out.println("\n==== " + actual.getNombre() + " gano la ronda! ====");
                break;
            }
            if(mesa.pilaVacia() && mesa.pozoVacio()){
                System.out.println("\n==== " + "No quedan mas fichas en la mesa!!!" + " ====");
                break;
            }
            turno = (turno + 1) % jugadores.size();
        }
        for(Jugador jug : jugadores){
            jug.setPuntaje(calcularPuntos(jug));
        }
    }

    private int calcularPuntos(Jugador jugador){
        int nuevoPuntaje = jugador.getPuntaje();

        nuevoPuntaje -= sumarPuntosFichas(jugador.getAtril()); //puntos del atril (restar)

        if(!jugador.tomoElMuerto()){
            nuevoPuntaje -= 100; //puntos por no tomar el muerto (-100)
        }

        int puntosJugadas = 0; //puntos de todas las jugadas (tanto fichas como si son canasta)
        for(Jugada j : jugador.getJugadasEnMesa()){
            puntosJugadas += sumarPuntosFichas(j.getFichas()); //puntos de fichas
            if(j.jugadaCanasta()){
                if(j.canastaPura()){
                    puntosJugadas += 200; //puntos de canasta pura
                } else {
                    puntosJugadas += 100; //puntos de canasta impura
                }
            }
        }

        if(jugador.hizoCanasta()){
            nuevoPuntaje += puntosJugadas;
        } else {
            nuevoPuntaje -= puntosJugadas;
        }

        return nuevoPuntaje;
    }

    private int sumarPuntosFichas(List<Ficha> fichas){
        return fichas.stream().mapToInt(Ficha::getPuntos).sum();
    }

    private void jugarTurno(Jugador jugador){
        System.out.println("\n==== Turno de " + jugador.getNombre() + " ====");

        int opcion;
        boolean descarto = false;
        boolean tomoFicha = false;

        while(!descarto){
            System.out.println("\nATRIL:");
            jugador.mostrarAtril();
            System.out.print("POZO -> ");
            mesa.mostrarPozo();

            mostrarOpciones();
            opcion = pedirOpcion();
            switch (opcion) {
                case 1:
                    if(!tomoFicha){
                        jugador.tomarFicha(mesa.robarDePila());
                        tomoFicha = true;
                    } else {
                        System.out.println("\nYa se levantaron fichas!");
                    }
                    break;
                
                case 2:
                    if(mesa.pozoVacio()){
                        System.out.println("\n-> El pozo no tiene fichas para levantar! <-");
                    }
                    else if(!tomoFicha){
                        jugador.tomarPozo(mesa.robarPozo());
                        tomoFicha = true;
                    } else {
                        System.out.println("\nYa se levantaron fichas!");
                    }
                    break;
                
                case 3:
                    if(tomoFicha){
                        jugador.jugarFichas(armarJugada(jugador));
                        if(jugador.atrilVacio()){
                            jugador.intentarCortar();
                        }
                    } else {
                        System.out.println("\n-> Antes de jugar debe tomar una ficha! <-");
                    }
                    break;

                case 4:
                    if(tomoFicha){
                        Jugada jugada;
                        Ficha ficha;
                        if((jugada = elegirJugada(jugador)) != null && (ficha = elegirFicha(jugador)) != null){
                            jugador.agregarFichaAJugada(ficha,jugada);
                            if(jugador.atrilVacio()){
                                jugador.intentarCortar();
                            }
                        }
                    } else {
                        System.out.println("\n-> Antes de jugar debe tomar una ficha! <-");
                    }
                    break;

                case 5:
                    if(tomoFicha){
                        Ficha fichaDescarte;
                        if((fichaDescarte = elegirFicha(jugador)) != null){
                            mesa.fichaAlPozo(fichaDescarte);
                            jugador.descartarFicha(fichaDescarte);
                            descarto = true;
                            if(jugador.atrilVacio()){
                                jugador.intentarCortar();
                            }
                        }
                    } else {
                        System.out.println("\n-> Antes de descartar debe tomar una ficha! <-");
                    }
                    break;

                default:
                    System.out.println("Opcion invalida! -> ");
                    break;
            }
        }
    }

    private void mostrarOpciones(){
        System.out.println("==== Seleccione una opcion ====");
        System.out.println("1 -> Tomar ficha de la pila <-");
        System.out.println("2 -> Tomar fichas del pozo <-");
        System.out.println("3 -> Bajar jugada del atril <-");
        System.out.println("4 -> Agregar ficha a jugada <-");
        System.out.println("5 -> Descartar ficha <-");
        System.out.print("-> ");
    }

    private int pedirOpcion(){
        Scanner scan = new Scanner(System.in);
        int opcion = scan.nextInt();
        while(opcion < 1 || opcion > 5){
            System.out.println("Opcion invalida! -> ");
            opcion = scan.nextInt();
        }
        return opcion;
    }

    private Jugada armarJugada(Jugador jugador){
        List<Ficha> fichasJugada = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        System.out.println("\n-> Seleccione las fichas de la jugada (numero + color) (\"comodin\" para comodines de 50) <-");
        System.out.println("-> No digite nada para terminar <-");

        while(true){
            jugador.mostrarAtril();

            System.out.print("-> ");
            String ficha = scan.nextLine();

            if(ficha.isEmpty()){
                break;
            }

            Optional<Ficha> fichaEnAtril = jugador.getAtril().stream()
                                            .filter(f -> f.toString().equalsIgnoreCase(ficha))
                                            .findFirst();
            if(fichaEnAtril.isPresent()){
                Ficha fichita = fichaEnAtril.get();
                fichasJugada.add(fichita);
                jugador.getAtril().remove(fichita);
            } else {
                System.out.println("-> La ficha no esta en el atril! <-");
            }
        }

        Jugada jugada = new Jugada(fichasJugada);
        if(!jugada.jugadaValida()){
            System.out.println("-> La jugada no es valida. Se devolveran las fichas al atril <-");
            jugador.getAtril().addAll(fichasJugada);
            fichasJugada.clear();
            return null;
        }
        System.out.println("\n-> Bajando jugada... <-");
        return jugada;
    }

    private Jugada elegirJugada(Jugador jugador){
        Scanner scan = new Scanner(System.in);
        int jugada;
        System.out.println("\nSeleccione una jugada (nro. de jugada)");
        System.out.println("No digite nada para cancelar.");
        jugador.mostrarJugadas();

        while(true){
            String num = scan.nextLine();

            if(num.isEmpty()){
                System.out.println("Cancelando...");
                return null;
            }

            if(num.matches("\\d+")){
                jugada = Integer.parseInt(num);
                if(jugada < 1 || jugada > jugador.getJugadasEnMesa().size()){
                    System.out.println("La jugada no existe!");
                    System.out.print("-> ");
                } else {
                    break;
                }
            } else {
                System.out.println("Jugada no existente!");
                System.out.print("-> ");
            }
        }
        return jugador.getJugadasEnMesa().get(jugada-1);
    }

    private Ficha elegirFicha(Jugador jugador){
        Scanner scan = new Scanner(System.in);
        System.out.println();
        jugador.mostrarAtril();
        System.out.println("Seleccione una ficha (numero + color)");
        System.out.print("No digite nada para cancelar ");

        while(true){
            System.out.print("-> ");
            String ficha = scan.nextLine();

            if(ficha.isEmpty()){
                return null;
            }

            Optional<Ficha> existeFicha = jugador.getAtril().stream()
                    .filter(f -> f.toString().equalsIgnoreCase(ficha))
                    .findFirst();

            if(existeFicha.isPresent()){
                return existeFicha.get();
            } else {
                System.out.print("La ficha no esta en el atril! ");
            }
        }
    }
}