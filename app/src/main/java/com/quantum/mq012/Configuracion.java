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
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quantum.conectividad.Conexion;
import com.quantum.db.DbHelper;
import com.quantum.parseo.Cuerpo;

 public class Configuracion extends AppCompatActivity {

  /*private TextView direccion,qtm, conteo, ubi, manSerie, rest, restSel,baseD,CBD;
    public static String direc = null;
    public static String  nroConteoGoblal = null;
    public static String  ubicacionGoblal = null;
    public static String  serieGoblal = null;
    public static boolean  checkGlobal = false;
    public static boolean  checkGlobalLector = false;

    public static String restGlobal = "";

    private CheckBox ckbxCodItem, ckbxLector;

    private static  boolean  CDBnormal= true;

    FloatingActionButton btnBaseDatos;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

       direccion= findViewById(R.id.direccion);
        conteo= findViewById(R.id.nroConteo);

        ubi= findViewById(R.id.ubicacion);
        rest= findViewById(R.id.servicioRest);
        restSel= findViewById(R.id.restSeleccionado);

        ckbxCodItem = findViewById(R.id.checkBoxCodItem);
        ckbxLector = findViewById(R.id.checkBoxLector);
        baseD= findViewById(R.id.base);
        CBD= findViewById(R.id.cbd);

        if(CBD.getText().toString() == "0"){
            ckbxLector.setChecked(false);
        }else{
            ckbxLector.setChecked(true);
        }

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        direccion.setText(preferences.getString("direcciones",""));
        conteo.setText(preferences.getString("conteo",""));
        manSerie.setText(preferences.getString("serie",""));
        ubi.setText(preferences.getString("ubicacion",""));
        rest.setText(preferences.getString("rest",""));
        restSel.setText(preferences.getString("rest2",""));
        baseD.setText(preferences.getString("base",""));
        CBD.setText(preferences.getString("cbd",""));

        //ckbxLector.setChecked(true);

        direc = direccion.getText().toString();
        nroConteoGoblal = conteo.getText().toString();
        serieGoblal = manSerie.getText().toString();
        ubicacionGoblal = ubi.getText().toString();
        if(restGlobal.equals("")){
            restGlobal = "1";
        }else{
            restGlobal = rest.getText().toString();
        }
        ckbxCodItem.setChecked(true);


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
                                    Toast.makeText(Configuracion.this, "", Toast.LENGTH_LONG).show();
                                    baseD.setText("1");
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

        if(baseD.getText().toString().equals("1")){
            btnBaseDatos.setVisibility(View.INVISIBLE);
        }else  if(baseD.getText().toString().equals("0")){
            btnBaseDatos.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(Configuracion.this,"....", Toast.LENGTH_LONG).show();
        }
    }

      public void guardar (View view){
      SharedPreferences preferecias =  getSharedPreferences("dato",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();

        Obj_editor.putString("direcciones", direccion.getText().toString());
        Obj_editor.putString("conteo", conteo.getText().toString());
        Obj_editor.putString("serie", manSerie.getText().toString());
        Obj_editor.putString("ubicacion", ubi.getText().toString());
        Obj_editor.putString("rest", rest.getText().toString());
        Obj_editor.putString("rest2", restSel.getText().toString());
        Obj_editor.putString("base", baseD.getText().toString());
        Obj_editor.putString("cbd", CBD.getText().toString());

        Obj_editor.commit();

        Intent siguiente = new Intent(Configuracion.this, LoginActivity.class);


        siguiente.putExtra("direcciones", direccion.getText().toString());
        siguiente.putExtra("conteo", conteo.getText().toString());

        startActivity(siguiente);

        DbHelper dbHelper = new DbHelper(Configuracion.this);
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        if(db != null){
            Toast.makeText(Configuracion.this, "", Toast.LENGTH_LONG).show();
            baseD.setText("1");
        }else{
            Toast.makeText(Configuracion.this, "", Toast.LENGTH_LONG).show();

        }

        if(rest == null){
            Toast.makeText(Configuracion.this,"no se completó la configuración REST, se tomará el default", Toast.LENGTH_LONG).show();
        }else{
            restGlobal = rest.getText().toString();
        }

        if (ckbxCodItem.isChecked()==false){
            checkGlobal = false;
            checkGlobalLector = false;
        }  else if (ckbxCodItem.isChecked()==true){
            checkGlobal = true;
            checkGlobalLector = true;
        }else{
            Toast.makeText(Configuracion.this,"error", Toast.LENGTH_LONG).show();
        }
        if (ckbxLector.isChecked()==true){
            CBD.setText("1");
        }else{
            CBD.setText("0");
        }

        if (CBD.getText().toString().equals("0")){
            checkGlobalLector = false;
            //   Toast.makeText(Configuracion.this,"0", Toast.LENGTH_LONG).show();
        } else if  (CBD.getText().toString().equals("1")){
            checkGlobalLector = true;
            //   Toast.makeText(Configuracion.this,"1", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Configuracion.this,"..", Toast.LENGTH_LONG).show();
        }

    }*/

}