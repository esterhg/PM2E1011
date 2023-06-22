package com.example.pm2e180114.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pm2e180114.Contactos;

import java.util.ArrayList;

public class SQLiteConexion extends SQLiteOpenHelper
{
    public SQLiteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /* Creacion de objectos de base de datos */
        sqLiteDatabase.execSQL(Transacciones.CreateTableContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Transacciones.DROPTableContactos);
        onCreate(db);
    }
    public static SQLiteConexion getInstance(Context context) {
        return new SQLiteConexion(context, Transacciones.NameDatabase, null, 1);
    }


}
