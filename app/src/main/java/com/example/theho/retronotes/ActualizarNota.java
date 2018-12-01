package com.example.theho.retronotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ACTUALIZARNOTA: clase que actualiza los datos de una nota cuando se hace una pulsación sobre ella
 */
public class ActualizarNota extends AppCompatActivity{
    EditText titulo, contenido;
    ConstraintLayout layout;
    String tituloAntiguo, contenidoAntiguo;
    String IP;
    SharedPreferences preferencias;
    ConexionMySQL conexionRemota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Conseguimos el tema elegido y lo establecemos
        preferencias = this.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        int tema=preferencias.getInt("TEMA",R.style.AppTheme);
        setTheme(tema);

        setContentView(R.layout.activity_actualizar_nota);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtenemos los elementos de entrada de datos y demas
        titulo = findViewById(R.id.tituloNota);
        contenido = findViewById(R.id.contenidoNota);
        layout = findViewById(R.id.layoutNotaNueva);
        //Obtenemos los datos antiguos de la nota seleccionada de la lista para luego compararlos
        Intent mensaje = getIntent();
        tituloAntiguo = mensaje.getStringExtra("TITULO");
        contenidoAntiguo = mensaje.getStringExtra("CONTENIDO");

        //Esos datos se ponen en los campos de edicion
        titulo.setText(tituloAntiguo);
        contenido.setText(contenidoAntiguo);

        preferencias = this.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        IP = preferencias.getString("IPNUEVA", "10.0.2.2");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcnota_editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent notaCambiadaVuelta = new Intent();
        String tituloNuevo = titulo.getText().toString();
        String contenidoNuevo = contenido.getText().toString();

        conexionRemota=new ConexionMySQL();
        //Seleccionamos las opciones del menú del actionBar
        switch (id) {
            case android.R.id.home:
                //Si pulsamos atras cambiando los datos o sin cambiarlos
                if (tituloNuevo.equals(tituloAntiguo) & contenidoNuevo.equals(contenidoAntiguo)) {
                    onBackPressed();
                } else {
                    //Cargamos los datos
                    cambioDatos(tituloNuevo, contenidoNuevo);
                    IP = preferencias.getString("IPNUEVA", "10.0.2.2");
                    conexionRemota.CargaDatos("http://"+IP+"/RetroNotes/update.php?titulo=" + tituloNuevo + "" +
                            "&contenido=" + contenidoNuevo + "&tituloAnterior=" + tituloAntiguo + "&contenidoAnterior=" + contenidoAntiguo);

                    Toast.makeText(this, getString(R.string.toast_actualizarnota), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mnu_aceptar:
                //Si el contenido es el mismo o diferente
                if (tituloNuevo.equals(tituloAntiguo) & contenidoNuevo.equals(contenidoAntiguo)) {
                    onBackPressed();
                } else {
                    //Se cargan los datos
                    cambioDatos(tituloNuevo, contenidoNuevo);
                    IP = preferencias.getString("IPNUEVA", "10.0.2.2");
                    conexionRemota.CargaDatos("http://"+IP+"/RetroNotes/update.php?titulo=" + tituloNuevo + "" +
                            "&contenido=" + contenidoNuevo + "&tituloAnterior=" + tituloAntiguo + "&contenidoAnterior=" + contenidoAntiguo);
                }
                setResult(RESULT_OK, notaCambiadaVuelta);
                onBackPressed();
                Toast.makeText(this, getString(R.string.toast_actualizarnota), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo que carga los datos modificados en la base de datos local
     * @param titulo
     * @param contenido
     */
    public void cambioDatos(String titulo, String contenido) {
        SentenciasSQLite conexion = new SentenciasSQLite(getApplicationContext(), "datos_notas",
                null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        conexion.modificarDatos(db, titulo, contenido, tituloAntiguo, contenidoAntiguo);
        if (db != null) {
            db.close();
        }
    }
}
