package com.example.algeiba.iot2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.algeiba.iot2.UtilidadesBD.ConexionSqliteHelper;
import com.example.algeiba.iot2.UtilidadesBD.Usuario;
import com.example.algeiba.iot2.UtilidadesBD.UtilidadUsuario;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public TextView emailUsuario,passwordUsuario;

    private Locale locale;
    private Configuration config = new Configuration();
    public Button botonRegistro,botonSesion,idioma;
    public TextView prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--------------si se oprime el registrar-----------------
        prueba = (TextView) findViewById(R.id.textView12);

        SharedPreferences preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        //Si los datos tomados del sharedPreferences no estan vacios entonces autentico
        SharedPreferences.Editor editor = preferencias.edit();
       // String userLogueado = null;
        // if (preferencias.getBoolean("IsLogin", false)) {
        // //   userLogueado = preferencias.getString("email", null);
        // //   if (userLogueado.equals(null)) {

        //      }
        //      else {
        //          ConexionSqliteHelper conexion = new ConexionSqliteHelper(this);
        //          SQLiteDatabase DB = conexion.getWritableDatabase();
        //          UtilidadUsuario utilidad = new UtilidadUsuario();
        //         String consulta = utilidad.consultaIntentos(userLogueado);
        //         Cursor cursor = DB.rawQuery(consulta, null);
        //        if(cursor.moveToFirst()){
        //           int intentos = cursor.getInt(cursor.getColumnIndex("intentos"));
        //           if (intentos < 3){
        //              Intent intent = new Intent(this, MainControles.class);     //asocio la instancia de redireccion de la activity actual a la de register
        //              startActivity(intent); //Realizo la redireccion
        //              finish();
        //           }
        //           else{
                        //alerta, mail, codigo
        //           }
        //        }
        //       }

        // }
        botonRegistro = (Button) findViewById(R.id.btnRegistrate);
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainRegistroActivity.class);
                startActivity(intent);
            }
        });

        //--------------si se oprime el iniciar sesion
        botonSesion = (Button) findViewById(R.id.btnIniciarSesion);
        botonSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailUsuario = (TextView) findViewById(R.id.nameUser);
                passwordUsuario = (TextView) findViewById(R.id.passwordUser);
                Validaciones(emailUsuario.getText().toString(), passwordUsuario.getText().toString());
            }
        });

    }


    private void Validaciones( String email,String pass) {
        UtilidadUsuario value = new UtilidadUsuario();
        String consulta = value.consultaUsuarioCompleto(email);
        ConexionSqliteHelper conexion=new ConexionSqliteHelper(this);
        SQLiteDatabase DB = conexion.getWritableDatabase();
        DB.execSQL(UtilidadUsuario.Create_Tabla_Usuarios);
        Cursor cursor = DB.rawQuery(consulta,null);

        //valido si existe el usuario y si coincide la contraseña/
        if(cursor.moveToFirst()){
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String name = cursor.getString(cursor.getColumnIndex("nombre"));
            int x = cursor.getInt(cursor.getColumnIndex("intentos"));
            Usuario usuario = new Usuario(name,email,password);
            if (pass.equals(password)){
                SharedPreferences preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("name",usuario.getUsuario());
                editor.putString("pass",usuario.getPassword());
                editor.putString("email",usuario.getEmail());
                editor.putBoolean("IsLogin",true);
                editor.commit();//confirma los datos almacenados

                //String N = preferencias.getString("name", null);
                //String P= preferencias.getString("email",null);
                //String P= preferencias.getString("pass",null);
                Toast.makeText(getApplicationContext(), "Ingreso exitoso",Toast.LENGTH_SHORT).show();
                Intent buton = new Intent(MainActivity.this, MainControles.class);
                startActivity(buton);
            }
           // else {
            //    Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
             //   x = x + 1;
            //    DB.execSQL(UtilidadUsuario.IncrementarIntentos(email, x));
            //    if (x == 3) {
            //        ejecutar();
             //       DB.execSQL(UtilidadUsuario.IncrementarIntentos(email, 0));
             //       DB.close();
             //   }
          //  }
        }
        else{
            Toast.makeText(getApplicationContext(),"Datos Incorrectos",Toast.LENGTH_SHORT).show();

        }
        DB.close();
    }
    public class time extends AsyncTask<Void, Integer,Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            //  hilo();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ejecutar();
        }
    }
    public void ejecutar(){
        time time = new time();
        time.execute();
    }


    public void hilo(){
        try{
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}