package com.quantum.mq012;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quantum.db.DbHelper;

public class Configuracion extends AppCompatActivity {
    private TextView direccion,qtm, conteo;

    public static String direc = null;
    public static String  nroConteoGoblal = null;

    FloatingActionButton btnBaseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        direccion= findViewById(R.id.direccion);
        conteo= findViewById(R.id.nroConteo);

        btnBaseDatos= findViewById(R.id.btnBaseDatos);


        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        direccion.setText(preferences.getString("direcciones",""));
        conteo.setText(preferences.getString("conteo",""));

        direc = direccion.getText().toString();
        nroConteoGoblal = conteo.getText().toString();

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        qtm = findViewById(R.id.QTM);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );

        //para crear base de datos
        btnBaseDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Configuracion.this);
                builder.setMessage("Desea crear una base de datos? ")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DbHelper dbHelper = new DbHelper(Configuracion.this);
                                SQLiteDatabase db =  dbHelper.getWritableDatabase();
                                if(db != null){
                                    Toast.makeText(Configuracion.this, "se ha completado la creacion  de la BASE DE  DATOS", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(Configuracion.this, "ERROR", Toast.LENGTH_LONG).show();

                                }
                            }
                        }) .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(Configuracion.this,"no se creó la base de datos", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
    }

    public void guardar (View view){
        SharedPreferences preferecias =  getSharedPreferences("dato",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();

        Obj_editor.putString("direcciones", direccion.getText().toString());
        Obj_editor.putString("conteo", conteo.getText().toString());



        Obj_editor.commit();


        Intent siguiente = new Intent(Configuracion.this, LoginActivity.class);


        siguiente.putExtra("direcciones", direccion.getText().toString());
        siguiente.putExtra("conteo", conteo.getText().toString());



        startActivity(siguiente);


    }
}