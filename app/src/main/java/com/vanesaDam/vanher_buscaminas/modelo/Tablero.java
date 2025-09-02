package com.vanesaDam.vanher_buscaminas.modelo;

/**
 * Clase que representa el tablero de juego.
 * Contiene las casillas, el número de filas y columnas, y la gestión de minas.
 * Se encarga de inicializar el tablero, colocar minas y calcular el número de minas cercanas.
 *
 * @author Vanesa Hernández Juarros
 */
public class Tablero {
    private Casilla[][] casillas; // Casillas del tablero
    int nf; // filas
    int nc; // columnas
    private int minas;

    /**
     * Constructor por defecto.
     * Crea un tablero de nivel principiante: 8x8 con 10 minas.
     */
    public Tablero() {
        this.nf = 8;
        this.nc = 8;
        this.minas = 10;
    }


    // ----- GETTERS Y SETTERS -----

    public int getNf() {
        return nf;
    }

    public void setNf(int nf) {
        this.nf = nf;
    }

    public int getNc() {
        return nc;
    }

    public void setNc(int nc) {
        this.nc = nc;
    }

    public Casilla[][] getCasillas() {

        return casillas;
    }

    public void setCasillas(Casilla[][] casillas) {
        this.casillas = casillas;
    }

    public int getMinas() {
        return minas;
    }

    public void setMinas(int minas) {
        this.minas = minas;
    }

    // ----- MÉTODOS DE INICIALIZACIÓN Y GESTIÓN DEL TABLERO -----

    /**
     * Inicializa el tablero con casillas vacías, coloca minas aleatoriamente
     * y calcula el número de minas alrededor de cada casilla.
     */
    public void inicializar() { // Rellena el tablero de casillas, minas y números de minas cercanas

        // Rellena el tablero con casillas sin minas
        if (this.casillas == null) {
            this.casillas = new Casilla[nf][nc];
        }

        // Crear las casillas vacías
        for (int i = 0; i < nf; i++) {
            for (int j = 0; j < nc; j++) {
                casillas[i][j] = new Casilla();
            }
        }
        colocaMinas(); // Coloca las minas en el tablero aleatoriamente
        ponerNumMinasCercanas(); // Calcula el número de minas cerca de la casilla (número)
    }

    /**
     * Coloca aleatoriamente las minas en el tablero.
     * Se asegura de no colocar más de una mina en la misma casilla.
     */
    public void colocaMinas() {

        // Inicialmente no hay minas
        int minasColocadas = 0;

        while (minasColocadas < minas) {
            int x = (int) (Math.random() * nf); // Una posición aleatoria en la fila
            int y = (int) (Math.random() * nc); // Una posición aleatoria en la columna

            // Si la casilla tiene mina isMina será false y si no tiene mina isMina será true
            // Si no tiene mina, colocamos una mina en esa posición
            if (!casillas[x][y].isMina()) {
                casillas[x][y].setMina(true);
                minasColocadas++; // Contador de minas colocadas

            }
        }
    }

    /**
     * Calcula el número de minas alrededor de cada casilla.
     * Solo se calcula para casillas que no contienen mina.
     */
    public void ponerNumMinasCercanas() {
        int filas = nf;
        int columnas = nc;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!casillas[i][j].isMina()) { // si la casilla no es una mina, contamos las minas alrededor
                    int numMinasCerca = contarMinasAlrededor(i, j, filas, columnas);
                    casillas[i][j].setMinasCerca(numMinasCerca);
                }
            }
        }
    }

    /**
     * Cuenta el número de minas alrededor de una casilla específica.
     *
     * @param x        fila de la casilla
     * @param y        columna de la casilla
     * @param filas    total de filas del tablero
     * @param columnas total de columnas del tablero
     * @return número de minas cercanas
     */
    public int contarMinasAlrededor(int x, int y, int filas, int columnas) {
        int contadorMC = 0;

        // Recorre las casillas para cada casilla en la fila anterior y siguiente y cada columna anterior y siguiente
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                // si no se sale del tablero y la casilla tiene mina sumamos 1
                if (nx >= 0 && ny >= 0 && nx < filas && ny < columnas && casillas[nx][ny].isMina()) {
                    contadorMC++;
                }
            }
        }
        return contadorMC;
    }
}
