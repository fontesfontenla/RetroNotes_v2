package com.example.theho.retronotes;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import java.io.Serializable;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;
import yuku.ambilwarna.AmbilWarnaDialog;

public class IntroducirNota extends AppCompatActivity {
    EditText titulo, contenido;
    String colorNota;
    ConstraintLayout layout;

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
        colorPicker.setColors(color1, color2, color3, color4, color5, color6, color7, color8).
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
                if (titulo.getText().toString() != "" || titulo.getText().toString() != "") {
                    //TODO lanzar ventana de confirmacion
                    onBackPressed();
                } else {
                    onBackPressed();
                    Toast.makeText(this, "Nota eliminada", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mnu_aceptar:
                if (titulo.getText().toString() == null || colorNota == null) {
                    onBackPressed();
                    Toast.makeText(this, "P'atras sin guardar nada", Toast.LENGTH_LONG).show();
                } else {
                    Intent nuevaNotaVuelta = new Intent();
                    nuevaNotaVuelta.putExtra("TITULO", titulo.getText().toString());
                    nuevaNotaVuelta.putExtra("CONTENIDO", contenido.getText().toString());
                    nuevaNotaVuelta.putExtra("HORA", "17:35");
                    nuevaNotaVuelta.putExtra("COLOR", colorNota);
                    setResult(RESULT_OK, nuevaNotaVuelta);
                    onBackPressed();
                    Toast.makeText(this, "Nota guardada", Toast.LENGTH_LONG).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
