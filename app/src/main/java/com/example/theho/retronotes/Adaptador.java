package com.example.theho.retronotes;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * ADAPTADOR: clase que genera la vista de la nota en el ListView en un determinado formato
 */

public class Adaptador extends BaseAdapter {

    private Context context;
    private ArrayList<Nota> arrayList;

    public Adaptador(ArrayList<Nota> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            //Inflamos el adaptador
            LayoutInflater inflador = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflador.inflate(R.layout.adapter, null);
        }
        //Obtenemos los parametros de visualziacion de datos
        TextView titulo = convertView.findViewById(R.id.txtTitulo);
        TextView contenido = convertView.findViewById(R.id.txtContenido);

        //Se asocian a los datos reogidos del arrayList
        titulo.setText(arrayList.get(position).getTitulo());
        contenido.setText(arrayList.get(position).getContenido());
        convertView.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

        String colorNota=arrayList.get(position).getColor().toLowerCase();
        //Si el color de fondo de la nota es blanco cambiamos el color de texto a negro
        if(colorNota.equals("#ffffff")){
            titulo.setTextColor(ContextCompat.getColor(context,R.color.darkPrimaryOscuro));
            contenido.setTextColor(ContextCompat.getColor(context,R.color.darkPrimaryOscuro));
        } else{
            titulo.setTextColor(ContextCompat.getColor(context,R.color.white));
            contenido.setTextColor(ContextCompat.getColor(context,R.color.white));
        }

        return convertView;
    }

}
