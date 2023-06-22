package com.example.pm2e180114;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2e180114.data.SQLiteConexion;
import com.example.pm2e180114.data.Transacciones;

import java.util.ArrayList;

public class modificar extends AppCompatActivity {
    SQLiteConexion conexion;
    int idSeleccionado = -1;
    EditText nombre;
    EditText telefono;
    EditText notas;
    EditText pais;
    Button actualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        nombre = (EditText) findViewById(R.id.txtnombre);
        telefono = (EditText) findViewById(R.id.txtnumero);
        notas = (EditText) findViewById(R.id.txtnota);
        pais = (EditText) findViewById(R.id.txtpais);
        actualizar = findViewById(R.id.btnactualizar);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idSeleccionado != -1) {
                    String nuevoNombre = nombre.getText().toString();
                    String nuevoTelefono = telefono.getText().toString();
                    String nuevasNotas = notas.getText().toString();
                    String paisNombre = pais.getText().toString();

                    actualizarContacto(idSeleccionado, nuevoNombre, paisNombre, nuevoTelefono, nuevasNotas);
                } else {
                    Toast.makeText(getApplicationContext(), "No se ha seleccionado ningún contacto para actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public void actualizarContacto(int id, String nuevoNombre, String paisNombre, String nuevoTelefono, String nuevasNotas) {
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Transacciones.ColumnasContactos.nombre, nuevoNombre);
        values.put(Transacciones.ColumnasContactos.pais, paisNombre);
        values.put(Transacciones.ColumnasContactos.telefono, nuevoTelefono);
        values.put(Transacciones.ColumnasContactos.notas, nuevasNotas);
        String whereClause = Transacciones.ColumnasContactos.id + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(Transacciones.tablaContactos, values, whereClause, whereArgs);

        Toast.makeText(getApplicationContext(), "Contacto actualizado", Toast.LENGTH_SHORT).show();
    }
}

