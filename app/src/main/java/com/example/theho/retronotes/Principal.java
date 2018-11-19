package com.example.theho.retronotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Principal extends AppCompatActivity {
    private ListView lv;
    private Adaptador adaptador;
    private ArrayList<Nota> nota;

    String response_body;
    int request_code;
    String IP="10.0.2.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obtenemos los datos para mostrar en listView
        ObtDatos();

        //Generamos un arrayList de tipo Nota donde se guardaran los datos para mostrarlos
        nota = new ArrayList<>();
        adaptador = new Adaptador(nota, this);
        lv = findViewById(R.id.lvNotas);
        lv.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();

        //Si se hace un click sobre la nota se puede editar
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                request_code = 1;
                //Obtenemos el JSON de todos los datos, para conseguir el elemento concreto que buscamos
                try {
                    JSONArray jsonArray = new JSONArray(response_body);
                    String tituloCambiar;
                    String contenidoCambiar;

                    //Conseguimos todas las variables que buscamos en la posicion seleccionada
                    tituloCambiar = jsonArray.getJSONObject(position).getString("titulo");
                    contenidoCambiar = jsonArray.getJSONObject(position).getString("contenido");

                    //Lanzamos un intent a ActualizarNota para que muestre los datos ya guardados
                    Intent actualizarNota = new Intent(Principal.this, ActualizarNota.class);
                    actualizarNota.putExtra("TITULO", tituloCambiar);
                    actualizarNota.putExtra("CONTENIDO", contenidoCambiar);
                    startActivityForResult(actualizarNota, request_code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //Un click largo en la nota la elimina de la lista
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Obtenemos los datos de la nota seleccionada de su JSON
                    JSONArray jsonArray = new JSONArray(response_body);
                    String tituloBorrar;
                    String contenidoBorrar;
                    tituloBorrar = jsonArray.getJSONObject(position).getString("titulo");
                    contenidoBorrar = jsonArray.getJSONObject(position).getString("contenido");
                    //Ejecutamos la URL que borra datos
                    //ENLACE IP
                    new CargarDatos().execute("http://"+IP+"/RetroNotes/borrado.php?titulo=" + tituloBorrar + "&contenido=" + contenidoBorrar);
                    Toast.makeText(getApplicationContext(), "Nota eliminada", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        //Boton flotante te envia al activity secundario para que se cree una nota nueva
        FloatingActionButton fab = findViewById(R.id.fab_nueva_nota);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_code = 1;
                Intent nuevaNota = new Intent(Principal.this, IntroducirNota.class);
                startActivityForResult(nuevaNota, request_code);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Accion cuando volvemos del segundo activity
        if (requestCode == request_code && resultCode == RESULT_OK) {
            //Se cargan los datos de nuevo para actualizar la lista
            ObtDatos();
            adaptador = new Adaptador(nota, getApplicationContext());
            lv = findViewById(R.id.lvNotas);
            lv.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * CargarDatos: clase que ejecuta la URL del archivo PHP en la base de datos para que ejecute
     * cambios en la base de datos
     */
    private class CargarDatos extends AsyncTask<String, Void, String> {
        @Override
        //La conexion a la URL la realiza por detras, en otro hilo, asi evitamos crasheos
        protected String doInBackground(String... urls) {
            try {
                return descargaUrl(urls[0]);
            } catch (IOException e) {
                return "Conexion fallida a pagina web. URL puede no ser valida";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Despues de realizar el borrado de los datos, actualizamos la listview para que se muestre
            //con los datos nuevos
            adaptador = new Adaptador(nota, getApplicationContext());
            lv = findViewById(R.id.lvNotas);
            lv.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();
            //Obtenemos los datos de nuevo
            ObtDatos();
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
    private String descargaUrl(String miUrl) throws IOException {
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
            String contentAsString = pasoAString(is, len);
            return contentAsString;
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
    //Transforma el resultado de la conexion URL a String mediante un buffer
    public String pasoAString(InputStream stream, int longitud) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[longitud];
        reader.read(buffer);
        return new String(buffer);
    }


    /**
     * Obtenemos los datos de consulta de toda la abse de datos para que los clasifique y los guarde,
     * y a su vez se muestren
     */
    public void ObtDatos() {
        AsyncHttpClient client = new AsyncHttpClient();
        //ENLACE IP
        String url = "http://"+IP+"/RetroNotes/consulta.php";
        RequestParams parametros = new RequestParams();
        parametros.put("id", 8);
        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    //Carga el arrayList de los objeton JSON
                    CargarLista(obtDatosJSON(new String(responseBody)));
                    response_body = new String(responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * Carga el listView con los datos sacados de obtDatosJSON
     *
     * @param datos arrayList de datos
     */
    public void CargarLista(ArrayList<Nota> datos) {
        adaptador = new Adaptador(datos, this);
        lv = findViewById(R.id.lvNotas);
        lv.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }

    /**
     * Obtiene los datos de la base de datos en JSON y los guarda por separado en un array de tipo Nota
     *
     * @param respuesta
     * @return
     */
    public ArrayList<Nota> obtDatosJSON(String respuesta) {
        ArrayList<Nota> listado = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            String titulo;
            String contenido;
            String color;
            String hora;
            for (int i = 0; i < jsonArray.length(); i++) {
                //Conseguimos todas las variables y las guardamos en un arrayList de tipo Nota
                titulo = jsonArray.getJSONObject(i).getString("titulo");
                contenido = jsonArray.getJSONObject(i).getString("contenido");
                color = jsonArray.getJSONObject(i).getString("color");
                hora = jsonArray.getJSONObject(i).getString("hora");
                Nota nuevaNota = new Nota(titulo, contenido, hora, "#" + color);
                listado.add(nuevaNota);
            }
        } catch (JSONException error) {
            error.printStackTrace();
        }
        return listado;
    }

}
