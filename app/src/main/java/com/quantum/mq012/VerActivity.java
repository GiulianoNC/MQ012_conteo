package com.quantum.mq012;

import static com.quantum.db.DbContactos.totalGoblal;
import static com.quantum.mq012.LoginActivity.nroConteoGoblal2;
//import static com.quantum.mq012.Configuracion.nroConteoGoblal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quantum.db.DbContactos;
import com.quantum.entidades.Contactos;

//en esta clase es para poder ver,editar,eliminar  lo seleccionado
public class VerActivity extends AppCompatActivity {

    TextView viewItem,viewNroSerie,viewResultado,qtm,titulo,idShow,viewCantidad,fabEliminar;
    Contactos contacto;
    int  id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        viewItem = findViewById(R.id.ItemInsertado);
        viewNroSerie = findViewById(R.id.SerieInsertada);
        idShow = findViewById(R.id.idMostrar);
        viewCantidad = findViewById(R.id.cantidadInsertada);

        //btnGuarda = findViewById(R.id.btnGuarda);
        fabEliminar = findViewById(R.id.FabEliminar);
        viewResultado = findViewById(R.id.resultado);
        titulo = findViewById(R.id.conteoB);


        qtm = findViewById(R.id.qtm3);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );

        viewCantidad .requestFocus();

        //mostrar el numero de conteo
        if(nroConteoGoblal2 != null){
            titulo.setText( nroConteoGoblal2);
        }

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        //hago una llamada a lo seleccionado  atra ves de la base de datos mediante  y el ID
        if(savedInstanceState == null){
            Bundle extras  = getIntent().getExtras();
            if(extras == null ){
                id = Integer.parseInt(null);
            }else{
                id= extras.getInt("ID");
                idShow.setText(id +"" );
            }
            }else{
            id = (int ) savedInstanceState.getSerializable("ID");
        }
        final DbContactos dbContactos = new DbContactos( VerActivity.this);
        contacto = dbContactos.verContactos(id);


        if(contacto != null){
            viewItem.setText(contacto.getItem());
            viewNroSerie.setText(contacto.getNumero_Serie());
            viewCantidad.setText(contacto.getCantidad());
            viewResultado.setText(contacto.getResultado());

            viewItem.setInputType(InputType.TYPE_NULL);
            viewNroSerie.setInputType(InputType.TYPE_NULL);
            viewResultado.setInputType(InputType.TYPE_NULL);
            viewCantidad.setInputType(InputType.TYPE_NULL);

        }else{
            Toast.makeText(VerActivity.this,"no hay datos", Toast.LENGTH_LONG).show();
        }

        //elimino un registro
        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder( VerActivity.this);
                builder.setMessage("Desea eliminar este registro?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dbContactos.eliminarContacto(id)){
                                    totalGoblal = totalGoblal - 1;
                                    lista();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(VerActivity.this,"no se eliminó regristro", Toast.LENGTH_LONG).show();
                            }
                        }).show();
            }
        });

      /*  //guardar registro
        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!viewItem.getText().toString().equals("") &&!viewNroSerie.getText().toString().equals("") ){
                    if(viewResultado.length() == 0){
                        viewResultado.setText("procesado");
                    }
                    correcto =    dbContactos.editarContacto(id, viewItem.getText().toString(), viewNroSerie.getText().toString(), viewCantidad.getText().toString(),viewResultado.getText().toString());
                    Toast.makeText(VerActivity.this,"REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VerActivity.this, SegundoActivity.class);

                    startActivity(intent);

                }else{

                    Toast.makeText(VerActivity.this,"DEBE LLENAR LOS CAMPOS", Toast.LENGTH_LONG).show();
                }

            }
        }); */

    }

    //si se elimina llamo a esto para volver a la segunda clase
    private void lista(){
        Intent intent = new Intent(this, SegundoActivity.class);
        startActivity(intent);
    }

    //para volver
    public void Salir(View v){
        Intent intent = new Intent(VerActivity.this, SegundoActivity.class);
        startActivity(intent);
    }


}