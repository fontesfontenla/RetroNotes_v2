package com.example.theho.retronotes;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IntroducirNota extends AppCompatActivity {
    EditText tituloNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_nota);
        tituloNota = findViewById(R.id.tituloNota);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosNota = new Intent(IntroducirNota.this,Principal.class);
                datosNota.setData(Uri.parse(tituloNota.getText().toString()));
                setResult(RESULT_OK,datosNota);
                finish();

            }
        });


    }
}
