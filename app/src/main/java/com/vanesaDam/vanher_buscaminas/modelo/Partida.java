package com.vanesaDam.vanher_buscaminas.modelo;

import com.vanesaDam.vanher_buscaminas.R;

/**
 * Clase que representa una partida del juego.
 * Contiene la información de la partida actual, como el tablero,
 * los puntos acumulados, el nivel de dificultad y el personaje seleccionado.
 *
 * @author Vanesa Hernández Juarros
 */
public class Partida {

    int puntos;
    private Tablero tablero;
    private int nivel;
    int imgPersonajeId;

    /**
     * Constructor de la clase Partida.
     * Inicializa la partida con un tablero por defecto, 0 puntos, nivel principiante
     * y personaje predeterminado.
     */
    public Partida() {
        this.tablero = new Tablero();
        this.puntos = 0;
        this.nivel = 1;
        this.imgPersonajeId = R.drawable.baseline_catching_pokemon_24;
    }

    // ----- GETTERS Y SETTERS -----

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getImgPersonajeId() {
        return imgPersonajeId;
    }

    public void setImgPersonajeId(int imgPersonajeId) {
        this.imgPersonajeId = imgPersonajeId;
    }

    // ----------- MÉTODOS -----------
    /**
     * Inicia la partida.
     * Crea un tablero nuevo y lo configura según el nivel.
     * Después, inicializa las minas y los números en el tablero.
     */
    public void iniciarJuego() {

        tablero = new Tablero(); // Crea un tablero con las casillas tapadas
        configTableroXNivel(); // Configura filas, columnas y minas según el nivel
        tablero.inicializar(); // Coloca minas y calcula número de minas cercanas
    }

    /**
     * Configura el tablero según el nivel de dificultad.
     * Nivel 1: 8x8 con 10 minas
     * Nivel 2: 12x12 con 30 minas
     * Nivel 3: 16x16 con 60 minas
     */
    public void configTableroXNivel() {
        if (nivel == 1) {
            getTablero().setNf(8);
            getTablero().setNc(8);
            getTablero().setMinas(10);
        } else if (nivel == 2) {
            getTablero().setNf(12);
            getTablero().setNc(12);
            getTablero().setMinas(30);
        } else {
            getTablero().setNf(16);
            getTablero().setNc(16);
            getTablero().setMinas(60);
        }
    }

}

