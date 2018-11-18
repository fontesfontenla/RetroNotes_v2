package com.example.theho.retronotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
public class ActualizarNota extends AppCompatActivity {
    EditText titulo, contenido;
    ConstraintLayout layout;
    String tituloAntiguo, contenidoAntiguo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);
        //Quitamos el titulo
        getSupportActionBar().setTitle("");
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
        //Seleccionamos las opciones del menú del actionBar
        switch (id) {
            case android.R.id.home:
                //Si pulsamos atras cambiando los datos o sin cambiarlos
                if (tituloNuevo.equals(tituloAntiguo) & contenidoNuevo.equals(contenidoAntiguo)) {
                    onBackPressed();
                } else {
                    //Llamamos a CargarDatos al que le pasamos la URL con los datos pasamos como GET
                    new CargarDatos().execute("http://192.168.1.38/RetroNotes/update.php?titulo=" + tituloNuevo + "" +
                            "&contenido=" + contenidoNuevo + "&tituloAnterior=" + tituloAntiguo + "&contenidoAnterior=" + contenidoAntiguo);
                }
                break;
            case R.id.mnu_aceptar:
                //Si el contenido es el mismo o diferente
                if (tituloNuevo.equals(tituloAntiguo) & contenidoNuevo.equals(contenidoAntiguo)) {
                    onBackPressed();
                } else {
                    //Llamamos a CargarDatos al que le pasamos la URL con los datos pasamos como GET
                    new CargarDatos().execute("http://192.168.1.38/RetroNotes/update.php?titulo=" + tituloNuevo + "" +
                            "&contenido=" + contenidoNuevo + "&tituloAnterior=" + tituloAntiguo + "&contenidoAnterior=" + contenidoAntiguo);
                }
                setResult(RESULT_OK, notaCambiadaVuelta);
                onBackPressed();
                Toast.makeText(this, "Nota guardada", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * CargarDatos: clase que ejecuta la URL del archivo PHP en la base de datos para que ejecute
     * cambios en la abse de datos
     */
    private class CargarDatos extends AsyncTask<String, Void, String> {
        @Override
        //La conexion a la URL la realiza por detras, en otro hilo, asi evitamos crasheos

        protected String doInBackground(String... urls) {
            try {
                return descargaURL(urls[0]);
            } catch (IOException e) {
                return "Conexion fallida a pagina web. URL puede no ser valida";
            }
        }

        @Override
        protected void onPostExecute(String result) {

        }

    }

    /**
     * descargaURL: se conecta a la URL especificada, y lo que consigue lo convierte en un inputStream
     * para convertirlo en un String
     *
     * @param miUrl URL a ejecutar
     * @return contenido en string
     * @throws IOException
     */
    private String descargaURL(String miUrl) throws IOException {
        InputStream is = null;
        miUrl = miUrl.replace(" ", "%20");
        int len = 500;
        try {
            //Convierte la URL como String en un objeto URL
            URL url = new URL(miUrl);
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
            String contenidoEnString = pasoAString(is, len);
            return contenidoEnString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * pasoAString: transforma el contenido recogido de la URL en string mediante buffer
     *
     * @param stream   inputStream de la URL
     * @param longitud longitud de la URL
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public String pasoAString(InputStream stream, int longitud) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[longitud];
        reader.read(buffer);
        return new String(buffer);
    }

}
