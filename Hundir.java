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
        //
        Scanner scanner = new Scanner(System.in);

        // Controla si el usuario ha elegido salir del juego
        boolean salir = false;
        
        while (!salir) {
            System.out.println("==Bienvenido jugadores a Hundir la flota==");
            System.out.println("Selecciona una opcion del menú para jugar");
            System.out.println("1. Empezar el juego");
            System.out.println("2. Salir");

            String opcionStr = scanner.nextLine();
            int opcion;
            opcion = Integer.parseInt(opcionStr);


            if (opcion == 1) {

                ejecucionDelJuego(scanner, tableroJugador1, tableroJugador2);
            } else if (opcion == 2) {
                salir = true;
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

    }
    
    public static void ejecucionDelJuego(Scanner scanner, String[][] tableroJugador1, String[][] tableroJugador2) {





        int coordenadaX, coordenadaY;
        int jugadorActual = 1;
        boolean salir = false;
        
        while (!salir) {
            System.out.println("==TURNO DEL JUGADOR " + jugadorActual + " ==");
            System.out.println("Si escribe 'abandono' termina el juego");
            
            //Determinar el tablero del oponente
            String[][] tableroOponente = (jugadorActual == 1) ? tableroJugador2 : tableroJugador1;
            boolean[][] visitado = new boolean[tableroOponente.length][tableroOponente[0].length];
            //Verificar si aun hay barcos sin destruir en el tablero oponente
            if(!hayBarcosTodavia(tableroOponente)){
                System.out.println("El jugador Actual ha ganado el juego " + jugadorActual);
                System.out.println("Porque ha hundido todos los barcos" );
                System.out.println("Fin del juego");
                salir = true;
                continue;
            }
            
            //Pedir las coordenadas
            //COORDENADA X
            System.out.println("Jugador " + jugadorActual + ", introduce el número de fila: ");
            String inputFila = scanner.nextLine();//""

            if (inputFila.equals("abandono")) {
                System.out.println("Gracias por jugar. ¡Hasta la próxima!");
                break;
            }

            coordenadaX = Integer.parseInt(inputFila);

            System.out.println("Jugador " + jugadorActual + ", introduce el número de columna: ");
            String inputColumna = scanner.nextLine();

            if (inputColumna.equals("abandono")) {
                System.out.println("Gracias por jugar. ¡Hasta la próxima!");
                break;
            }

            coordenadaY = Integer.parseInt(inputColumna);
            
            //Validar que el usuario no ingrese coordenadas que no existen
            if(coordenadaX < 0 || coordenadaX >= tableroOponente.length  || coordenadaY < 0 || coordenadaY >= tableroOponente[0].length){
                System.out.println("Coordenadas fuera del rango");
                continue;
            }
            
            
            
            if (tableroOponente[coordenadaX][coordenadaY].equals("🚢")) {
                System.out.println("Tocado!!!");
                tableroOponente[coordenadaX][coordenadaY] = "D";

                // Verificar si todas las partes conectadas del barco están destruidas
                if (verificarBarcoHundido(tableroOponente, coordenadaX, coordenadaY, new boolean[tableroOponente.length][tableroOponente[0].length])) {
                    System.out.println("¡Hundiste un barco!");
                }
                //Contar Partes de los barcos en general en toda la matriz
                if (!hayBarcosTodavia(tableroOponente)) {
                    System.out.println("El Jugador " + jugadorActual + "Ha ganado esta partida");
                    System.out.println("Ya que ha destruido todos los barcos");
                    mostrarTableros(tableroJugador1, tableroJugador2);
                    salir = true;
                    continue;
                }
            
            }else if(tableroOponente[coordenadaX][coordenadaY].equals("🌊")){
                System.out.println("Fallastes tocaste Agua!!!");
                tableroOponente[coordenadaX][coordenadaY] = "A";
            
            }else{ //Toca una gota de agua o dinamita YA FUE DISPARADA EN ESA POSICION
                System.out.println("Ya disparastes aqui. Intenta otra coordenada");
            }
            
            
            
            
            //Cambiar el jugador al final del while CUANDO TERMINA EL TURNO
            jugadorActual = (jugadorActual == 1) ? 2 : 1;
            

        }
    }
    
    
    
    public static boolean hayBarcosTodavia(String[][] tableroOponente){
        for(int i = 0; i < tableroOponente.length; i++){ //Filas
            for(int j = 0; j < tableroOponente[0].length; j++){ //Columnas
                if(tableroOponente[i][j].equals("🚢")){
                    return true;
                }
            }
        }
        
        return false; //No quedan barcos
    }
    
    public static void mostrarTablero(String[][] tablero){
        for(int i = 0; i < tablero.length; i++){ //Filas
            for(int j = 0; j < tablero[0].length; j++){ //Columnas
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void mostrarTableros(String[][] tablero1,String[][] tablero2){
        System.out.println("Tablero del jugador 1");
        mostrarTablero(tablero1);
        System.out.println("Tablero del jugador 2");
        mostrarTablero(tablero2);
    }
    
   //Aplicando recursividad verificamos los lados adyacentes del barco que fue tocado inicialmente
    //y de esa manera confirmamos si ya fue hundido o si aun tiene partes
    public static boolean verificarBarcoHundido(String[][] tablero, int x, int y, boolean[][] visitados) {
        // Si está fuera de límites o es agua, consideramos que esta parte no afecta el hundimiento
        if (x < 0 || x >= tablero.length || y < 0 || y >= tablero[0].length || tablero[x][y].equals("🌊")) {
            return true;
        }
        // Si la posición ya fue visitada, no la volvemos a verificar
        if (visitados[x][y]) {
            return true;
        }
        // Si encontramos una parte del barco no destruida, significa que el barco aún no está hundido
        if (tablero[x][y].equals("🚢")) {
            return false;
        }

        // Marcar la posición actual como visitada
        visitados[x][y] = true;

        // Comprobar todas las direcciones; el barco está hundido solo si todas las partes conectadas están destruidas
        boolean arriba = verificarBarcoHundido(tablero, x - 1, y, visitados);
        boolean abajo = verificarBarcoHundido(tablero, x + 1, y, visitados);
        boolean izquierda = verificarBarcoHundido(tablero, x, y - 1, visitados);
        boolean derecha = verificarBarcoHundido(tablero, x, y + 1, visitados);

        return arriba && abajo && izquierda && derecha;
    }

}