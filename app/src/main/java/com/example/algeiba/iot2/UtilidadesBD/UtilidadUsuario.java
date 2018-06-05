package com.example.algeiba.iot2.UtilidadesBD;

import android.content.ContentValues;

/**
 * Created by Algeiba on 5/26/2018.
 */


public class UtilidadUsuario {

    public static final String Campo_Id="Id";
    public static final String Tabla_Usuario="Usuarios";
    public static final String Campo_Email="email";
    public static final String Campo_Nombre="nombre";
    public static final String Campo_Password="password";
    private static final String Campo_Intentos = "intentos";
    public static final String Create_Tabla_Usuarios="Create table if not exists "+Tabla_Usuario+
            " ("+Campo_Id+" integer primary key autoincrement not null,"
            +Campo_Email+" varchar not null,"+
            Campo_Nombre+"  varchar not null,"+
            Campo_Password+"  varchar not null," +
            Campo_Intentos+" integer not null)";
    public UtilidadUsuario(){

    }
    public ContentValues valueUsuario(String email, String nombre, String password){
        ContentValues usuario= new ContentValues();
        usuario.put(Campo_Email,email);
        usuario.put(Campo_Nombre,nombre);
        usuario.put(Campo_Password,password);

        return usuario;
    }
public String insertUsuario(String nombre, String email, String password){
    String insert = "INSERT INTO "+Tabla_Usuario+"("+Campo_Email+","+Campo_Nombre+","+Campo_Password+","+Campo_Intentos+") VALUES ('"+email+"','"+nombre+"','"+password+"',0)";
return insert;
}

    public String consultaUsuario(String user){
        String consultaEmail= "select "+ Campo_Email +" from "+Tabla_Usuario+" where "+Campo_Email+" == '"+user+"';";
        return consultaEmail;
    }

    public String consultaUsuarioCompleto(String user){
        String consultaEmail= "select "+Campo_Email+", "+Campo_Password+", "+Campo_Nombre +", "+Campo_Intentos+" from "+Tabla_Usuario+" where "+Campo_Email+" == '"+user+"';";
        return consultaEmail;
    }

    public String consultaNombre(String user){
        String consultaEmail= "select "+Campo_Nombre +" from "+Tabla_Usuario+" where "+Campo_Email+" == '"+user+"';";
        return consultaEmail;
    }

    public static String consultaIntentos(String user){
        String consulta= "select "+Campo_Intentos +" from "+Tabla_Usuario+" where "+Campo_Email+" == '"+user+"';";
        return consulta;
    }

    public static String IncrementarIntentos(String user, int valor){
        String consulta= "update "+Tabla_Usuario+" set "+Campo_Intentos +"="+ valor+" where "+Campo_Email+" == '"+user+"';";
        return consulta;
    }

}


