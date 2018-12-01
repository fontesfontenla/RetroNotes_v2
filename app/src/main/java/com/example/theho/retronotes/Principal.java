package com.example.theho.retronotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ComprobacionConexion.CambioIPListener {
    private ListView lv;
    private Adaptador adaptador;
    int request_code;
    String IP;
    SharedPreferences preferencias;
    SharedPreferences.Editor editorPreferencias;
    SentenciasSQLite bd;
    ConexionMySQL conexionRemota;
    int menuPosicion;
    SQLiteDatabase leer;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencias = this.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        //Cambio de tema de la aplicación
        int tema = preferencias.getInt("TEMA", R.style.AppTheme);
        setTheme(tema);

        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Establecemos el valor de la IP
        IP = preferencias.getString("IPNUEVA", "10.0.2.2");

        //Obtenemos los datos para mostrar en listView
        CargarListaLocal(obtDatos());

        //Cargamos el menu contextual para cada nota individual
        registerForContextMenu(lv);
        //Si se hace un click sobre la nota se puede editar
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                request_code = 1;
                bd = new SentenciasSQLite(getApplicationContext(), "datos_notas", null, 1);
                leer = bd.getReadableDatabase();
                String sentenciaEditar = "select * from datos_notas";
                cursor = leer.rawQuery(sentenciaEditar, null);
                if (cursor.moveToPosition(position)) {
                    String tituloEditar;
                    String contenidoEditar;
                    tituloEditar = cursor.getString(cursor.getColumnIndex("titulo"));
                    contenidoEditar = cursor.getString(cursor.getColumnIndex("contenido"));

                    //Lanzamos un intent a ActualizarNota para que muestre los datos ya guardados
                    Intent actualizarNota = new Intent(Principal.this, ActualizarNota.class);
                    actualizarNota.putExtra("TITULO", tituloEditar);
                    actualizarNota.putExtra("CONTENIDO", contenidoEditar);
                    startActivityForResult(actualizarNota, request_code);
                }
                cursor.close();
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

        //Menu lateral
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Si el tema es el normal, el fondo del navigation view es blanco
        if (tema == R.style.AppTheme) {
            navigationView.setBackgroundResource(R.color.white);
        }



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflador = getMenuInflater();
        if (v.getId() == R.id.lvNotas) {
            inflador.inflate(R.menu.contextual_nota, menu);
            menuPosicion = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcEditar:
                bd = new SentenciasSQLite(getApplicationContext(), "datos_notas", null, 1);
                leer = bd.getReadableDatabase();
                String sentenciaEditar = "select * from datos_notas";
                cursor = leer.rawQuery(sentenciaEditar, null);
                if (cursor.moveToPosition(menuPosicion)) {
                    String tituloEditar;
                    String contenidoEditar;
                    tituloEditar = cursor.getString(cursor.getColumnIndex("titulo"));
                    contenidoEditar = cursor.getString(cursor.getColumnIndex("contenido"));

                    //Lanzamos un intent a ActualizarNota para que muestre los datos ya guardados
                    Intent actualizarNota = new Intent(Principal.this, ActualizarNota.class);
                    actualizarNota.putExtra("TITULO", tituloEditar);
                    actualizarNota.putExtra("CONTENIDO", contenidoEditar);
                    startActivityForResult(actualizarNota, request_code);
                }
                cursor.close();
                break;
            case R.id.opcBorrar:
                bd = new SentenciasSQLite(getApplicationContext(), "datos_notas", null, 1);
                leer = bd.getReadableDatabase();
                String sentenciaBorrar = "select * from datos_notas";
                cursor = leer.rawQuery(sentenciaBorrar, null);
                if (cursor.moveToPosition(menuPosicion)) {
                    String titulo;
                    String contenido;
                    String color;
                    titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                    contenido = cursor.getString(cursor.getColumnIndex("contenido"));
                    color = cursor.getString(cursor.getColumnIndex("color"));
                    SQLiteDatabase borrar = bd.getWritableDatabase();
                    //Cargamos datos a la base de datos local
                    bd.borrarDatos(borrar, titulo, contenido, color);
                    IP = preferencias.getString("IPNUEVA", "10.0.2.2");
                    conexionRemota = new ConexionMySQL();
                    //Cargamos datos a la base de datos remota
                    conexionRemota.CargaDatos("http://" + IP + "/RetroNotes/borrado.php?titulo=" + titulo + "&contenido=" + contenido);
                    if (borrar != null && leer != null) {
                        borrar.close();
                        leer.close();
                    }
                }
                cursor.close();
                CargarListaLocal(obtDatos());
                Toast.makeText(getApplicationContext(), getString(R.string.toast_notaeliminada), Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Accion cuando volvemos del segundo activity
        if (requestCode == request_code && resultCode == RESULT_OK) {
            //Se cargan los datos de nuevo para actualizar la lista
            CargarListaLocal(obtDatos());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Menu de seleccion de temas
     */
    public void CreateAlertDialogWithRadioButtonGroup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
        builder.setTitle(getString(R.string.menu_tema_titulo));
        String[] values = {getString(R.string.menu_tema_oscuro), getString(R.string.menu_tema_clasico)};
        preferencias = this.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        int seleccionado = preferencias.getInt("OPCION_SELECCIONADA_TEMA", 1);
        builder.setSingleChoiceItems(values, seleccionado, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Toast.makeText(Principal.this, getString(R.string.toast_modooscuro), Toast.LENGTH_LONG).show();
                        editorPreferencias = preferencias.edit();
                        editorPreferencias.putInt("TEMA", R.style.Oscuro);
                        editorPreferencias.putInt("OPCION_SELECCIONADA_TEMA", 0);
                        editorPreferencias.apply();
                        recreate();
                        break;
                    case 1:
                        Toast.makeText(Principal.this, getString(R.string.toast_modoclasico), Toast.LENGTH_LONG).show();
                        editorPreferencias = preferencias.edit();
                        editorPreferencias.putInt("TEMA", R.style.AppTheme);
                        editorPreferencias.putInt("OPCION_SELECCIONADA_TEMA", 1);
                        editorPreferencias.apply();
                        recreate();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_notas) {
            onBackPressed();
        } else if (id == R.id.nav_tema) {
            CreateAlertDialogWithRadioButtonGroup();
        } else if (id == R.id.nav_cambio_ip) {
            mostrarDialogoIP();
        } else if (id == R.id.nav_info) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
            LayoutInflater inflador = getLayoutInflater();
            builder.setView(inflador.inflate(R.layout.texto_info, null));
            builder.setPositiveButton(getString(R.string.btn_aceptar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Muestra la ventana de dialogo para cambiar la IP
     */
    public void mostrarDialogoIP() {
        ComprobacionConexion comprobarIP = new ComprobacionConexion();
        comprobarIP.show(getSupportFragmentManager(), "ip");
    }

    @Override
    public void conseguirIP(String ipMenu) {
        //Conseguimos el valor de la IP escrita
        IP = ipMenu;
        IP = preferencias.getString("IPNUEVA", "10.0.2.2");
    }

    /**
     * Carga el listView con los datos sacados de obtDatos
     *
     * @param datos arrayList de datos
     */
    public void CargarListaLocal(ArrayList<Nota> datos) {
        adaptador = new Adaptador(datos, this);
        lv = findViewById(R.id.lvNotas);
        lv.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }

    /**
     * Obtiene los datos de la base de datos en JSON y los guarda por separado en un array de tipo Nota
     *
     * @return arrayList con los datos a pasarle al adaptador
     */
    public ArrayList<Nota> obtDatos() {
        ArrayList<Nota> listado = new ArrayList<>();
        bd = new SentenciasSQLite(getApplicationContext(), "datos_notas", null, 1);
        SQLiteDatabase db = bd.getReadableDatabase();
        String sentencia = "select * from datos_notas";
        Cursor cursor = db.rawQuery(sentencia, null);
        if (cursor.moveToFirst()) {
            String titulo;
            String contenido;
            String color;
            do {
                titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                contenido = cursor.getString(cursor.getColumnIndex("contenido"));
                color = cursor.getString(cursor.getColumnIndex("color"));
                Nota nuevaNota = new Nota(titulo, contenido, "#" + color);
                listado.add(nuevaNota);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listado;
    }
}
