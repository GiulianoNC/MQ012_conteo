package com.quantum.db;

/*import static com.quantum.mq012.Configuracion.direc;
import static com.quantum.mq012.Configuracion.nroConteoGoblal;
import static com.quantum.mq012.Configuracion.restGlobal;*/
import static com.quantum.mq012.LoginActivity.contraseñaGlobal;
import static com.quantum.mq012.LoginActivity.direc;
import static com.quantum.mq012.LoginActivity.nroConteoGoblal2;
import static com.quantum.mq012.LoginActivity.restGlobal;
import static com.quantum.mq012.LoginActivity.usuarioGlobal;
import static com.quantum.mq012.SegundoActivity.progresBar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.quantum.conectividad.Conexion;
import com.quantum.entidades.Contactos;
import com.quantum.parseo.Cuerpo;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//BASE DE DATOS
public class DbContactos extends DbHelper{

    Context context;
    public static  boolean  colorGlobal = false;
    public static int  totalGoblal  = 0;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertaContacto(String nombre, String item, String nro_serie, String ubicacion, String cantidad,String resultado){

        long id = 0;
        long respuesta = 0;
        //vamos a usar el try catch para que no se detenga si hay un error,
        try{
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //agregar la funcion para insertar el registro
        ContentValues values = new ContentValues();

                //inserto el nombre de la columna y despues el parametro
                values.put("nombre", nombre);
                values.put("item", item);
                values.put("Numero_Serie", nro_serie);
                values.put("ubicacion", ubicacion);
                values.put("cantidad", cantidad);
                values.put("resultado",resultado);
                //nos va a regresar el id insertado
                id = db.insert(TABLE_CONTEO, null, values);

        }catch (Exception ex){
            ex.toString();
        }
        return id;
    }

