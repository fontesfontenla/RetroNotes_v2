package com.example.theho.retronotes;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConexionMySQL {

    /**
     * Ejecutamos la sentencia de conexion con la base de datos remota
     *
     * @param sentencia URL a ejecutar
     */
    public void CargaDatos(String sentencia) {
        new CargarDatosRemoto().execute(sentencia);
    }

    /**
     * CargarDatosRemoto: clase que ejecuta la URL del archivo PHP en la base de datos para que ejecute
     * cambios en la base de datos
     */

    private class CargarDatosRemoto extends AsyncTask<String, Void, String> {
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
    public String pasoAString(InputStream stream, int longitud) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[longitud];
        reader.read(buffer);
        return new String(buffer);
    }
}
