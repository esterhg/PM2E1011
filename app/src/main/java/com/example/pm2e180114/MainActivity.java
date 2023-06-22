package com.example.pm2e180114;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e180114.data.SQLiteConexion;
import com.example.pm2e180114.data.Transacciones;

public class MainActivity extends AppCompatActivity {
    public Spinner spinner;
    public ImageView foto;
    public Button btnlista,guardar;
    public EditText nombre,telefono,notas,pais;
    private Uri imageUri;
    String paisSeleccionado;
    ImageView contactImageImgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); btnlista=(Button) findViewById(R.id.btnlista);
        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), lista.class);
                startActivity(intent);
            }
        });

        spinner=(Spinner)findViewById(R.id.txtspinner);
        String[] pais={"-Selecccionar-","+502 Guatemala","+503 El Salvador","+504 Honduras","+506 Costa Rica"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,pais);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paisSeleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nombre=findViewById(R.id.txtnombre);
        telefono=(EditText)findViewById(R.id.txtnumero);
        notas=(EditText)findViewById(R.id.txtnota);
        guardar = (Button) findViewById(R.id.btnguardar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPerson();
            }
        });
        contactImageImgView=(ImageView) findViewById(R.id.contactImageImgView);
        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select Contacto Image"),1);


            }
        });

    }

    public void  onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                contactImageImgView.setImageURI(imageUri);
            }
        }
        super.onActivityResult(reqCode, resCode, data);
    }


    private void AddPerson() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        String nombreText = nombre.getText().toString();
        String notasText = notas.getText().toString();
        String telefonoText = telefono.getText().toString();

        if (nombreText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe de Ingresar un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (paisSeleccionado.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe de selecionar pais", Toast.LENGTH_SHORT).show();
            return;
        }

        if (notasText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese notas", Toast.LENGTH_SHORT).show();
            return;
        }

        if (telefonoText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe de ingresar un numero", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.pais, paisSeleccionado);
        valores.put(Transacciones.notas , notas.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());

        if (imageUri != null) {
            valores.put(Transacciones.imagen, imageUri.toString());
        }

        Long result = db.insert(Transacciones.tablaContactos, Transacciones.id, valores);
        Toast.makeText(getApplicationContext(), "Registro ingresado : " + result.toString(),Toast.LENGTH_LONG ).show();

        db.close();
        CleanScreen();

    }

    private void CleanScreen()
    {
        nombre.setText("");
        notas.setText("");
        telefono.setText("");
        spinner.setSelection(0);
    }

}