package com.quantum.db;

import static com.quantum.mq012.Configuracion.direc;
import static com.quantum.mq012.Configuracion.nroConteoGoblal;
import static com.quantum.mq012.LoginActivity.contraseñaGlobal;
import static com.quantum.mq012.LoginActivity.usuarioGlobal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.quantum.conectividad.Conexion;
import com.quantum.entidades.Contactos;
import com.quantum.parseo.Cuerpo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//BASE DE DATOS
public class DbContactos extends DbHelper{

    Context context;
    Contactos contacto;
    boolean correcto = false;
    public static  boolean  colorGlobal = false;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertaContacto(String nombre, String item, String nro_serie, String resultado){

        long id = 0;
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
            values.put("resultado",resultado);




            //nos va a regresar el id insertado
         id = db.insert(TABLE_CONTEO, null, values);
        }catch (Exception ex){
            ex.toString();
        }
        return id;
    }

    public ArrayList<Contactos> mostrarContactos(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto ;
        Cursor cursorContactos ;

        //consulta de contactos
        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTEO + " ORDER BY nombre ASC", null);

        if (cursorContactos.moveToFirst()){
            do{
                contacto =  new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setNombre(cursorContactos.getString(1));
                contacto.setItem(cursorContactos.getString(2));
                contacto.setNumero_Serie(cursorContactos.getString(3));
                contacto.setResultado(cursorContactos.getString(4));
                listaContactos.add(contacto);

            }while(cursorContactos.moveToNext());
        }
        cursorContactos.close();
        return listaContactos;

    }

    public ArrayList<Contactos> mostrarContactos2(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto ;
        Cursor cursorContactos ;

        //consulta de contactos
        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTEO + " ORDER BY nombre ASC", null);

        if (cursorContactos.moveToFirst()){
            do{
                contacto =  new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setNombre(cursorContactos.getString(1));
                contacto.setItem(cursorContactos.getString(2));
                contacto.setNumero_Serie(cursorContactos.getString(3));
                contacto.setResultado(cursorContactos.getString(4));
                listaContactos.add(contacto);

                //llamo a retrofit
                //agregado
                String ItemString = contacto.setItem(cursorContactos.getString(2));
                String SerieString = contacto.setNumero_Serie(cursorContactos.getString(3));
                int idInt =   contacto.setId(cursorContactos.getInt(0));


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(direc)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Conexion conexion = retrofit.create(Conexion.class);

                Cuerpo login = new Cuerpo(usuarioGlobal, contraseñaGlobal,  nroConteoGoblal,ItemString,SerieString);

                Call<Cuerpo> call = conexion.getDatos(login);
                call.enqueue(new Callback<Cuerpo>() {
                    @Override
                    public void onResponse(Call<Cuerpo> call, Response<Cuerpo> response) {
                        int statusCode = response.code();

                        if (statusCode <= 200){
                            Cuerpo cuerpo =  response.body();

                            String estado = cuerpo.getEstado();

                            if(estado.equals("true")){

                                editarContacto(idInt, ItemString,SerieString,"procesado");
                                Toast.makeText(context," Completado"  ,Toast.LENGTH_SHORT).show();;
                                mostrarContactos();
                                colorGlobal = false;


                            }else{
                                editarContacto(idInt, ItemString,SerieString,"error") ;
                                Toast.makeText(context," Completado"  ,Toast.LENGTH_SHORT).show();
                                mostrarContactos();
                                colorGlobal = true;

                            }
                        }if (statusCode != 200){
                        Toast.makeText(context," error en la conexion"  ,Toast.LENGTH_SHORT).show();;
                    }
                    }

                    @Override
                    public void onFailure(Call<Cuerpo> call, Throwable t) {
                        Toast.makeText(context,"No se conectó",Toast.LENGTH_SHORT).show();;

                    }
                });

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
               contacto.setResultado(cursorContactos.getString(4));



        }
        cursorContactos.close();
        return contacto;

    }

    public boolean editarContacto(int id,String item, String nro_serie, String resultado){

        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{

            //validar por el ID
            db.execSQL("UPDATE " + TABLE_CONTEO + " SET item = '" +
                    "', item = '" + item + "', Numero_Serie = '" + nro_serie+ "', resultado = '" +resultado + "', id = '" +id+"'WHERE id='" + id + "' ");
            correcto= true;

        }catch (Exception ex){
            ex.toString();
            correcto= false;
        }finally {
            //cierra la conexion
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


