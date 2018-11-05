package com.example.theho.retronotes;

import android.content.Context;
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

    ArrayList<Nota> notas;
    LayoutInflater inflador;

    public Adaptador(ArrayList<Nota> notas, Context contexto) {
        this.notas = notas;
        this.inflador = LayoutInflater.from(contexto);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder contenedor = null;
        if (convertView == null) {

            convertView = inflador.inflate(R.layout.adapter, null);

            contenedor = new ViewHolder();
            contenedor.titulo = (TextView) convertView.findViewById(R.id.tituloNota);
            contenedor.contenido = (TextView) convertView.findViewById(R.id.txtContenido);
            contenedor.hora = (TextView) convertView.findViewById(R.id.txtHora);
            contenedor.favorito = (ImageView) convertView.findViewById(R.id.imgFav);

            convertView.setTag(contenedor);
        } else contenedor = (ViewHolder) convertView.getTag();

        Nota peli = (Nota) getItem(position);
        contenedor.titulo.setText(peli.getTitulo());
        contenedor.contenido.setText(peli.getContenido());
        contenedor.hora.setText(peli.getHora());

        return convertView;
    }

    class ViewHolder {
        TextView titulo, contenido, hora;
        ImageView favorito;
    }
}