    //para contar la cantidad de registros
    public int getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTEO, null);
        int count = cursor.getCount();
        totalGoblal = count;
        cursor.close();
        return count;
    }



    public ArrayList<Contactos> mostrarContactos(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto ;
        Cursor cursorContactos,cursorContactos2 ;


        Cursor conteo = db.rawQuery("SELECT  COUNT(*) FROM " + TABLE_CONTEO + " ORDER BY nombre ASC", null);

        //consulta de contactos
        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTEO + " ORDER BY nombre ASC", null);

        if (cursorContactos.moveToFirst()){
            do{
                contacto =  new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setNombre(cursorContactos.getString(1));
                contacto.setItem(cursorContactos.getString(2));
                contacto.setNumero_Serie(cursorContactos.getString(3));
                contacto.setUbicacion(cursorContactos.getString(4));
                contacto.setCantidad(cursorContactos.getString(5));
                contacto.setResultado(cursorContactos.getString(6));
                listaContactos.add(contacto);

            }while(cursorContactos.moveToNext());
         //   totalGoblal = totalGoblal +1;
        }
        cursorContactos.close();
        //Toast.makeText(context," es :"  + totalGoblal,Toast.LENGTH_SHORT).show();;
        return listaContactos;
    }

    public ArrayList<Contactos> enviarRegistros (){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto ;
        Cursor cursorContactos ;

        //consulta de contactos
        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTEO + " ORDER BY nombre ASC", null);

        if (cursorContactos.moveToFirst()){
            do {
                contacto = new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setNombre(cursorContactos.getString(1));
                contacto.setItem(cursorContactos.getString(2));
                contacto.setNumero_Serie(cursorContactos.getString(3));
                contacto.setUbicacion(cursorContactos.getString(4));
                contacto.setCantidad(cursorContactos.getString(5));
                contacto.setResultado(cursorContactos.getString(6));
                listaContactos.add(contacto);

                //llamo a retrofit
                //agregado
                String ItemString = contacto.setItem(cursorContactos.getString(2));
                String SerieString = contacto.setNumero_Serie(cursorContactos.getString(3));
                String ubicacionString = contacto.setUbicacion(cursorContactos.getString(4));
                String CantidadString = contacto.setCantidad(cursorContactos.getString(5));

                int idInt = contacto.setId(cursorContactos.getInt(0));

                if (restGlobal.equals("1") || restGlobal.equals("")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {

                                Cuerpo cuerpo = response.body();
                                boolean prueba = cuerpo.getEstado();
                                //boolean prueba =  contactList.getSubmitted();
                                if (prueba == true) {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    ;
                                    mostrarContactos();

                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();
                                }
                            }else{
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó, revisar la configuración | servidor", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
                } else if (restGlobal.equals("2")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos2(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    ;
                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();
                                ;
                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;

                        }

                    });
                } else if (restGlobal.equals("3")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos3(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado" , Toast.LENGTH_SHORT).show();

                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado",  Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            } else if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();

                        }

                    });
                } else if (restGlobal.equals("4")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos4(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();

                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();


                        }

                    });
                } else if (restGlobal.equals("5")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();

                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos5(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();

                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();


                        }

                    });
                } else if (restGlobal.equals("6")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos6(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();

                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;

                        }

                    });
                } else if (restGlobal.equals("7")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos7(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();

                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;

                        }

                    });
                } else if (restGlobal.equals("8")) {
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal, nroConteoGoblal2 , ubicacionString, ItemString, SerieString, CantidadString);

                    Call<Cuerpo> call = conexion.getDatos8(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200) {
                                Cuerpo cuerpo = response.body();

                                boolean estado = cuerpo.getEstado();

                                if (estado == true) {

                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Procesado ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();

                                    mostrarContactos();


                                } else {
                                    editarContacto(idInt, ItemString, SerieString, ubicacionString, CantidadString, " Error     ");
                                    Toast.makeText(context, " Completado", Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }
                            if (statusCode != 200) {
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context, "No se conectó", Toast.LENGTH_SHORT).show();
                            ;

                        }

                    });
                }else if (restGlobal.equals("9")){
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(500, TimeUnit.SECONDS)
                            .connectTimeout(500, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal,  nroConteoGoblal2 , ubicacionString, ItemString,SerieString,CantidadString);

                    Call<Cuerpo> call = conexion.getDatos9(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200){
                                Cuerpo cuerpo =  response.body();

                                boolean estado = cuerpo.getEstado();

                                if(estado == true){

                                    editarContacto(idInt, ItemString,SerieString,ubicacionString, CantidadString," Procesado ");
                                    Toast.makeText(context," Completado"  ,Toast.LENGTH_SHORT).show();;
                                    mostrarContactos();


                                }else{
                                    editarContacto(idInt, ItemString,SerieString,ubicacionString,CantidadString," Error     ") ;
                                    Toast.makeText(context," Completado"  ,Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }if (statusCode != 200){
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context,"No se conectó",Toast.LENGTH_SHORT).show();;

                        }

                    });
                }else if (restGlobal.equals("10")){
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.SECONDS)
                            .connectTimeout(5000, TimeUnit.SECONDS)
                            .build();


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(direc)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                    Conexion conexion = retrofit.create(Conexion.class);

                    Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal,  nroConteoGoblal2 , ubicacionString, ItemString,SerieString,CantidadString);

                    Call<Cuerpo> call = conexion.getDatos10(login);
                    call.enqueue(new Callback<Cuerpo>() {
                        @Override
                        public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                            int statusCode = response.code();

                            if (statusCode <= 200){
                                Cuerpo cuerpo =  response.body();

                                boolean estado = cuerpo.getEstado();

                                if(estado == true){

                                    editarContacto(idInt, ItemString,SerieString,ubicacionString, CantidadString," Procesado ");
                                    Toast.makeText(context," Completado"  ,Toast.LENGTH_SHORT).show();;
                                    mostrarContactos();


                                }else{
                                    editarContacto(idInt, ItemString,SerieString,ubicacionString,CantidadString," Error     ") ;
                                    Toast.makeText(context," Completado"  ,Toast.LENGTH_SHORT).show();
                                    mostrarContactos();

                                }

                            }if (statusCode != 200){
                                Toast.makeText(context, " Error, revisar la configuración ", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Cuerpo> call, Throwable t) {
                            Toast.makeText(context,"No se conectó",Toast.LENGTH_SHORT).show();;

                        }

                    });
                }else{
                    Toast.makeText(context,"No se conectó",Toast.LENGTH_SHORT).show();;
                }

            }while(cursorContactos.moveToNext());

        }
        cursorContactos.close();
        return listaContactos;

    }

    public Contactos verContactos(int id){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto = null;
        Cursor cursorContactos ;

        //consulta de contactos
        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTEO + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorContactos.moveToFirst()){

                contacto =  new Contactos();
                contacto.setId(cursorContactos.getInt(0));
               contacto.setNombre(cursorContactos.getString(1));
               contacto.setItem(cursorContactos.getString(2));
               contacto.setNumero_Serie(cursorContactos.getString(3));
            contacto.setUbicacion(cursorContactos.getString(4));
            contacto.setCantidad(cursorContactos.getString(5));
            contacto.setResultado(cursorContactos.getString(6));



        }
        cursorContactos.close();
        return contacto;

    }

    public boolean editarContacto(int id,String item, String nro_serie, String ubicacion ,String  cantidad, String resultado){

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            //validar por el ID
            db.execSQL("UPDATE " + TABLE_CONTEO + " SET item = '" +
                    "', item = '" + item + "', Numero_Serie = '" + nro_serie+ "', ubicacion = '" + ubicacion+ "',cantidad = '" + cantidad+ "', resultado = '" +resultado + "', id = '" +id+"'WHERE id='" + id + "' ");
            correcto= true;
            progresBar.setVisibility(View.VISIBLE);

        }catch (Exception ex){
            ex.toString();
            correcto= false;

        }finally {
            //cierra la conexion
            progresBar.setVisibility(View.INVISIBLE);

            db.close();
        }
        return correcto;

    }

    public  boolean eliminarDatos(){
        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            //validar por el ID
            db.execSQL("DELETE FROM " + TABLE_CONTEO  );
            correcto = true;
        }catch (Exception ex){
            ex.toString();
            correcto= false;
        }finally {
            //cierra la conexion
            db.close();
        }
        return correcto;

    }

    public boolean eliminarContacto(int id){

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            //validar por el ID
            db.execSQL("DELETE FROM " + TABLE_CONTEO +  " WHERE id = '" + id + "'");
            correcto = true;
        }catch (Exception ex){
            ex.toString();
            correcto= false;
        }finally {
            //cierra la conexion
            db.close();
        }
        return correcto;
    }


}


