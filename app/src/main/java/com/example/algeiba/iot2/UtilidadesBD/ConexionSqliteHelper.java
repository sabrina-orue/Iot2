package com.example.algeiba.iot2.UtilidadesBD;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Algeiba on 5/26/2018.
 */

public  class ConexionSqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyAplicacion";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase DataBase; //Instancia de la clase que maneja la base de datos

    public ConexionSqliteHelper(Context context) {              //Constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        try{
            db.execSQL(UtilidadUsuario.Create_Tabla_Usuarios);

            db.execSQL ("PRAGMA foreign_keys = ON"); //Esto permite  habilitar las foreign keys de la tabla
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int olvdVersion, int newVersion){ //Si existe una version vieja al instalar se borra

        db.execSQL("DROP TABLE IF EXISTS"+UtilidadUsuario.Tabla_Usuario);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        SQLiteDatabase DB= this.getWritableDatabase();
        return DB;
    }

    public SQLiteDatabase read(){          //Metodo para leer datos de la bd
        DataBase = this.getReadableDatabase();

        return DataBase;
    }

    public void close(){       //Metodo para cerrar la bd
        DataBase.close();
    }
}
