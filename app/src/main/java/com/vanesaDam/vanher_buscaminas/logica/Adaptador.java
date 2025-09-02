package com.vanesaDam.vanher_buscaminas.logica;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vanesaDam.vanher_buscaminas.R;
import com.vanesaDam.vanher_buscaminas.modelo.Personaje;

import java.util.List;

/**
 * Adaptador personalizado para mostrar objetos Personaje en un Spinner.
 * Cada fila muestra la imagen y el nombre del personaje.
 *
 * @author Vanesa Hernández Juarros
 */
public class Adaptador extends ArrayAdapter<Personaje> {

    private final LayoutInflater inflater;

    /**
     * Constructor del adaptador.
     *
     * @param context    Contexto de la actividad
     * @param personajes Lista de personajes a mostrar
     */
    public Adaptador(Context context, List<Personaje> personajes) {
        super(context, 0, personajes);
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Devuelve la vista que se muestra en el Spinner cuando está desplegado.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearFila(position, convertView, parent);
    }

    /**
     * Devuelve la vista de cada elemento cuando se despliega el Spinner
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearFila(position, convertView, parent);
    }

    /**
     * Crea la vista de una fila del Spinner.
     *
     * @param position    Posición del elemento en la lista
     * @param convertView Vista reciclada, si existe
     * @param parent      Vista padre
     * @return Vista personalizada con imagen y nombre del personaje
     */
    private View crearFila(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // Infla la vista si no existe
        if (view == null) {
            view = inflater.inflate(R.layout.item_personaje, parent, false);
        }

        // Obtiene referencias a las vistas
        ImageView imageView = view.findViewById(R.id.imageViewPersonaje);
        TextView textView = view.findViewById(R.id.textViewNombre);

        // Configura los datos del personaje
        Personaje personaje = getItem(position);
        if (personaje != null) {
            imageView.setImageResource(personaje.getImagenId());
            textView.setText(personaje.getNombre());
        }

        return view;
    }
}