package com.quantum.mq012;

import static com.quantum.mq012.Configuracion.nroConteoGoblal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.db.DbContactos;
import com.quantum.entidades.Contactos;

public class AgregarActivity extends AppCompatActivity {

    TextView item, serie,qtm,titulo,idMostrar;
    int  id = 0;
    Contactos contacto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agragar);

        item = findViewById(R.id.itenIn);
        serie = findViewById(R.id.serieIn);
        titulo = findViewById(R.id.conteoC);
        idMostrar = findViewById(R.id.idMos);



        qtm = findViewById(R.id.qtm4);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );

        //mostrar el numero de conteo
        if(nroConteoGoblal != null){
            titulo.setText( nroConteoGoblal);
        }


        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color



    }



    //para agregar items
    public void agregar(View v){

        DbContactos dbContactos = new DbContactos(AgregarActivity.this);
        long id  =dbContactos.insertaContacto(nroConteoGoblal,item.getText().toString(),  serie.getText().toString(), "Pending");

        if (id >0 && item.length() != 0  && serie.length() != 0 ){
            Toast.makeText(AgregarActivity.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
            limpiar();
        }else{
            Toast.makeText(AgregarActivity.this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_LONG).show();

        }
    }

    //limpia los textViews de item y serie
    private void limpiar (){
        serie.setText("");
        item.setText("");

    }

    //para volver
    public void Salir(View v){
        Intent intent = new Intent(AgregarActivity.this, SegundoActivity.class);

        startActivity(intent);
    }
}