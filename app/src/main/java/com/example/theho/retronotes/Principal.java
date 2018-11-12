package com.example.theho.retronotes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lv;
    private Adaptador adaptador;
    private ArrayList<Nota> nota;

    int request_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creacion de un ArrayList
        nota = new ArrayList<>();

        Nota nota1 = new Nota("Nota 1", "Contenido nota 1", "11:30","#ff66c1");
        Nota nota2 = new Nota("Nota 2", "Contenido nota 2", "12:30","#66cdaa");
        Nota nota3 = new Nota("Nota 3", "Contenido nota 3", "13:30","#f2de15");
        Nota nota4 = new Nota("Nota 4", "Contenido nota 4", "14:30","#cc0000");
        Nota nota5 = new Nota("Nota 5", "Contenido nota 5", "15:30","#8187ff");
        nota.add(nota1);
        nota.add(nota2);
       /* nota.add(nota3);
        nota.add(nota4);
        nota.add(nota5);
*/
        //Añadir notas a la listview principal
        adaptador = new Adaptador(nota, this);
        lv = findViewById(R.id.lvNotas);
        lv.setAdapter(adaptador);

        //Boton flotante te envia al activity secundario
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    //Accion cuando volvemos del segundo activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == request_code && resultCode == RESULT_OK) {
            String titulo = data.getSerializableExtra("TITULO").toString();
            String contenido = data.getSerializableExtra("CONTENIDO").toString();
            String fecha = data.getSerializableExtra("HORA").toString();
            String colorNota=data.getSerializableExtra("COLOR").toString();
            Log.i("colornotaprincipal", colorNota);
            Nota nuevaNota = new Nota(titulo, contenido, fecha,"#"+colorNota);
            nota.add(nuevaNota);
            adaptador = new Adaptador(nota, this);
            lv.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    //TODO Opcion del actionbar de busqueda
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnu_busqueda) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notas) {
            // Handle the camera action
        } else if (id == R.id.nav_destacados) {

        } else if (id == R.id.nav_ordenar) {

        } else if (id == R.id.nav_color) {

        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
