package com.quantum.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int  DATABASE_VERSION = 1;
    private static final String  DATABASE_NOMBRE = "conteo2.db";
    public  static  final String TABLE_CONTEO= "t_conteo2";

    //creacion del nombre y version
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    //armado de la base de datos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " +  TABLE_CONTEO  + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "nombre TEXT  NOT NULL," +
                "item TEXT  NOT NULL," +
                "Numero_Serie TEXT NOT NULL," +
                "ubicacion TEXT  NOT NULL," +
                "cantidad TEXT NOT NULL," +
                "resultado TEXT NOT NULL," +
                "UNIQUE(Numero_Serie))");
    }

    // se manda
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE  " + TABLE_CONTEO);
        onCreate(sqLiteDatabase);
    }
}
