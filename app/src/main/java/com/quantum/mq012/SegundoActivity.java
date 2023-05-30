package com.quantum.mq012;

import static com.quantum.db.DbContactos.totalGoblal;
import static com.quantum.mq012.Configuracion.nroConteoGoblal;
import static com.quantum.mq012.Configuracion.restGlobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.adaptador.ListaContactosAdapter;
import com.quantum.db.DbContactos;
import com.quantum.entidades.Contactos;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SegundoActivity extends AppCompatActivity {

    Button limpieza;
    RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;
   TextView codigo,codigo2,qtm,titulo,dataBase,ubicacion,totales,totalInt;
    ListaContactosAdapter adapter;

     public static ProgressBar progresBar;

    public String pathDatabase = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo);

        codigo = findViewById(R.id.codigoInsertado);
        codigo2 = findViewById(R.id.codigoInsertado2);
        titulo = findViewById(R.id.conteo);
        limpieza = findViewById(R.id.limpiarTodo);
        totales = findViewById(R.id.total);
        totalInt = findViewById(R.id.totalInt);


        qtm = findViewById(R.id.qtm2);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );

        progresBar = findViewById(R.id.progressBar);
        dataBase = findViewById(R.id.DataBase);

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        //lo que aparece en la lista RECYCLER
        listaContactos =  findViewById(R.id.listaContactos);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));

        //guardar cantidad
        SharedPreferences preferences = getSharedPreferences("datos2", Context.MODE_PRIVATE);
        totales.setText(preferences.getString("total",""));


        //llamado de la clase para mostrar objetos
        DbContactos dbContactos = new DbContactos(SegundoActivity.this);
        listaArrayContactos = new ArrayList<>();
        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);

     /*   if(totalGoblal != 0){
            totales.setText(totalGoblal + "");
        }else{
            totales.setText( "");

        }*/
        progresBar.setVisibility(View.INVISIBLE);

        //mostrar el numero de conteo
        if(nroConteoGoblal != null){
            titulo.setText( nroConteoGoblal);

            mostrar();
            cantidad();
        }

        //para limpieza
        limpieza.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SegundoActivity.this);
            builder.setMessage("Desea eliminar los registros de la tabla? ")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbContactos.eliminarDatos();
                           mostrar();
                            cantidad();

                            //totales.setText(totalGoblal + "");
                        }
                    }) .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Toast.makeText(SegundoActivity.this,"No se hizo la limpieza", Toast.LENGTH_LONG).show();
                }
            }).show();
        });

        obtener();


    }
    public void obtener(){
        pathDatabase = getDatabasePath("conteo2.db").getAbsolutePath();
        dataBase.setText(pathDatabase);

    }

    public void cantidad(){
        DbContactos dbContactos = new DbContactos(SegundoActivity.this);
        dbContactos.getRowCount();
        totales.setText(totalGoblal + "");
    }

    public void actualizar(View v){
        cantidad();
        mostrar();
        Toast.makeText(SegundoActivity.this,"Actualizado", Toast.LENGTH_LONG).show();
    }

    //metodo para llamado de la base de datos
    public void mostrar(){
        DbContactos dbContactos = new DbContactos(SegundoActivity.this);
        listaArrayContactos = new ArrayList<>();
        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);

            SharedPreferences preferecias =  getSharedPreferences("datos2",Context.MODE_PRIVATE);
            SharedPreferences.Editor Obj_editor = preferecias.edit();
            Obj_editor.putString("total", totales.getText().toString());
            Obj_editor.commit();

    }
    public void enviar (View v){

        new AlertDialog.Builder(this)
                //.setIcon(R.drawable.alacran)
                .setTitle("¿Desea enviar todos los registros?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbContactos dbContactos = new DbContactos(SegundoActivity.this);
                        listaArrayContactos = new ArrayList<>();
                        adapter = new ListaContactosAdapter(dbContactos.enviarRegistros());
                        listaContactos.setAdapter(adapter);
                        mostrar();
                       /* progresBar.setMax(100);
                        for (int i = 0; i <= 100; i++) {
                            progresBar.setVisibility(View.VISIBLE);
                            progresBar.setProgress(i);
                            try {
                              //  progresBar.setVisibility(View.INVISIBLE);
                                Thread.sleep(90);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }*/
                        Toast.makeText(SegundoActivity.this,"Enviando, aguarde unos segundos", Toast.LENGTH_LONG).show();
                    }
                }).show();

    }

    public void Salir(View v){
        new AlertDialog.Builder(this)
                //.setIcon(R.drawable.alacran)
                .setTitle("¿Realmente desea cerrar la aplicación?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //android.os.Process.killProcess(android.os.Process.myPid()); //Su funcion es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                        finishAffinity();;
                    }
                }).show();
    }

    public void colectar(View v){
        Intent intent = new Intent (SegundoActivity.this, AgregarActivity.class);
        startActivity(intent);
    }

}
