package com.example.theho.retronotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Lanza el dialogo de seleccion de IP y comprueba su estructura
 */
public class ComprobacionConexion extends AppCompatDialogFragment {
    private EditText txtTest;
    private CambioIPListener listener;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        preferencias = getContext().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflador = getActivity().getLayoutInflater();
        View view = inflador.inflate(R.layout.cambio_ip, null);
        txtTest = view.findViewById(R.id.txtIP);
        txtTest.setHint(preferencias.getString("IPNUEVA", "10.0.2.2"));
        builder.setView(view).setNegativeButton(getString(R.string.btn_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(getString(R.string.btn_aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ip = txtTest.getText().toString();
                validarIP(ip);
            }
        });
        return builder.create();
    }

    /**
     * Valida que la IP que se le pasa tenga la estructura correcta
     *
     * @param ipString
     */
    public void validarIP(String ipString) {
        String regex = "\\b((25[0–5]|2[0–4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0–5]|2[0–4]\\d|[01]?\\d\\d?)\\b";

        if (Pattern.matches(regex, ipString) || ipString.equals("10.0.2.2")) {
            editor = preferencias.edit();
            editor.putString("IPNUEVA", ipString);
            editor.apply();
            listener.conseguirIP(ipString);
            Toast.makeText(getContext(), getString(R.string.toast_ipcorrecta), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_ipincorrecta), Toast.LENGTH_SHORT).show();
        }
    }

    public interface CambioIPListener {
        void conseguirIP(String ip);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CambioIPListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
