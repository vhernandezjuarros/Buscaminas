package com.vanesaDam.vanher_buscaminas.modelo;

/**
 * Clase que representa un personaje del juego.
 * Cada personaje tiene un nombre y un identificador de imagen asociado.
 * Se utiliza para mostrar los personajes en la interfaz y asignarlos al juego.
 *
 * @author Vanesa Hernández Juarros
 */
public class Personaje {
    private String nombre;
    private int imagenId;

    /**
     * Constructor de la clase Personaje.
     * @param nombre Nombre del personaje
     * @param imagenId ID del recurso de imagen asociado
     */
    public Personaje(String nombre, int imagenId) {
        this.nombre = nombre;
        this.imagenId = imagenId;
    }

    // ----- GETTERS Y SETTERS -----

    public String getNombre() {
        return nombre;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }
}
