import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //dibujamos los mapas
        String[][] mapa1 = {
                {"🚢", "🚢", "🚢", "🌊"},
                {"🌊", "🌊", "🌊", "🌊"},
                {"🌊", "🚢", "🚢", "🚢"},
                {"🌊", "🌊", "🌊", "🌊"}
        };
        String[][] mapa2 = {
                {"🌊", "🚢", "🌊", "🌊"},
                {"🌊", "🚢", "🌊", "🚢"},
                {"🌊", "🚢", "🌊", "🚢"},
                {"🌊", "🌊", "🌊", "🚢"}
        };
        String[][] mapa;

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        int coordenadaX, coordenadaY;

        int jActivo = 2;
        while (!salir) {
            //cambia de jugador al volver a entrar en el bucle
            if (jActivo == 1){
                jActivo = 2;
                mapa = mapa1;
                System.out.println("Mapa a destruir: 1");
            }
            else {
                jActivo = 1;
                mapa = mapa2;
                System.out.println("Mapa a destruir: 2");
            }
            System.out.println("=== Bienvenido a hundir la flota! ===");
            System.out.println("Si quieres terminar el juego, introduce el número 999999");


            //Imprimimos posiciones del mapa
            System.out.println("Las '⭕' son las posiciones que aún no han sido disparadas:");
            for (int i = 0; i < mapa2.length; i++) {
                for (int j = 0; j < mapa2[i].length; j++) {
                    if (mapa[i][j]=="🌊"||mapa[i][j]=="🚢"){
                        System.out.print("⭕");
                    }
                    else {
                        System.out.print(mapa[i][j]);
                    }
                }
                System.out.println();
            }

            // Pedimos las coordenadas
            System.out.println("Introduce el número de fila: ");
            coordenadaX = scanner.nextInt()-1;
            // En el escaner le restamos uno y así es más intiutivo para el jugador decir la fila que quiere
            // atacar ya que si quiere atacar la primera fila, debería poner 0 y al tener puesto aquí el -1
            // puede poner el nº1 directamente

            System.out.println("Introduce el número de columna: ");
            coordenadaY = scanner.nextInt()-1;

            // Comprobamos si el usuario quiere salir
            if (coordenadaX == 999999) {
                salir = true;
                System.out.println("Gracias por jugar. ¡Hasta la próxima!");
                break;
            }

            // Verificamos si las coordenadas están dentro del rango
            if (coordenadaX < 0 || coordenadaX >= mapa.length || coordenadaY < 0 || coordenadaY >= mapa[0].length) {
                System.out.println("Coordenadas fuera de rango. Intenta de nuevo.");
                continue;
            }

            // Verificamos si hay un barco en la posición elegida
            if (mapa[coordenadaX][coordenadaY].equals("🚢")) {
                // Marcamos el barco tocado o hundido
                mapa[coordenadaX][coordenadaY] = "🧨";
                //comprobamos si el barco está hundido o tocado
                if (esHundido(mapa, coordenadaX, coordenadaY)==true||
                    esHundido(mapa, coordenadaX-1, coordenadaY)==true||
                    esHundido(mapa, coordenadaX-2, coordenadaY)==true||
                    esHundido(mapa, coordenadaX, coordenadaY-1)==true||
                    esHundido(mapa, coordenadaX, coordenadaY-2)==true) {
                    System.out.println("HUNDIDO");
                }
                else {
                    System.out.println("TOCADO");
                }
            } else if (mapa[coordenadaX][coordenadaY].equals("🌊")) {
                System.out.println("Fallaste. Agua.");
                mapa[coordenadaX][coordenadaY] = "💧";  // Marcamos como agua disparada
            } else {
                System.out.println("Ya disparaste aquí. Vuelve a intentarlo en tu siguiente turno.");
            }
            // Verificamos si hay barcos antes de pedir coordenadas
            if (!hayBarco(mapa)) {
                System.out.println("¡Has hundido todos los barcos! Fin del juego.");
                salir = true;
            }
        }
        scanner.close();
        //al acabar el bucle se imprimen los dos mapas
        System.out.println("MAPA 1");
        for (int i = 0; i < mapa1.length; i++) {
            for (int j = 0; j < mapa1[i].length; j++) {
                System.out.print(mapa1[i][j]);
            }
            System.out.println();
        }
        System.out.println("MAPA 2");
        for (int i = 0; i < mapa2.length; i++) {
            for (int j = 0; j < mapa2[i].length; j++) {
                System.out.print(mapa2[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Método para revisar si el barco tocado es tocado o hundido
     * @param mapa
     * @param coordenadaX
     * @param coordenadaY
     * @return
     */
    public  static boolean esHundido(String[][] mapa, int coordenadaX, int coordenadaY ) {

        //2 hacemos lo del [i][j] 🧨 && [i+1][j] 🧨 && [i+2][j] 🧨
        // si es así, se devuelve true, sino se devuelve siempre fasle.
        int mapaSize = mapa.length;
        //1º comprobar coordenadas i, i+1 e i+2 son dentro del mapa tanto verticalmente como horizontalmente
        if (coordenadaY < mapaSize && coordenadaX < mapaSize &&
                coordenadaY +1 < mapaSize && coordenadaX +1 < mapaSize &&
                coordenadaY +2 < mapaSize && coordenadaX+2 < mapaSize &&
                coordenadaY >=0 && coordenadaX >=0){
            //miramos si está hundido en vertical o horizontal
            if( mapa[coordenadaX][coordenadaY].equals("\uD83E\uDDE8") &&
                    mapa[coordenadaX][coordenadaY+1].equals("\uD83E\uDDE8")  &&
                    mapa[coordenadaX][coordenadaY+2].equals("\uD83E\uDDE8"))  {
                return true;
            }
            else if ( mapa[coordenadaX][coordenadaY].equals("\uD83E\uDDE8") &&
                    mapa[coordenadaX+1][coordenadaY].equals("\uD83E\uDDE8")  &&
                    mapa[coordenadaX+2][coordenadaY].equals("\uD83E\uDDE8"))  {
                return true;
            }
            else {
                return false;
            }
        }
        return false;

    }





    /**
     * Método para verificar si aún quedan barcos en el mapa
     * @param mapa
     * @return
     */
    public static boolean hayBarco(String[][] mapa) {
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                if (mapa[i][j].equals("🚢")) {
                    return true;  // Si encontramos un barco, retornamos true
                }
            }
        }
        return false;  // Si no hay barcos, retornamos false
    }

}