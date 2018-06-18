package com.example.algeiba.iot2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.algeiba.iot2.UtilidadesBD.ConexionSqliteHelper;
import com.example.algeiba.iot2.UtilidadesBD.UtilidadUsuario;

/**
 * Created by Algeiba on 5/26/2018.
 */
    public class MainRegistroActivity extends AppCompatActivity {

        public Button botonOkRegistro;
        public EditText nombreUsuario,emailUsuario,passwordUsuario;
        public UtilidadUsuario value= UtilidadUsuario.getInstancia();
        public int intentos = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_regstro);

        }

        public void onClick(View v)
        {
            nombreUsuario = (EditText) findViewById(R.id.nameUser);
            emailUsuario = (EditText) findViewById(R.id.emailUser);
            passwordUsuario = (EditText) findViewById(R.id.passUser);

            if ((nombreUsuario.length() == 0) || (emailUsuario.length() == 0) || (passwordUsuario.length() == 0)) {
                Toast.makeText(getApplicationContext(),"Faltan Completar Campos", Toast.LENGTH_SHORT).show();
            }
            else if(passwordUsuario.length() < 8 ){
                Toast.makeText(getApplicationContext(),"Contraseña demasiado corta", Toast.LENGTH_SHORT).show();
            }
            else {
                Boolean correcto= Validacion(emailUsuario.getText().toString());
                if (correcto){
                    registrarUsuarios();
                }
                else{
                    Toast.makeText(getApplicationContext(),"No se ha podido registrar",Toast.LENGTH_SHORT).show();

                }
                //registrarUsuariosPorSentencia();
            }
        }
    //TODO: ver con Sabry
        private Boolean Validacion(String emailUser) {
            if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                // String mensaje= String.valueOf(R.string.inalid);
                emailUsuario.setError("Datos Icorrectos");
                return false;
            }
            else{
                String consulta = value.consultaUsuario(emailUser);
                ConexionSqliteHelper conexion=new ConexionSqliteHelper(this);
                SQLiteDatabase DB= conexion.getWritableDatabase();
                DB.execSQL(UtilidadUsuario.Create_Tabla_Usuarios);
                Cursor cursor = DB.rawQuery(consulta,null);
                if (cursor.moveToFirst()){ //devuelve true si existe el registro
                    //si existe muestro una noti en el input
                    String mensaje = String.valueOf("El usuario ya existe");
                    emailUsuario.setError(mensaje);
                    return false;
                }
                else{
                    return true;
                }
            }

        }

        public void registrarUsuarios() {
            ConexionSqliteHelper conexion=new ConexionSqliteHelper(this);
            try {
                ContentValues user = value.valueUsuario(emailUsuario.getText().toString(), nombreUsuario.getText().toString(), passwordUsuario.getText().toString());
                SQLiteDatabase DB= conexion.getWritableDatabase();

                //inserta lo que haya en @ContentValues.usuario
                String insertUsuario= value.insertUsuario();
                DB.execSQL(insertUsuario);
                DB.close();
                SharedPreferences preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("name",value.usuario.get(value.Campo_Email).toString());
                editor.putString("pass",value.usuario.get(value.Campo_Nombre).toString());
                editor.putString("email",value.usuario.get(value.Campo_Password).toString());
                editor.putBoolean("IsLogin",true);
                editor.commit();

                Toast.makeText(getApplicationContext(),"Se ha regitrado correctamente",Toast.LENGTH_SHORT).show();
                Intent buton = new Intent(MainRegistroActivity.this, MainControles.class);
                startActivity(buton);

                //  Intent intent= new Intent(Main2Activity.this, MomentoListActivity.class);     //asocio la instancia de redireccion de la activity actual a la de register
                // startActivity(intent);
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"No se pudo registrar",Toast.LENGTH_LONG).show();
            }
        }

    }

