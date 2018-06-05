package com.example.algeiba.iot2.UtilidadesBD;

/**
 * Created by Algeiba on 5/26/2018.
 */


public class Usuario {


    private int id;
    private String usuario;
    private String email;
    private String password;
    private int intentos;

    public Usuario(String usuario, String email,String password){

        this.email= email;
        this.usuario=usuario;
        this.password=password;
        this.intentos = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(String usuario) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIntentos(){
        return intentos;
    }
    public void  setIntentos(int i){
        this.intentos = i;
    }

    public int incrementarIntentos(){
        int intent = this.intentos + 1;
        this.intentos = intent;
        return this.intentos;
    }
}

