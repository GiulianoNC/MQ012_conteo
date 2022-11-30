package com.quantum.mq012;

import static com.quantum.mq012.Configuracion.nroConteoGoblal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.adaptador.ListaContactosAdapter;
import com.quantum.db.DbContactos;
import com.quantum.entidades.Contactos;

import java.util.ArrayList;

public class SegundoActivity extends AppCompatActivity {

    Button btn,limpieza;
    RecyclerView listaContactos,listaId;
    ArrayList<Contactos> listaArrayContactos,listaArrayID;
   TextView codigo,codigo2,conteo,qtm,titulo;
    ListaContactosAdapter adapter;


    //cuando selecciono
    TextView viewItem,viewNroSerie,viewResultado,idShow;
    boolean correcto = false;
    Contactos contacto;
    int  id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo);

        codigo = findViewById(R.id.codigoInsertado);
        codigo2 = findViewById(R.id.codigoInsertado2);
        titulo = findViewById(R.id.conteo);
        limpieza = findViewById(R.id.limpiarTodo);

        qtm = findViewById(R.id.qtm2);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        //lo que aparece en la lista RECYCLER
        listaContactos = (RecyclerView) findViewById(R.id.listaContactos);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));


        //llamado de la clase para mostrar objetos
        DbContactos dbContactos = new DbContactos(SegundoActivity.this);
        listaArrayContactos = new ArrayList<>();
        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);


        //mostrar el numero de conteo
        if(nroConteoGoblal != null){
            titulo.setText( nroConteoGoblal);

            mostrar();
        }

        //para crear base de datos
        limpieza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SegundoActivity.this);
                builder.setMessage("Desea eliminar los registros de la tabla? ")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbContactos.eliminarDatos();
                                mostrar();
                            }
                        }) .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(SegundoActivity.this,"No se hizo la limpieza", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });

    }
    public void actualizar(View v){

        mostrar();
        Toast.makeText(SegundoActivity.this,"Actualizado", Toast.LENGTH_LONG).show();

    }

    //metodo para llamado de la base de datos
    public void mostrar(){
        DbContactos dbContactos = new DbContactos(SegundoActivity.this);
        listaArrayContactos = new ArrayList<>();
        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);

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
                        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos2());
                        listaContactos.setAdapter(adapter);

                        mostrar();

                        Toast.makeText(SegundoActivity.this,"Enviando", Toast.LENGTH_LONG).show();
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
