package com.vanesaDam.vanher_buscaminas;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vanesaDam.vanher_buscaminas.logica.Adaptador;
import com.vanesaDam.vanher_buscaminas.modelo.Casilla;
import com.vanesaDam.vanher_buscaminas.modelo.Partida;
import com.vanesaDam.vanher_buscaminas.modelo.Personaje;
import com.vanesaDam.vanher_buscaminas.modelo.Tablero;

import java.util.ArrayList;

/**
 * Clase principal de la aplicación Buscaminas.<br>
 * Controla la actividad principal, gestiona la interfaz gráfica, el tablero de juego,
 * los menús y las interacciones del usuario.<br>
 * Funcionalidades principales:
 * - Creación dinámica del tablero según nivel de dificultad.
 * - Gestión de clic corto y clic largo en casillas con {@link View.OnClickListener} y {@link View.OnLongClickListener}
 * - Diálogos de instrucciones, configuración y selección de personaje.
 * - Control de puntuación y fin de partida.
 *
 * @author Vanesa Hernández Juarros
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private Partida partida;            // Objeto que representa la partida actual
    private GridLayout tableroView;     // Vista del tablero GridLayout
    private int nivel = 1;              // Nivel de dificultad (1 = principiante, 2 = amateur, 3 = avanzado)
    private boolean jugando = false;    // Indica si la partida está en curso

    // Ajuste de los márgenes para ocupar toda la pantalla (soporte para barras del sistema)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuración de la barra de herramientas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);// Añade la barra de herramientas al Action Bar

        // Inicializamos partida con tablero 8x8 y 10 minas (nivel principiante)
        // El tablero tendrá casillas: vacías, con mina o con número
        partida = new Partida();
        partida.getTablero().inicializar();


        //Crear el tablero GridLayout dinámicamente
        tableroView = new GridLayout(this);// Cream un GridLayout
        tableroView.setRowCount(partida.getTablero().getNf()); // filas
        tableroView.setColumnCount(partida.getTablero().getNc()); // columnas


        ConstraintLayout mainContainer = findViewById(R.id.mainContainer);// Obtiene el contenedor del Grid
        ConstraintLayout.LayoutParams gridLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        tableroView.setLayoutParams(gridLayoutParams);

        // Agrega botones al GridLayout (uno por cada celda)
        for (int i = 0; i < partida.getTablero().getNf(); i++) {
            for (int j = 0; j < partida.getTablero().getNc(); j++) {


                Casilla casilla = partida.getTablero().getCasillas()[i][j];
                // Da tamaño al botón
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Ancho proporcional
                params.height = 0; // Alto proporcional
                params.rowSpec = GridLayout.spec(i, 1, 1f); // 1 celda de alto con peso 1
                params.columnSpec = GridLayout.spec(j, 1, 1f); // 1 celda de ancho con peso 1

                if (!casilla.isMina()) {//Crea un Button si la casilla no tiene mina
                    Button btnCasilla = new Button(this);
                    btnCasilla.setLayoutParams(params);//Damos tamaño al botón
                    btnCasilla.setTag(i + "," + j);  //Guardamos la posición para cuando se haga cli
                    btnCasilla.setOnClickListener(this);
                    btnCasilla.setOnLongClickListener(this);
                    btnCasilla.setEnabled(false);
                    tableroView.addView(btnCasilla);
                } else { // Crea un ImageButton si la casilla tiene una mina
                    ImageButton btnMina = new ImageButton(this);
                    btnMina.setLayoutParams(params); // Da tamaño al botón
                    btnMina.setTag(i + "," + j);  // Guarda la posición para cuando se haga clic
                    btnMina.setAdjustViewBounds(true);
                    btnMina.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    btnMina.setPadding(1, 1, 1, 1);
                    btnMina.setOnClickListener(this);
                    btnMina.setOnLongClickListener(this);
                    btnMina.setEnabled(false);
                    tableroView.addView(btnMina);
                }
            }
        }

        // Añade el GridLayout al contenedor
        mainContainer.addView(tableroView);
    }

    //----- MENU SUPERIOR -----

    /**
     * Infla el menú superior a partir de su recurso XML menu_main (asocia la vista menu)
     * @param menu Menú de opciones a inflar
     *
     * @return true si se ha inflado correctamente, false en caso contrario
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Maneja las acciones del menú superior.
     * @param item elemento del menú seleccionado
     *
     * @return true si se ha procesado correctamente, false en caso contrario
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idAccMenu = item.getItemId();
        // Detecta la opción seleccionada
        if (idAccMenu == R.id.mnInstrucc) {
            mostrarInstrucciones();
            return true;
        } else if (idAccMenu == R.id.nmNuevoJuego) {
            nuevoJuego(nivel);
            return true;
        } else if (idAccMenu == R.id.mnConfig) {
            mostrarDialogConfig();
            return true;
        } else if (idAccMenu == R.id.mnSelecc) {
            mostrarDialogSelecPersonaje(item);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // ----- MÉTODOS DEL MENÚ -----
    /**
     * Muestra un diálogo con las instrucciones del juego.
     */
    private void mostrarInstrucciones() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instrucciones");
        builder.setMessage(
                "Los personajes del juego esconden minas.\n" +
                        "Encuéntralos para que no exploten!!.\n\n" +
                        "Despeja el tablero sin explotar las minas:\n" +
                        "- Clic corto para destapar una celda.\n" +
                        "- Clic largo para marcar una mina.\n" +
                        "- Los números indican cuántas minas hay alrededor.\n" +
                        "- Un punto por cada mina acertada!!"
        );
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {// Botón Aceptar
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();// Cierra el diálogo
            }
        });

        // Crea el diálogo
        AlertDialog dialog = builder.create();

        // Muestra el diálogo
        dialog.show();
    }

    /**
     * Inicia un nuevo juego con el nivel de dificultad actual.
     *
     * @param nivel de dificultad seleccionada
     */
    private void nuevoJuego(int nivel) {

        if (partida == null) {
            partida = new Partida();
        }

        partida.setPuntos(0); // Reiniciar puntuación

        // Establece el nivel
        partida.setNivel(nivel);

        // Inicia la partida con el nivel seleccionado cuando pulse el menú nuevo juego
        partida.iniciarJuego();
        dibujarTablero();
        jugando = true;
    }

    /**
     * Muestra un diálogo para seleccionar el nivel de dificultad.
     */
    private void mostrarDialogConfig() {

        // RadioGroup y RadioButtons con los niveles de dificultad
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        RadioButton rbtPrincipiante = new RadioButton(this);
        rbtPrincipiante.setText(R.string.principiante);
        radioGroup.addView(rbtPrincipiante);
        int idRbtPrindipiante = rbtPrincipiante.getId();

        RadioButton rbtAmateur = new RadioButton(this);
        rbtAmateur.setText(R.string.amateur);
        radioGroup.addView(rbtAmateur);
        int idRbtAmateur = rbtAmateur.getId();

        RadioButton rbtAvanzado = new RadioButton(this);
        rbtAvanzado.setText(R.string.avanzado);
        radioGroup.addView(rbtAvanzado);
        int idRbtAvanzado = rbtAvanzado.getId();

        // Establece el RadioButton marcado según el valor actual de 'nivel'
        if (nivel == 1) {
            radioGroup.check(idRbtPrindipiante);
        } else if (nivel == 2) {
            radioGroup.check(idRbtAmateur);
        } else if (nivel == 3) {
            radioGroup.check(idRbtAvanzado);
        } else {
            radioGroup.check(idRbtPrindipiante);
        }

        // Muestra el nivel de dificultad con un diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nivel de dificultad");
        builder.setView(radioGroup);

        // Cierra el diálogo al pulsar "Volver"
        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        // Obtiene el nivel de dificultad al seleccionar un radiobutton y pulsar "Aceptar"
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int rbtId = radioGroup.getCheckedRadioButtonId(); //id del rbt seleccionado
                if (rbtId == idRbtPrindipiante) {
                    nivel = 1;
                } else if (rbtId == idRbtAmateur) {
                    nivel = 2;
                } else if (rbtId == idRbtAvanzado) {
                    nivel = 3;
                }
            }
        });

        // Crea y muestra el diálogo
        builder.create().show();
    }

    /**
     * Muestra un diálogo para seleccionar el personaje asociado a las minas.
     */
    private void mostrarDialogSelecPersonaje(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Personajes:");

        // Crea la fila donde irá la foto y el texto
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        Spinner spinner = new Spinner(this);

        String[] nombres = {"Pokemon", "Flutter", "Bomba timer"};
        int[] fotos = {
                R.drawable.baseline_catching_pokemon_24,
                R.drawable.baseline_flutter_dash_24,
                R.drawable.baseline_timer_24
        };

        // Crea una lista de objetos Personaje.
        ArrayList<Personaje> personajes = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            personajes.add(new Personaje(nombres[i], fotos[i]));
        }

        // Crea el adaptador
        Adaptador adaptador = new Adaptador(this, personajes);
        spinner.setAdapter(adaptador);

        layout.addView(spinner);
        builder.setView(layout);

        // Obtiene los valores del array de personajes según en qué fila del spinner cliquemos
        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int numfila = spinner.getSelectedItemPosition();
                Personaje personajeSeleccionado = personajes.get(numfila);
                int imgenPersonajeId = personajeSeleccionado.getImagenId();
                partida.setImgPersonajeId(imgenPersonajeId);

                item.setIcon(imgenPersonajeId);
            }
        });
        // Crea y muestra el diálogo
        builder.create().show();
    }

    //----- RESTO DE MÉTODOS -----

    /**
     * Maneja el clic corto en una casilla.
     * @param v Vista (casilla) pulsada.
     */
    @Override
    public void onClick(View v) {
        if (jugando) { // Solo procesa el clic si una partida está en curso.
            v.setEnabled(true);

            // Obtiene la fila y columna de la casilla a partir del tag de la vista.
            String tag = (String) v.getTag();
            int f = Integer.parseInt(tag.split(",")[0]);
            int c = Integer.parseInt(tag.split(",")[1]);
            Casilla casilla = partida.getTablero().getCasillas()[f][c];

            if (v instanceof Button) { // si es botón no hay mina
                Button btn = (Button) v;
                if (casilla.getMinasCerca() > 0) { // Si hay minas cerca, muestra el número.
                    btn.setText(String.valueOf(casilla.getMinasCerca()));
                    btn.setTextColor(getColor(R.color.numero));
                }
                btn.setEnabled(false);
                btn.setBackgroundColor(getColor(R.color.fondoDestapado));
            } else if (v instanceof ImageButton) { // Si es un ImageButton hay mina).
                ImageButton imgbtn = (ImageButton) v;
                Toast.makeText(MainActivity.this, "¡Mina! Perdiste.", Toast.LENGTH_LONG).show();
                imgbtn.setBackgroundColor(getColor(R.color.white));
                int imgPersonajeId = partida.getImgPersonajeId();
                imgbtn.setImageResource(imgPersonajeId);
                imgbtn.setScaleY(-1f); // Invierte la imagen verticalmente
                imgbtn.setForeground(AppCompatResources.getDrawable(this, R.drawable.baseline_close_24));                imgbtn.setEnabled(false);
                Toast.makeText(MainActivity.this, "Puntuación: " + partida.getPuntos() + " puntos", Toast.LENGTH_LONG).show();
                descubrirTablero(partida.getTablero().getCasillas(), tableroView, partida);
            }
        }
    }

    /**
     * Maneja el clic largo en una casilla (marcar minas).
     * @param v vista de la casilla pulsada con click largo.
     *
     * @return boolean Devuelve true si el callback ha consumido el evento de clic largo, false en caso contrario.
     */
    @Override
    public boolean onLongClick(View v) {
        if (jugando) { // Solo procesa el clic si una partida está en curso.
            v.setEnabled(true);

            // Obtiene la fila y columna de la casilla a partir del tag de la vista.
            String tag = (String) v.getTag();
            int f = Integer.parseInt(tag.split(",")[0]);
            int c = Integer.parseInt(tag.split(",")[1]);
            Casilla casilla = partida.getTablero().getCasillas()[f][c];

            if (v instanceof Button) {// Si es botón no hay mina
                Button btn = (Button) v;
                Toast.makeText(MainActivity.this, "No hay Mina. ¡Fallaste!", Toast.LENGTH_LONG).show();
                btn.setText(String.valueOf(casilla.getMinasCerca()));
                btn.setTextColor(getColor(R.color.numero));
                btn.setEnabled(false);
                btn.setBackgroundColor(getColor(R.color.fondoDestapado));
                Toast.makeText(MainActivity.this, "Puntuación: " + partida.getPuntos() + " puntos", Toast.LENGTH_LONG).show();
                descubrirTablero(partida.getTablero().getCasillas(), tableroView, partida);
            } else if (v instanceof ImageButton) {  // Si es un ImageButton hay mina).
                ImageButton imgbtn = (ImageButton) v;
                imgbtn.setImageResource(R.drawable.baseline_flag_24);
                imgbtn.setEnabled(false);
                partida.setPuntos(partida.getPuntos() + 1);
                // Comprueba si se han encontrado todas las minas.
                if (partida.getPuntos() == partida.getTablero().getMinas()) {
                    descubrirTablero(partida.getTablero().getCasillas(), tableroView, partida);
                    Toast.makeText(MainActivity.this, "Has Ganado!!!", Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Puntuación: " + partida.getPuntos() + " puntos", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Has encontrado una mina! 1 Punto!!", Toast.LENGTH_LONG).show();
                }
            }
        }
        return true;
    }

    /**
     * Descubre todas las casillas del tablero al finalizar la partida.
     * Muestra las minas y los números en las casillas correspondientes.
     *
     * @param casillas La matriz de objetos {@link Casilla} que representa el estado lógico del tablero.
     * @param gridLayout El {@link GridLayout} que contiene las casillas del tablero.
     * @param partida El objeto actual de la {@link Partida},
     */
    public void descubrirTablero(Casilla[][] casillas, GridLayout gridLayout, Partida partida) {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                Casilla casilla = casillas[i][j];
                View view = gridLayout.getChildAt(i * casillas[i].length + j);

                if (casilla.isMina() && view instanceof ImageButton) {
                    ImageButton imgbtn = (ImageButton) view;
                    int imgPersonajeId = partida.getImgPersonajeId();
                    imgbtn.setImageResource(imgPersonajeId);
                    imgbtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imgbtn.setPadding(0, 0, 0, 0);
                    imgbtn.setEnabled(false);
                } else if (!casilla.isMina() && view instanceof Button) {
                    Button btn = (Button) view;
                    btn.setText(String.valueOf(casilla.getMinasCerca()));
                    btn.setEnabled(false);
                    btn.setBackgroundColor(getColor(R.color.fondoDestapado));
                }
            }
        }
    }

    /**
     * Dibuja o redibuja el tablero en pantalla según el nivel y estado actual.
     */
    public void dibujarTablero() {
        Tablero tablero = partida.getTablero();
        tableroView.removeAllViews(); // Elimina todos los botones o vistas del contenedor
        // Crear el GridLayout dinámicamente
        tableroView = new GridLayout(this);// Crea un GridLayout
        tableroView.setRowCount(tablero.getNf()); // filas
        tableroView.setColumnCount(tablero.getNc()); // columnas

        ConstraintLayout mainContainer = findViewById(R.id.mainContainer);// Obtiene el contenedor del Grid
        ConstraintLayout.LayoutParams gridLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        tableroView.setLayoutParams(gridLayoutParams);

        // Agregar botones al GridLayout (uno por cada celda)
        for (int i = 0; i < tablero.getNf(); i++) {
            for (int j = 0; j < tablero.getNc(); j++) {

                Casilla casilla = tablero.getCasillas()[i][j];
                // Da tamaño al botón
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0; // Ancho proporcional
                params.height = 0; // Alto proporcional
                params.rowSpec = GridLayout.spec(i, 1, 1f); // 1 celda de alto con peso 1
                params.columnSpec = GridLayout.spec(j, 1, 1f); // 1 celda de ancho con peso 1

                if (!casilla.isMina()) { // Crear un Button si la casilla no tiene mina
                    Button btnCasilla = new Button(this);
                    btnCasilla.setLayoutParams(params); // Da tamaño al botón
                    btnCasilla.setTag(i + "," + j); // Guarda la posición para cuando se haga clic

                    // Configuración s/nivel
                    if (nivel == 1) {
                        btnCasilla.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        btnCasilla.setTypeface(Typeface.DEFAULT_BOLD);
                    } else if (nivel == 2) {
                        btnCasilla.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        btnCasilla.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        btnCasilla.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        btnCasilla.setPadding(0, 0, 0, 0);
                    }
                    btnCasilla.setOnClickListener(this);
                    btnCasilla.setOnLongClickListener(this);
                    tableroView.addView(btnCasilla);

                } else { // Crea un ImageButton si la casilla tiene una mina
                    ImageButton btnMina = new ImageButton(this);
                    btnMina.setLayoutParams(params);// Da tamaño al botón
                    btnMina.setTag(i + "," + j);  // Guarda la posición para cuando se haga clic
                    btnMina.setAdjustViewBounds(true);
                    btnMina.setPadding(1, 1, 1, 1);
                    btnMina.setOnClickListener(this);
                    btnMina.setOnLongClickListener(this);
                    tableroView.addView(btnMina);
                }
            }
        }

        // Agregar el GridLayout al contenedor
        mainContainer.addView(tableroView);
    }
}

