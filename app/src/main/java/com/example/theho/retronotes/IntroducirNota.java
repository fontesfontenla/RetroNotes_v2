package com.example.theho.retronotes;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class IntroducirNota extends AppCompatActivity {
    EditText titulo, contenido;
    String colorNota = "ffffff";
    ConstraintLayout layout;
    String IP="10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_nota);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titulo = findViewById(R.id.tituloNota);
        contenido = findViewById(R.id.contenidoNota);
        layout = findViewById(R.id.layoutNotaNueva);
    }

    /**
     *
     */
    public void abrirMenuColor() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colores = new ArrayList<>();
        colores.add("#258174");
        colores.add("#3C8D2F");
        colores.add("#20724F");
        colores.add("#800080");
        colores.add("#966D37");
        colores.add("#B77231");
        colores.add("#808000");
        colores.add("#323299");

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
        colorPicker.setColors(color1, color2, color3, color4, color5, color6, color7, color8, color9,
                color10,color11,color12,color13,color14,color15,color16,color17,color18).
                setColumns(5).
                setTitle("Elige un color").
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


    private class CargarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Conexion fallida a pagina web. URL puede no ser valida";
            }
        }

        @Override
        protected void onPostExecute(String result) {

        }

    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        myurl = myurl.replace(" ", "%20");
        int len = 500;
        try {
            //Convierte la URL como String en un objeto URL
            URL url = new URL(myurl);
            //Creamos la conexion
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //Conectamos
            conn.connect();
            //Lo que responda la URl, lo guarda en el inputStream
            is = conn.getInputStream();

            //Convertimos el contenido en un string
            String contentAsString = readIt(is, len);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }


    }

    //Transforma el resultado de la conexion URL a String mediante un buffer
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
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
                    onBackPressed();
                    Toast.makeText(this, "Nota eliminada", Toast.LENGTH_LONG).show();
                break;
            case R.id.mnu_aceptar:
                if (titulo.getText().toString().equals("") || contenido.getText().toString().equals("")) {
                    onBackPressed();
                } else {
                    String tituloGuardar = titulo.getText().toString();
                    String contenidoGuardar = contenido.getText().toString();
                    String colorGuardar = colorNota;
                    //Guardamos los datos en la base de datos
                    //ENLACE IP
                    new CargarDatos().execute("http://"+IP+"/RetroNotes/registro.php?titulo=" + tituloGuardar + "" +
                            "&contenido=" + contenidoGuardar + "&color=" + colorGuardar);

                    Intent nuevaNotaVuelta = new Intent();
                    setResult(RESULT_OK, nuevaNotaVuelta);
                    onBackPressed();
                    Toast.makeText(this, "Nota guardada", Toast.LENGTH_LONG).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
