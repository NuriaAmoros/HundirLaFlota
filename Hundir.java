import java.util.Scanner;

public class Hundir {
    public static void main(String[] args) {
        String[][] tableroJ1 = {
                {"🌊", "🌊", "🌊", "🌊"},
                {"🌊", "🌊", "🌊", "🌊"},
                {"🌊", "🚢", "🚢", "🚢"},
                {"🚢", "🌊", "🌊", "🌊"}
        };

        String[][] tableroJ2 = {
                {"🚢", "🌊", "🌊", "🌊"},
                {"🌊", "🌊", "🚢", "🌊"},
                {"🌊", "🌊", "🚢", "🌊"},
                {"🌊", "🌊", "🚢", "🌊"}
        };

        Scanner entrada = new Scanner(System.in);
        boolean salirDelJuego = false;

        while (!salirDelJuego) {
            System.out.println("==Bienvenido jugadores a Hundir la flota==");
            System.out.println("Selecciona una opcion del menú para jugar");
            System.out.println("1. Empezar el juego");
            System.out.println("2. Salir");

            String opcionLeida = entrada.nextLine();
            int opcion = Integer.parseInt(opcionLeida);

            if (opcion == 1) {
                ejecutarJuego(entrada, tableroJ1, tableroJ2);
            } else if (opcion == 2) {
                salirDelJuego = true;
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void ejecutarJuego(Scanner entrada, String[][] tableroJ1, String[][] tableroJ2) {
        int fila, columna;
        int jugador = 1;
        boolean finDePartida = false;

        while (!finDePartida) {
            System.out.println("==TURNO DEL JUGADOR " + jugador + " ==");
            System.out.println("Si escribe 'abandono' termina el juego");

            String[][] tableroRival = (jugador == 1) ? tableroJ2 : tableroJ1;
            boolean[][] casillasVisitadas = new boolean[tableroRival.length][tableroRival[0].length];

            if (!hayBarcos(tableroRival)) {
                System.out.println("El jugador " + jugador + " ha ganado el juego");
                System.out.println("Porque ha hundido todos los barcos");
                System.out.println("Fin del juego");
                finDePartida = true;
                continue;
            }

            System.out.println("Jugador " + jugador + ", introduce el número de fila: ");
            String entradaFila = entrada.nextLine();
            if (entradaFila.equals("abandono")) {
                System.out.println("Gracias por jugar. ¡Hasta la próxima!");
                break;
            }
            fila = Integer.parseInt(entradaFila);

            System.out.println("Jugador " + jugador + ", introduce el número de columna: ");
            String entradaColumna = entrada.nextLine();
            if (entradaColumna.equals("abandono")) {
                System.out.println("Gracias por jugar. ¡Hasta la próxima!");
                break;
            }
            columna = Integer.parseInt(entradaColumna);

            if (fila < 0 || fila >= tableroRival.length || columna < 0 || columna >= tableroRival[0].length) {
                System.out.println("Coordenadas fuera del rango");
                continue;
            }

            if (tableroRival[fila][columna].equals("🚢")) {
                System.out.println("Tocado!!!");
                tableroRival[fila][columna] = "D";

                if (verificarBarcoHundido(tableroRival, fila, columna, new boolean[tableroRival.length][tableroRival[0].length])) {
                    System.out.println("¡Hundiste un barco!");
                }

                if (!hayBarcos(tableroRival)) {
                    System.out.println("El Jugador " + jugador + " ha ganado esta partida");
                    System.out.println("Ya que ha destruido todos los barcos");
                    mostrarTableros(tableroJ1, tableroJ2);
                    finDePartida = true;
                    continue;
                }
            } else if (tableroRival[fila][columna].equals("🌊")) {
                System.out.println("Fallaste, tocaste Agua!!!");
                tableroRival[fila][columna] = "A";
            } else {
                System.out.println("Ya disparaste aquí. Intenta otra coordenada");
            }

            jugador = (jugador == 1) ? 2 : 1;
        }
    }

    public static boolean hayBarcos(String[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j].equals("🚢")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void mostrarTablero(String[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void mostrarTableros(String[][] tablero1, String[][] tablero2) {
        System.out.println("Tablero del jugador 1");
        mostrarTablero(tablero1);
        System.out.println("Tablero del jugador 2");
        mostrarTablero(tablero2);
    }

    public static boolean verificarBarcoHundido(String[][] tablero, int x, int y, boolean[][] visitados) {
        if (x < 0 || x >= tablero.length || y < 0 || y >= tablero[0].length || tablero[x][y].equals("🌊")) {
            return true;
        }
        if (visitados[x][y]) {
            return true;
        }
        if (tablero[x][y].equals("🚢")) {
            return false;
        }

        visitados[x][y] = true;

        boolean arriba = verificarBarcoHundido(tablero, x - 1, y, visitados);
        boolean abajo = verificarBarcoHundido(tablero, x + 1, y, visitados);
        boolean izquierda = verificarBarcoHundido(tablero, x, y - 1, visitados);
        boolean derecha = verificarBarcoHundido(tablero, x, y + 1, visitados);

        return arriba && abajo && izquierda && derecha;
    }
}
