package com.example.pm2e180114;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;




import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.pm2e180114.data.SQLiteConexion;
import com.example.pm2e180114.data.Transacciones;

import java.io.File;
import java.util.ArrayList;

public class lista extends AppCompatActivity {
    SQLiteConexion conexion;
    ListView listpersonas;
    ArrayList<Contactos> lista;
    ArrayList<String> ArregloPersonas;
    Button eliminar;
    Button btnactualizar,compartir,verimg;
    private int posicionSeleccionada = -1;
    private int idContactoSeleccionado = -1;

    int idSeleccionado = -1;
    Contactos contacto;
    int id = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);



        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        listpersonas = (ListView) findViewById(R.id.listpersonas);

        ObtenerTabla();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloPersonas);
        listpersonas.setAdapter(adp);

        listpersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                posicionSeleccionada = i;
                idContactoSeleccionado = lista.get(i).getId();

                String selectedItem = (String) adapterView.getItemAtPosition(i);
                String telefonoSeleccionado = lista.get(i).getTelefono();
                Toast.makeText(getApplicationContext(), "Seleccionaste: " + selectedItem, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(lista.this);
                builder.setMessage("¿Deseas llamar a este contacto?")
                        .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + telefonoSeleccionado));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        eliminar=(Button) findViewById(R.id.btneliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (posicionSeleccionada != -1) {
                    eliminarContacto(idContactoSeleccionado);
                    posicionSeleccionada = -1;
                    ObtenerTabla();
                    actualizarLista();
                }
            }
        });
        
        btnactualizar=(Button) findViewById(R.id.btnactualizar);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String nuevoNombre = intent.getStringExtra("nuevoNombre");
        String paisNombre = intent.getStringExtra("paisNombre");
        String nuevoTelefono = intent.getStringExtra("nuevoTelefono");
        String nuevasNotas = intent.getStringExtra("nuevasNotas");

        // ...

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idContactoSeleccionado != -1) {
                    Intent intent = new Intent(lista.this, modificar.class);
                    intent.putExtra("id", idContactoSeleccionado); // Envía el ID del contacto seleccionado
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Seleccione un contacto para actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button backButton = findViewById(R.id.btnatras);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresar a la actividad anterior
            }
        });
        compartir=(Button) findViewById(R.id.btnCompartir);
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (posicionSeleccionada  != -1) {
                    Contactos contacto = obtenerContactoPorId(idContactoSeleccionado);
                    if (contacto != null) {
                        compartirContacto(contacto);
                        Toast.makeText(getApplicationContext(), " encontrado", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Contacto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private Contactos obtenerContactoPorId(int id) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacto = null;
        String[] projection = {
                Transacciones.ColumnasContactos.id,
                Transacciones.ColumnasContactos.nombre,
                Transacciones.ColumnasContactos.pais,
                Transacciones.ColumnasContactos.telefono,
                Transacciones.ColumnasContactos.notas,

        };
        String selection = Transacciones.ColumnasContactos.id + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                Transacciones.tablaContactos,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            contacto = new Contactos();
            contacto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Transacciones.ColumnasContactos.id)));
            contacto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.ColumnasContactos.nombre)));
            contacto.setPais(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.ColumnasContactos.pais)));
            contacto.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.ColumnasContactos.telefono)));
            contacto.setNotas(cursor.getString(cursor.getColumnIndexOrThrow(Transacciones.ColumnasContactos.notas)));
        }
        cursor.close();
        return contacto;
    }
    private void compartirContacto(Contactos contacto) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacto");
        intent.putExtra(Intent.EXTRA_TEXT, obtenerTextoContacto(contacto));
        startActivity(Intent.createChooser(intent, "Compartir contacto"));
    }

    private String obtenerTextoContacto(Contactos contacto) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(contacto.getNombre()).append("\n");
        sb.append("País: ").append(contacto.getPais()).append("\n");
        sb.append("Teléfono: ").append(contacto.getTelefono()).append("\n");
        sb.append("Notas: ").append(contacto.getNotas()).append("\n");
        return sb.toString();
    }
    private void actualizarContacto(int id, String nuevoNombre, String paisNombre, String nuevoTelefono, String nuevasNotas) {
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Transacciones.ColumnasContactos.nombre, nuevoNombre);
        values.put(Transacciones.ColumnasContactos.pais, paisNombre);
        values.put(Transacciones.ColumnasContactos.telefono, nuevoTelefono);
        values.put(Transacciones.ColumnasContactos.notas, nuevasNotas);
        String whereClause = Transacciones.ColumnasContactos.id + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(Transacciones.tablaContactos, values, whereClause, whereArgs);

        ObtenerTabla();

        Toast.makeText(getApplicationContext(), "Contacto actualizado", Toast.LENGTH_SHORT).show();
    }


    private void eliminarContacto(int id) {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String whereClause = Transacciones.ColumnasContactos.id + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(Transacciones.tablaContactos, whereClause, whereArgs);

        ObtenerTabla();

        Toast.makeText(getApplicationContext(), "Contacto eliminado", Toast.LENGTH_SHORT).show();
    }
    private void ObtenerTabla()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos person = null;
        lista = new ArrayList<Contactos>();
      Cursor cursor = db.rawQuery(Transacciones.SelectTableContactos,null);
        while(cursor.moveToNext())
        {
            person = new Contactos();
            person.setId(cursor.getInt(0));
            person.setNombre(cursor.getString(1));
            person.setPais(cursor.getString(2));
            person.setTelefono(cursor.getString(3));
            person.setNotas(cursor.getString(4));
            String imagePath = cursor.getString(5);


            lista.add(person);
        }
        cursor.close();
        fillList();

    }
    private void actualizarLista() {
        ObtenerTabla();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloPersonas);
        listpersonas.setAdapter(adp);
    }

    private void fillList()
    {
        ArregloPersonas = new ArrayList<String>();

        for(int i=0; i < lista.size(); i++)
        {
            ArregloPersonas.add(" | "
//                    +lista.get(i).getImg() + "|"
                     +lista.get(i).getId() + "|"
                    +lista.get(i).getPais() + "|"
                    +lista.get(i).getNombre() + " | "
                    +lista.get(i).getTelefono());
        }
    }
}