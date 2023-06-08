package com.quantum.mq012;

/*import static com.quantum.mq012.Configuracion.direc;
import static com.quantum.mq012.Configuracion.nroConteoGoblal;
import static com.quantum.mq012.Configuracion.restGlobal;
import static com.quantum.mq012.Configuracion.ubicacionGoblal;*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quantum.conectividad.Conexion;
import com.quantum.db.DbHelper;
import com.quantum.parseo.Cuerpo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView user, contraseña,urls,hand;
    TableLayout  logueo, config;

    public static String usuarioGlobal = null;
    public static String contraseñaGlobal = null;
    private TextView qtm;

    Switch switcher;
    boolean nightMODE;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //CONFIGURACION
    private TextView direccionL, conteoL, ubiL, restL, restSelL,baseDL;
    public static String direc = null;
    public static String  ubicacionGoblal = null;
    public static String  serieGoblal = null;
    public static boolean  checkGlobal = false;
    public static boolean  checkGlobalLector = false;
    public static boolean  handHeldGlobal = false;
    public static String  nroConteoGoblal2 = null;
    public static String  visible = null;

    public static String restGlobal = "";

    private CheckBox ckbxCodItem, ckbxLector,handHeldLector;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ingreso
        user = findViewById(R.id.Usuario);
        contraseña = findViewById(R.id.contras);

        //CONFIGURACION
        direccionL= findViewById(R.id.direccion2);
        conteoL= findViewById(R.id.nroConteo2);
        ubiL= findViewById(R.id.ubicacion2);
        restL= findViewById(R.id.servicioRest2);
        restSelL= findViewById(R.id.restSeleccionado2);
        ckbxCodItem = findViewById(R.id.checkBoxCodItem2);
        ckbxLector = findViewById(R.id.checkBoxLector3);
        handHeldLector = findViewById(R.id.handheld);
        baseDL= findViewById(R.id.base2);
        hand = findViewById(R.id.handText);
        //ckbxLector.setChecked(true);

        //TableLayout
        logueo= findViewById(R.id.logueo);
        config= findViewById(R.id.configuracion);

        //guardar ingreso datos
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        user.setText(preferences.getString("usuario",""));
        contraseña.setText(preferences.getString("password",""));
        hand.setText(preferences.getString("hand",""));

        //guardar config datos
        SharedPreferences preferences2 = getSharedPreferences("dato", Context.MODE_PRIVATE);
        direccionL.setText(preferences2.getString("direcciones",""));
        conteoL.setText(preferences2.getString("conteo",""));
        //manSerieL.setText(preferences2.getString("serie",""));
        ubiL.setText(preferences2.getString("ubicacion",""));
        restL.setText(preferences2.getString("rest",""));
        restSelL.setText(preferences2.getString("rest2",""));
        baseDL.setText(preferences2.getString("base",""));

        urls = findViewById(R.id.dir);
        String direccion = getIntent().getStringExtra("direcciones");
        urls.setText(direccion);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        //titulo
        qtm = findViewById(R.id.QTMtitulo);
        qtm.setText("QTM -  CONTEO   " + "\n" + "      CICLICO" );

        //Esto es el Day/Night Mode
        //Uso el SharedPreference para guardar el modo cuando salgo de la pagina
        switcher = findViewById(R.id.btnToggleDark);
        sharedPreferences = getSharedPreferences("MODE",Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night",false); //El modo luz es el default

        if (nightMODE){
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",true);
                }
                editor.apply();
            }
        });
        ckbxCodItem.setChecked(true);

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        if(user.length() == 0){
            config.setVisibility(View.INVISIBLE);
        }else{
            logueo.setVisibility(View.INVISIBLE);
        }
        if(visible == "1"){

            logueo.setVisibility(View.VISIBLE);
            config.setVisibility(View.INVISIBLE);

         }else{
            logueo.setVisibility(View.INVISIBLE);
            config.setVisibility(View.VISIBLE);

        }
    }
    //globales
    public void global(){
        usuarioGlobal = user.getText().toString();
        contraseñaGlobal = contraseña.getText().toString();
        nroConteoGoblal2 =  conteoL.getText().toString();
        ubicacionGoblal = ubiL.getText().toString();

        String direcion = urls.getText().toString();
        direc =direcion;
        if(restGlobal.equals("")){
            restGlobal = "1";
        }else{
            restGlobal = restL.getText().toString();
        }
        if(ckbxLector.isChecked()){
            checkGlobalLector = true;
        }else{
            restGlobal = restL.getText().toString();
        }
        // handHeldLector.setChecked(false);

        if(handHeldLector.isChecked()){
            Toast.makeText(this, "activado", Toast.LENGTH_SHORT).show();
            hand.setText("1");
        }else{
            hand.setText("0");
        }

        if(hand.getText().toString().equals( "1")){
            handHeldLector.setChecked(true);
            handHeldGlobal = true;
            Toast.makeText(this, "activado", Toast.LENGTH_SHORT).show();
        }else{
            handHeldLector.setChecked(false);
            handHeldGlobal = false;
        }

        //asignaciones globales
        direc = direccionL.getText().toString();
        nroConteoGoblal2 = conteoL.getText().toString();
        ubicacionGoblal = ubiL.getText().toString();
    }
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //acciones de menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_privacidad:

                String url = "https://quantumconsulting.com.ar/politicas-de-privacidad/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                break;

            case R.id.action_configuracion:
               /* Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);

                startActivity(siguiente);*/
                logueo.setVisibility(View.INVISIBLE);
                config.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ingresar (View v){

        String user2 = user.getText().toString();
        String contraseña2 = contraseña.getText().toString();
        String conteo = conteoL.getText().toString();

        String direccion =direccionL.getText().toString();
        urls.setText(direccion);

        if (user2.length() == 0 && contraseña2.length() == 0) {
            Toast.makeText(this, "Debes ingresar un usuario y contraseña", Toast.LENGTH_SHORT).show();
        }
        if (user2.length() != 0 && contraseña2.length() != 0) {

            if (urls.length() == 0)  {
             /*   Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);
                startActivity(siguiente);*/
                logueo.setVisibility(View.INVISIBLE);
                config.setVisibility(View.VISIBLE);
            }else{

                Toast.makeText(LoginActivity.this, "Procesando", Toast.LENGTH_SHORT).show();

                usuarioGlobal = user.getText().toString();
                contraseñaGlobal = contraseña.getText().toString();
                nroConteoGoblal2 =  conteoL.getText().toString();
                ubicacionGoblal = ubiL.getText().toString();
                direc = direccion;


                if(restGlobal.equals("1") || restGlobal.equals("")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direccion)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,conteo ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else  if(restGlobal.equals("2")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos2(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(restGlobal.equals("3")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos3(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(restGlobal.equals("4")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos4(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(restGlobal.equals("5")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos5(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(restGlobal.equals("6")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos6(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(restGlobal.equals("7")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos7(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else  if(restGlobal.equals("8")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos8(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(restGlobal.equals("9")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos9(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(restGlobal.equals("10")){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);
                    Cuerpo logerse = new Cuerpo(usuarioGlobal, contraseñaGlobal,nroConteoGoblal2 ,"1","12" );

                    Call<Cuerpo> call1 = conexion.getDatos10(logerse);
                    call1.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (response.isSuccessful()){
                                if(statusCode <= 200){
                                    Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);
                                    startActivity(siguiente);
                                }}
                            if(statusCode != 200){
                                Toast.makeText(LoginActivity.this, "chequear la conexión, y|o los datos ingresados "  , Toast.LENGTH_SHORT).show();
                            }}
                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed  "  , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Completar en configuración los datos "  , Toast.LENGTH_SHORT).show();
                }

            }
        }
        SharedPreferences preferecias =  getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();
        Obj_editor.putString("usuario", user.getText().toString());
        Obj_editor.putString("password", contraseña.getText().toString());
        Obj_editor.putString("hand", hand.getText().toString());

        Obj_editor.commit();

        global();
    }

    public void configuracion(View v){

        SharedPreferences preferecias =  getSharedPreferences("dato",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();

        if(direccionL.length() != 0){
            logueo.setVisibility(View.VISIBLE);
            config.setVisibility(View.INVISIBLE);
        }

        Obj_editor.putString("direcciones", direccionL.getText().toString());
        Obj_editor.putString("conteo", conteoL.getText().toString());
      //  Obj_editor.putString("serie", manSerieL.getText().toString());
        Obj_editor.putString("ubicacion", ubiL.getText().toString());
        Obj_editor.putString("rest", restL.getText().toString());
        Obj_editor.putString("rest2", restSelL.getText().toString());
     //   Obj_editor.putString("base", baseDL.getText().toString());
     //   Obj_editor.putString("cbd", CBDL.getText().toString());

        Obj_editor.commit();

        Intent siguiente = new Intent(LoginActivity.this, SegundoActivity.class);

        siguiente.putExtra("direcciones", direccionL.getText().toString());
        siguiente.putExtra("conteo", conteoL.getText().toString());

        direc = direccionL.getText().toString();
        nroConteoGoblal2 = conteoL.getText().toString();
        ubicacionGoblal = ubiL.getText().toString();
        usuarioGlobal = user.getText().toString();
        contraseñaGlobal = contraseña.getText().toString();


        startActivity(siguiente);

        DbHelper dbHelper = new DbHelper(LoginActivity.this);
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        if(db != null){
            baseDL.setText("1");
        }else{
            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();

        }

        if(restL == null){
            Toast.makeText(LoginActivity.this,"no se completó la configuración REST, se tomará el default", Toast.LENGTH_LONG).show();
        }else{
            restGlobal = restL.getText().toString();
        }

        if (ckbxCodItem.isChecked()==false){
            checkGlobal = false;
            checkGlobalLector = false;
        }  else if (ckbxCodItem.isChecked()==true){
            checkGlobal = true;
            checkGlobalLector = true;
        }else{
            Toast.makeText(LoginActivity.this,"error3", Toast.LENGTH_LONG).show();
        }

        if (handHeldLector.isChecked()==false){
            handHeldGlobal = false;
        }  else if (handHeldLector.isChecked()==true){
            handHeldGlobal = true;
            handHeldLector.setChecked(true);
        }else{
            Toast.makeText(LoginActivity.this,"error5", Toast.LENGTH_LONG).show();
        }
    }
}
