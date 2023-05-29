package com.quantum.mq012;

import static com.quantum.mq012.Configuracion.checkGlobal;
import static com.quantum.mq012.Configuracion.checkGlobalLector;
import static com.quantum.mq012.Configuracion.nroConteoGoblal;
import static com.quantum.mq012.Configuracion.ubicacionGoblal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.quantum.db.DbContactos;
import com.quantum.entidades.Contactos;

import java.util.Timer;
import java.util.TimerTask;

public class AgregarActivity extends AppCompatActivity {

    TextView item, serie,qtm,titulo,idMostrar,cantidad,ubicacion,cantiT, qrInfo,colectadoQ,colectado;
    int  id = 0;
    Contactos contacto;
    Button qr, ok;

    int conteo ;

    public static int  nroGoblal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agragar);

        item = findViewById(R.id.itenIn);
        serie = findViewById(R.id.serieIn);
        ubicacion = findViewById(R.id.ubicacionA);
        cantidad = findViewById(R.id.cant);
        cantiT = findViewById(R.id.viewNombre4);

        qr = findViewById(R.id.qrImage);
        qrInfo = findViewById(R.id.qrInfo);
        titulo = findViewById(R.id.conteoC);
        idMostrar = findViewById(R.id.idMos);
        colectadoQ = findViewById(R.id.colectado);
        ok = findViewById(R.id.btOK);
        colectado = findViewById(R.id.colectado2);


        qtm = findViewById(R.id.qtm4);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );


        if(checkGlobalLector == false){
            colectadoQ.setVisibility(View.INVISIBLE);
            ok.setVisibility(View.INVISIBLE);
            serie .requestFocus();
        }else{
            colectadoQ .requestFocus();
            colectadoQ.setVisibility(View.INVISIBLE);
            ok.setVisibility(View.INVISIBLE);
        }

        //mostrar el numero de conteo
        if(nroConteoGoblal != null){
            titulo.setText( nroConteoGoblal);
        }
        if(ubicacionGoblal != null){
            ubicacion.setText( ubicacionGoblal);
        }
        if(checkGlobal == true){
            cantidad.setVisibility(View.INVISIBLE);
            cantiT.setVisibility(View.INVISIBLE);
            cantidad.setText("1");
        }

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color


        //para agregar instantaneo cada cinco segundos
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                agregar3();
            }
        }, 0, 5000);
    }


    public void scan(View v){
        IntentIntegrator intentIntegrator = new IntentIntegrator(AgregarActivity.this);
        //tipo de lector
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        //que va a decir el lector
        intentIntegrator.setPrompt("Lector - Código");
        //que camara usa, en este caso la 0 es la de atras
        intentIntegrator.setCameraId(0);
        //dispositivos, alertas de sonido
        intentIntegrator.setBeepEnabled(true);
        //para leer correctamente
        intentIntegrator.setBarcodeImageEnabled(true);
        //inicia el elemento de scaneo
        intentIntegrator.initiateScan();
    }

    public void scan2(View v){
        IntentIntegrator intentIntegrator2 = new IntentIntegrator(AgregarActivity.this);
        //tipo de lector
        intentIntegrator2.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        //que va a decir el lector
        intentIntegrator2.setPrompt("Lector - Código");
        //que camara usa, en este caso la 0 es la de atras
        intentIntegrator2.setCameraId(0);
        //dispositivos, alertas de sonido
        intentIntegrator2.setBeepEnabled(true);
        //para leer correctamente
        intentIntegrator2.setBarcodeImageEnabled(true);
        //inicia el elemento de scaneo
        intentIntegrator2.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //recibir el resultado de los parametros de arriba
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        IntentResult result2 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null  ){
            if(result.getContents() == null ){
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            }else{
                if(qrInfo.equals("")){
                    qrInfo.setText("");
                }else{
                    qrInfo.setText(result.getContents());
                    if(checkGlobalLector == false){

                        if(serie.isFocused() == true){
                            serie.setText(result.getContents());
                        }else if(item.isFocused() == true){
                            item.setText(result.getContents());
                        } else {
                            Toast.makeText(this, "completar campos", Toast.LENGTH_LONG).show();
                        }
                    }else{
                       // String qrString = qrInfo.getText().toString();
                        colectadoQ.setText(result.getContents());
                        String qrString = colectadoQ.getText().toString();
                        if(colectadoQ.length() < 24 ){
                            Toast.makeText(AgregarActivity.this, "debe ser al menos 24  caracteres", Toast.LENGTH_SHORT).show();
                        }else{
                            ok();
                        }


                   /*     String subcadena = qrString.substring(10, 20);
                           String string = subcadena;
                        String[] parts = string.split("-");
                        String itemString = parts[0];
                        String serieString = parts[1];
                        String ubicacionString = parts[2];
                        item.setText(itemString);
                        serie.setText(serieString);
                        ubicacion.setText(ubicacionString);*/
                    }
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void ok(){
        String qrString = colectadoQ.getText().toString();
        String subcadena = qrString.substring(10, 23);
        serie.setText(subcadena);
        colectado.setText(subcadena);
        agregar2();
        /*
        String string = qrString;
        String[] parts = string.split("-");
        String itemString = parts[0];
        String serieString = parts[1];
        String ubicacionString = parts[2];
        item.setText(itemString);
        serie.setText(serieString);
        ubicacion.setText(ubicacionString);*/
    }

    //para agregar items
    public void agregar(View v){

        if ( id >0 && item.length() == 0  && serie.length() == 0 && ubicacion.length() == 0  ){
            Toast.makeText(AgregarActivity.this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_SHORT).show();

        }else if(id >0 && item.length() != 0  || serie.length() != 0 || ubicacion.length() != 0  ){
            String palletString = serie.getText().toString();
            String strNew =  palletString.replace(" ", "");

            DbContactos dbContactos = new DbContactos(AgregarActivity.this);
            long id  =dbContactos.insertaContacto("nombre",item.getText().toString(),  strNew, ubicacion.getText().toString(),cantidad.getText().toString(),"  P  ");

            }else{
            Toast.makeText(AgregarActivity.this, "cargue al menos un campo", Toast.LENGTH_SHORT).show();
            }
            limpiar();
        }

    public void agregar2(){

        if ( id >0 && item.length() == 0  && serie.length() == 0 && ubicacion.length() == 0  ){
            Toast.makeText(AgregarActivity.this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_SHORT).show();

        }else if(id >0 && item.length() != 0  || serie.length() != 0 || ubicacion.length() != 0  ){
            String palletString = serie.getText().toString();
            String strNew =  palletString.replace(" ", "");

            DbContactos dbContactos = new DbContactos(AgregarActivity.this);
            long id  =dbContactos.insertaContacto("nombre",item.getText().toString(),  strNew, ubicacion.getText().toString(),cantidad.getText().toString(),"  P  ");

            Toast.makeText(this, "Registro Guardado", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(AgregarActivity.this, "cargue al menos un campo", Toast.LENGTH_SHORT).show();
        }
        limpiar();
    }

    public void agregar3(){

        if(serie.length() != 0){
            String subcadena = serie.getText().toString();
            colectado.setText(subcadena);

            if ( id >0 && item.length() == 0  && serie.length() == 0 && ubicacion.length() == 0  ){
                Toast.makeText(AgregarActivity.this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_SHORT).show();

            }else if(id >0 && item.length() != 0  || serie.length() != 0 || ubicacion.length() != 0  ){
                String palletString = serie.getText().toString();
                String strNew =  palletString.replace(" ", "");

                DbContactos dbContactos = new DbContactos(AgregarActivity.this);
                long id  =dbContactos.insertaContacto("nombre",item.getText().toString(),  strNew, ubicacion.getText().toString(),cantidad.getText().toString(),"  P ");

            }else{
                Toast.makeText(AgregarActivity.this, "cargue al menos un campo", Toast.LENGTH_SHORT).show();
            }
            limpiar();
        }

    }


    //limpia los textViews de item y serie
    private void limpiar (){
        serie.setText("");
        item.setText("");
        cantidad.setText("");
        colectadoQ.setText("");

    }
    //para volver
    public void Salir(View v){
        Intent intent = new Intent(AgregarActivity.this, SegundoActivity.class);
        startActivity(intent);
    }
}