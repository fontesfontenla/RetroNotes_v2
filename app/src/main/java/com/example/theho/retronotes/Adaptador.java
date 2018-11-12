package com.example.theho.retronotes;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by theho on 02/11/2018.
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
            LayoutInflater inflador = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflador.inflate(R.layout.adapter, null);


        }

        TextView titulo=convertView.findViewById(R.id.txtTitulo);
        TextView contenido=convertView.findViewById(R.id.txtContenido);
        TextView hora=convertView.findViewById(R.id.txtHora);

        titulo.setText(arrayList.get(position).getTitulo());
        contenido.setText(arrayList.get(position).getContenido());
        hora.setText(arrayList.get(position).getHora());
        convertView.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));


        return convertView;
    }

}
