import java.util.Scanner;

public class Hundir {
    public static void main(String[] args) {
        String[][] tableroJugador1 = {
                {"🌊", "🌊", "🌊", "🌊"},
                {"🌊", "🌊", "🌊", "🌊"},
                {"🌊", "🚢", "🚢", "🚢"},
                {"🚢", "🌊", "🌊", "🌊"}
        };

        String[][] tableroJugador2 = {
                {"🚢", "🌊", "🌊", "🌊"},
                {"🌊", "🌊", "🚢", "🌊"},
                {"🌊", "🌊", "🚢", "🌊"},
                {"🌊", "🌊", "🚢", "🌊"}
        };

        Scanner lectorEntrada = new Scanner(System.in);
        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            System.out.println("==Bienvenido jugadores a Hundir la flota==");
            System.out.println("Selecciona una opción del menú:");
            System.out.println("1. Empezar el juego");
            System.out.println("2. Salir");

            int opcion = Integer.parseInt(lectorEntrada.nextLine());

            if (opcion == 1) {
                ejecutarJuego(lectorEntrada, tableroJugador1, tableroJugador2);
            } else if (opcion == 2) {
                juegoTerminado = true;
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void ejecutarJuego(Scanner lectorEntrada, String[][] tableroJugador1, String[][] tableroJugador2) {
        int numeroJugadorActual = 1;
        boolean partidaTerminada = false;

        while (!partidaTerminada) {
            System.out.println("== TURNO DEL JUGADOR " + numeroJugadorActual + " ==");
            System.out.println("Escribe 'abandono' para terminar el juego");

            String[][] tableroOponente = (numeroJugadorActual == 1) ? tableroJugador2 : tableroJugador1;

            if (!hayBarcosRestantes(tableroOponente)) {
                System.out.println("¡El jugador " + numeroJugadorActual + " ha ganado!");
                mostrarTableros(tableroJugador1, tableroJugador2);
                break;
            }

            int fila = pedirCoordenada(lectorEntrada, "fila", numeroJugadorActual);
            int columna = pedirCoordenada(lectorEntrada, "columna", numeroJugadorActual);

            if (fila < 0 || fila >= tableroOponente.length || columna < 0 || columna >= tableroOponente[0].length) {
                System.out.println("Coordenadas fuera del rango. Intente de nuevo.");
                continue;
            }

            switch (tableroOponente[fila][columna]) {
                case "🚢":
                    System.out.println("¡Tocado!");
                    tableroOponente[fila][columna] = "D";

                    if (verificarBarcoHundido(tableroOponente, fila, columna, new boolean[tableroOponente.length][tableroOponente[0].length])) {
                        System.out.println("¡Hundiste un barco!");
                    }

                    if (!hayBarcosRestantes(tableroOponente)) {
                        System.out.println("¡El jugador " + numeroJugadorActual + " ha ganado la partida!");
                        mostrarTableros(tableroJugador1, tableroJugador2);
                        partidaTerminada = true;
                    }
                    break;

                case "🌊":
                    System.out.println("Fallaste. Agua.");
                    tableroOponente[fila][columna] = "A";
                    break;

                default:
                    System.out.println("Ya disparaste aquí. Intenta otra coordenada.");
                    break;
            }

            numeroJugadorActual = (numeroJugadorActual == 1) ? 2 : 1;
        }
    }

    private static int pedirCoordenada(Scanner lectorEntrada, String tipo, int jugador) {
        System.out.println("Jugador " + jugador + ", introduce el número de " + tipo + ": ");
        String entrada = lectorEntrada.nextLine();

        if (entrada.equalsIgnoreCase("abandono")) {
            System.out.println("Gracias por jugar. ¡Hasta la próxima!");
            System.exit(0);
        }

        return Integer.parseInt(entrada);
    }

    public static boolean hayBarcosRestantes(String[][] tablero) {
        for (String[] fila : tablero) {
            for (String celda : fila) {
                if (celda.equals("🚢")) return true;
            }
        }
        return false;
    }

    public static void mostrarTablero(String[][] tablero) {
        for (String[] fila : tablero) {
            for (String celda : fila) {
                System.out.print(celda + " ");
            }
            System.out.println();
        }
    }

    public static void mostrarTableros(String[][] tablero1, String[][] tablero2) {
        System.out.println("Tablero del jugador 1:");
        mostrarTablero(tablero1);
        System.out.println("Tablero del jugador 2:");
        mostrarTablero(tablero2);
    }

    public static boolean verificarBarcoHundido(String[][] tablero, int x, int y, boolean[][] visitados) {
        if (x < 0 || x >= tablero.length || y < 0 || y >= tablero[0].length || tablero[x][y].equals("🌊")) {
            return true;
        }

        if (visitados[x][y]) return true;
        if (tablero[x][y].equals("🚢")) return false;

        visitados[x][y] = true;

        return verificarBarcoHundido(tablero, x - 1, y, visitados)
                && verificarBarcoHundido(tablero, x + 1, y, visitados)
                && verificarBarcoHundido(tablero, x, y - 1, visitados)
                && verificarBarcoHundido(tablero, x, y + 1, visitados);
    }
}
