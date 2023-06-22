package com.example.pm2e180114.data;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Transacciones
{
   // Nombre de la base de datos
    public static final String NameDatabase = "PM";

    // tABLAS DE bASE DE DATOS

    public static final  String tablaContactos = "Contactos";

    // Campos de la tabla personas
    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String pais = "pais";
   public static final String notas= "notas";
 public static final String  imagen= "imagen";
    public static final String telefono = "telefono";



    // DDL Create and Drop
//    public static final String CreateTableContactos = "CREATE TABLE Contactos"+
//            "( id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, pais TEXT, telefono TEXT, "+
//            "notas TEXT )";
    public static final String CreateTableContactos = "CREATE TABLE Contactos"+
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, pais TEXT, telefono TEXT,  imagen  BLOB,notas TEXT)";

    public static final String DROPTableContactos = "DROP TABLE IF EXISTS contactos";

    // DML
    public static final String SelectTableContactos= "SELECT * FROM " + Transacciones.tablaContactos;

    public static final String updateTableContactos = "UPDATE Contactos SET nombre = ?, pais = ?, telefono = ?, imagen = ?, notas = ? WHERE id = ?";

    public static final class ColumnasContactos {
        public static final String id = "id";
        public static final String nombre = "nombre";
        public static final String pais = "pais";
        public static final String notas= "notas";
        public static final String  imagen= "imagen";
        public static final String telefono = "telefono";
    }
    @SuppressLint("Range")
    public static String obtenerNumeroTelefonoPorID(int idContacto) {
        String numeroTelefono = "";

        SQLiteDatabase db = SQLiteDatabase.openDatabase(NameDatabase, null, SQLiteDatabase.OPEN_READONLY);

        String[] columnas = {telefono};
        String whereClause = id + " = ?";
        String[] whereArgs = {String.valueOf(idContacto)};

        Cursor cursor = db.query(tablaContactos, columnas, whereClause, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            numeroTelefono = cursor.getString(cursor.getColumnIndex(telefono));
        }

        cursor.close();
        db.close();

        return numeroTelefono;
    }



}
