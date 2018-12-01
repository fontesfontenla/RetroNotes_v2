package com.example.theho.retronotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Ejecuta las sentencias SQLite para la base de datos local
 */
public class SentenciasSQLite extends SQLiteOpenHelper {
    String sqlCreateTable = "CREATE TABLE datos_notas(titulo VARCHAR(64) NOT NULL," +
            "contenido TEXT NOT NULL," +
            "color VARCHAR(50) NOT NULL)";

    public SentenciasSQLite(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int versión) {
        super(context, nombre, factory, versión);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void conseguirFecha(SQLiteDatabase db){
       db.execSQL("SELECT strftime('%d/%m', date('now'))");
    }
    public void insertarDatos(SQLiteDatabase db, String titulo, String contenido, String color) {

        db.execSQL("insert into datos_notas(titulo, contenido, color) values ('" + titulo + "','" + contenido + "','" + color + "')");
    }
    public void borrarDatos(SQLiteDatabase db, String titulo, String contenido, String color){
        db.execSQL("DELETE FROM datos_notas where titulo='"+titulo+"' and contenido='"+contenido+"' and color='"+color+"'");
    }
    public void modificarDatos(SQLiteDatabase db, String tituloNuevo, String contenidoNuevo, String tituloAntiguo, String contenidoAntiguo){
        db.execSQL("update datos_notas set titulo='"+tituloNuevo+"', contenido='"+contenidoNuevo+"' where titulo='"+tituloAntiguo+"' and contenido='"+contenidoAntiguo+"'");
    }
}