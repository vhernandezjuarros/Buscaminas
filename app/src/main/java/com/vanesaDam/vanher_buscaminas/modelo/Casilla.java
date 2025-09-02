package com.vanesaDam.vanher_buscaminas.modelo;

/**
 * Clase que representa una casilla del tablero del juego Buscaminas.
 * Cada casilla puede contener una mina y sabe cuántas minas hay alrededor.
 *
 * @author Vanesa Hernández Juarros
 */
public class Casilla {
    boolean mina; // Indica si la casilla contiene una mina o no
    int minasCerca;// Número de minas alrededor de esta casilla

    /**
     * Constructor por defecto.
     * Inicializa la casilla sin mina y con 0 minas cercanas.
     * Por defecto la casilla estará oculta
     */
    public Casilla() {
        this.mina = false;
        this.minasCerca = 0;
    }

    // ----- GETTERS Y SETTERS -----

    public boolean isMina() {
        return mina;
    }

    public void setMina(boolean mina) {
        this.mina = mina;
    }

    public int getMinasCerca() {
        return minasCerca;
    }

    public void setMinasCerca(int minasCerca) {
        this.minasCerca = minasCerca;
    }
}
