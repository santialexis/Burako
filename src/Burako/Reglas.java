package Burako;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Reglas {

    /* VALIDACION DE JUGADAS */
    public static boolean jugadaValida(Jugada jugada){
        if(jugada.getFichas().size() < 3){
            return false;
        }

        return esEscalera(jugada) || esPierna(jugada);
    }


    public static boolean esEscalera(Jugada jugada){
        List<Ficha> fichasComunes = jugada.getFichas().stream().filter(f->!f.esComodin()).toList();
        int comodines = (int) jugada.getFichas().stream().filter(f->f.esComodin()).count();

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
        return huecos <= comodines;
    }


    public static boolean esPierna(Jugada jugada){
        List<Ficha> fichasComunes = jugada.getFichas().stream().filter(f->!f.esComodin()).toList();

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


    public static boolean validarAgregarFicha(Ficha ficha, Jugada jugada, Jugador jugador){
        List<Ficha> copia = new ArrayList<>(jugada.getFichas());
        copia.add(ficha);
        Jugada temp = new Jugada(copia);

        return !jugadaValida(temp) || (!puedeCortar(jugador) && jugador.getAtril().size() <= 1);
    }


    public static boolean esCanastaPura(Jugada jugada){
        List<Ficha> fichasJugada = jugada.getFichas();
        if(esEscalera(jugada)){
            int min = fichasJugada.get(0).getNumero();
            int max = fichasJugada.get(fichasJugada.size()-1).getNumero();
            Color colorEscalera = fichasJugada.get(0).getColor();

            List<Integer> escalera = new ArrayList<>();
            for(int i=min; i <= max; i++){
                escalera.add(i);
            }

            for(int j=0; j < fichasJugada.size(); j++){
                Ficha f = fichasJugada.get(j);
                if(f.getNumero() != escalera.get(j)){
                    return false;
                }
                if(f.getNumero() == 2 && !f.getColor().equals(colorEscalera)){
                    return false;
                }
            }
        } else {
            for(Ficha f : fichasJugada){
                if(f.esComodin()){
                    return false;
                }
            }
        }
        return true;
    }


    /* CALCULO DE PUNTOS */
    public static int calcularPuntos(Jugador jugador){
        int nuevoPuntaje = jugador.getPuntaje();

        nuevoPuntaje -= sumarPuntosFichas(jugador.getAtril()); //restar puntos del atril

        if(!jugador.tomoElMuerto()){
            nuevoPuntaje -= 100; //-100 puntos por no tomar el muerto
        }

        int puntosJugadas = 0; //puntos de todas las jugadas (tanto fichas como canastas)
        for(Jugada j : jugador.getJugadasEnMesa()){
            puntosJugadas += sumarPuntosFichas(j.getFichas()); //puntos de las fichas
            if(j.esCanasta()){
                if(esCanastaPura(j)){
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

        if(nuevoPuntaje < 0){
            nuevoPuntaje = 0;
        }
        return nuevoPuntaje;
    }

    private static int sumarPuntosFichas(List<Ficha> fichas){
        return fichas.stream().mapToInt(Ficha::getPuntos).sum();
    }


    /* VALIDACION DE CORTE */
    public static boolean puedeCortar(Jugador jugador){
        if(!jugador.tomoElMuerto()){
            return true;
        } else {
            return jugador.atrilVacio() && jugador.hizoCanasta();
        }
    }


    /* REPARTIR FICHAS */
    public static void repartirFichas(List<Jugador> jugadores, Mesa mesa){
        List<Ficha> fichasTodas = crearFichas();
        Collections.shuffle(fichasTodas);

        for(Jugador j : jugadores){
            //repartir muertos
            List<Ficha> fichasMuerto = new ArrayList<>();
            for(int m=0; m < 11; m++){
                fichasMuerto.add((fichasTodas.remove(0)));
            }
            j.setMuerto(fichasMuerto);
            //repartir atril
            List<Ficha> fichasIniciales = new ArrayList<>();
            for(int a=0; a < 12; a++){
                fichasIniciales.add(fichasTodas.remove(0));
            }
            j.setAtril(fichasIniciales);
        }
        mesa.setPila(fichasTodas);
    }

    private static List<Ficha> crearFichas() {
        List<Ficha> fichasJuego = new ArrayList<>();

        for (int j = 0; j < 2; j++){
            for (Color col : Color.values()){
                if(col.equals(Color.NINGUNO)){continue;}
                for (int num = 1; num <= 13; num++){
                    fichasJuego.add(new Ficha(num,col));
                }
            }
            fichasJuego.add(new Ficha(0, Color.NINGUNO));
        }
        return fichasJuego;
    }
}

/*      CODIGO PARA HACER PRUEBAS DE VALIDACION (PEGAR EN PARTIDA)
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
 */