package com.example.algeiba.iot2.UtilidadesBD;

import android.content.ContentValues;

import com.example.algeiba.iot2.SeguridadUtil;

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

    public static ContentValues usuario;

    private static UtilidadUsuario instancia;


    public static synchronized UtilidadUsuario getInstancia(){
        if(instancia == null){
            instancia = new UtilidadUsuario();
        }
        return instancia;
    }

    private UtilidadUsuario(){
    }
    public ContentValues valueUsuario(String email, String nombre, String password)
    throws  Exception{
        usuario = new ContentValues();

        String passwdEncp = SeguridadUtil.encrypt(password,SeguridadUtil.PALABRACLAVE);
        String emailEncp = SeguridadUtil.encrypt(email,passwdEncp);
        String nombreEncp = SeguridadUtil.encrypt(nombre,passwdEncp);
        usuario.put(Campo_Email,emailEncp);
        usuario.put(Campo_Nombre,nombreEncp);
        usuario.put(Campo_Password,passwdEncp);

        return usuario;
    }
public String insertUsuario(){
    String email = usuario.get(Campo_Email).toString();
    String nombre = usuario.get(Campo_Nombre).toString();
    String password = usuario.get(Campo_Password).toString();
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


