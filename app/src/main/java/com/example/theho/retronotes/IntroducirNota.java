package com.example.theho.retronotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
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
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class IntroducirNota extends AppCompatActivity {
    EditText titulo, contenido;
    String colorNota = "ffffff";
    ConstraintLayout layout;
    String IP;
    SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Conseguimos el tema seleccionado y cambiamos el tema de la activity
        preferencias = this.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        int tema = preferencias.getInt("TEMA", R.style.AppTheme);
        setTheme(tema);

        setContentView(R.layout.activity_introducir_nota);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titulo = findViewById(R.id.tituloNota);
        contenido = findViewById(R.id.contenidoNota);
        layout = findViewById(R.id.layoutNotaNueva);
        //Conseguimos la IP
        IP = preferencias.getString("IPNUEVA", "10.0.2.2");
    }

    /**
     * Abre el menu de seleccion de color de la nota
     */
    public void abrirMenuColor() {
        final ColorPicker colorPicker = new ColorPicker(this);

        int color1 = ResourcesCompat.getColor(getResources(), R.color.colorFondo1, null);
        int color2 = ResourcesCompat.getColor(getResources(), R.color.colorFondo2, null);
        int color3 = ResourcesCompat.getColor(getResources(), R.color.colorFondo3, null);
        int color4 = ResourcesCompat.getColor(getResources(), R.color.colorFondo4, null);
        int color5 = ResourcesCompat.getColor(getResources(), R.color.colorFondo5, null);
        int color6 = ResourcesCompat.getColor(getResources(), R.color.colorFondo6, null);
        int color7 = ResourcesCompat.getColor(getResources(), R.color.colorFondo7, null);
        int color8 = ResourcesCompat.getColor(getResources(), R.color.colorFondo8, null);
        int color9 = ResourcesCompat.getColor(getResources(), R.color.colorFondo9, null);
        int color10 = ResourcesCompat.getColor(getResources(), R.color.colorFondo10, null);
        int color11 = ResourcesCompat.getColor(getResources(), R.color.colorFondo11, null);
        int color12 = ResourcesCompat.getColor(getResources(), R.color.colorFondo12, null);
        int color13 = ResourcesCompat.getColor(getResources(), R.color.colorFondo13, null);
        int color14 = ResourcesCompat.getColor(getResources(), R.color.colorFondo14, null);
        int color15 = ResourcesCompat.getColor(getResources(), R.color.colorFondo15, null);
        int color16 = ResourcesCompat.getColor(getResources(), R.color.colorFondo16, null);
        int color17 = ResourcesCompat.getColor(getResources(), R.color.colorFondo17, null);
        int color18 = ResourcesCompat.getColor(getResources(), R.color.colorFondo18, null);
        //Metemos los colores
        colorPicker.setColors(color1, color2, color3, color4, color5, color6, color7, color8, color9,
                color10, color11, color12, color13, color14, color15, color16, color17, color18).
                setColumns(5).
                setTitle(getString(R.string.color_titulo)).
                setRoundColorButton(true).
                setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        //Se guarda el color en hexadecimal
                        colorNota = Integer.toHexString(color);
                        layout.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcnota, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnu_color:
                abrirMenuColor();
                break;
            case android.R.id.home:
                if (titulo.getText().toString().equals("") || contenido.getText().toString().equals("")) {
                    onBackPressed();
                } else {
                    String tituloGuardar = titulo.getText().toString();
                    String contenidoGuardar = contenido.getText().toString();
                    String colorGuardar = colorNota;

                    //Establecemos conexion remota
                    IP = preferencias.getString("IPNUEVA", "10.0.2.2");
                    ConexionMySQL conexionRemota = new ConexionMySQL();
                    conexionRemota.CargaDatos("http://" + IP + "/RetroNotes/registro.php?titulo=" + tituloGuardar + "" +
                            "&contenido=" + contenidoGuardar + "&color=" + colorGuardar);
                    SentenciasSQLite conexion = new SentenciasSQLite(getApplicationContext(), "datos_notas",
                            null, 1);
                    SQLiteDatabase db = conexion.getWritableDatabase();
                    //Guardamos localmente
                    conexion.insertarDatos(db, tituloGuardar, contenidoGuardar, colorGuardar);
                    if (db != null) {
                        db.close();
                    }
                    Intent nuevaNotaVuelta = new Intent();
                    setResult(RESULT_OK, nuevaNotaVuelta);
                    onBackPressed();
                    Toast.makeText(this, getString(R.string.toast_notaguardada), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mnu_aceptar:

                    String tituloGuardar = titulo.getText().toString();
                    String contenidoGuardar = contenido.getText().toString();
                    String colorGuardar = colorNota;
                    //Establecemos conexion remota
                    IP = preferencias.getString("IPNUEVA", "10.0.2.2");
                    ConexionMySQL conexionRemota = new ConexionMySQL();
                    conexionRemota.CargaDatos("http://"+IP+"/RetroNotes/registro.php?titulo=" + tituloGuardar + "" +
                            "&contenido=" + contenidoGuardar + "&color=" + colorGuardar);
                    SentenciasSQLite conexion = new SentenciasSQLite(getApplicationContext(), "datos_notas",
                            null, 1);
                    //Guardamos en local
                    SQLiteDatabase db = conexion.getWritableDatabase();
                    conexion.insertarDatos(db, tituloGuardar, contenidoGuardar, colorGuardar);
                    if (db != null) {
                        db.close();
                    }
                    Intent nuevaNotaVuelta = new Intent();
                    setResult(RESULT_OK, nuevaNotaVuelta);
                    onBackPressed();
                    Toast.makeText(this, getString(R.string.toast_notaguardada), Toast.LENGTH_LONG).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
