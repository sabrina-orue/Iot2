package com.example.algeiba.iot2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.widget.TextView;
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
    public Contador counter;
    //public TextView texto;
    private Locale locale;
    private Configuration config = new Configuration();
    public Button botonRegistro,botonSesion,idioma;
    public TextView prueba, reintente, reintente2;
    public int x=60;
    public int contador=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter = new Contador(61000,1000);
        //--------------si se oprime el registrar-----------------
        prueba =(TextView) findViewById(R.id.textView12);
        reintente =(TextView) findViewById(R.id.textReintente);
        reintente2 =(TextView) findViewById(R.id.textReintente2);
        prueba.setVisibility(View.INVISIBLE);
        reintente2.setVisibility(View.INVISIBLE);
        reintente.setVisibility(View.INVISIBLE);
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
                passwordUsuario = (TextView) findViewById(R.id.passwordUser);
                String Pass = null;
                try {
                    Pass = SeguridadUtil.encrypt(passwordUsuario.getText().toString(),SeguridadUtil.PALABRACLAVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String name = null;
                try {
                    emailUsuario = (TextView) findViewById(R.id.nameUser);
                    if(!Patterns.EMAIL_ADDRESS.matcher(emailUsuario.getText().toString()).matches()) {
                        // String mensaje= String.valueOf(R.string.inalid);
                        emailUsuario.setError("Email no valido");
                    }
                    name = SeguridadUtil.encrypt(emailUsuario.getText().toString(),Pass);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Validaciones(name, Pass);
            }
        });

    }


    private void Validaciones( String email,String pass) {

       // if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // String mensaje= String.valueOf(R.string.inalid);
          //  emailUsuario.setError("Email no valido");
      //  }
        UtilidadUsuario value = UtilidadUsuario.getInstancia();
        String consulta = value.consultaUsuarioCompleto(email);
        ConexionSqliteHelper conexion=new ConexionSqliteHelper(this);
        SQLiteDatabase DB = conexion.getWritableDatabase();
        DB.execSQL(UtilidadUsuario.Create_Tabla_Usuarios);
        Cursor cursor = DB.rawQuery(consulta,null);

        //valido si existe el usuario y si coincide la contraseña/
        if(cursor.moveToFirst()){
            try {
                String passwdEncrip = cursor.getString(cursor.getColumnIndex("password"));
                String nombreEncrip = cursor.getString(cursor.getColumnIndex("email"));

                //la password fue encriptada con palabra clave
              //  String password = SeguridadUtil.encrypt(passwdEncrip, SeguridadUtil.PALABRACLAVE);

                //todos los campos fueron encriptados con la password sin encriptar
                String name = SeguridadUtil.encrypt(nombreEncrip, passwdEncrip);
                if (pass.equals(passwdEncrip)){
                    SharedPreferences preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencias.edit();
                    //Refactor para que guarde encriptado
                    //FIXME: ver como mejorar el código haciendo que se guarde estáticamente el usuario sin llamar a valueUsuario
                    value.valueUsuario(name,email,pass);
                    editor.putString("name",value.usuario.get(value.Campo_Email).toString());
                    editor.putString("pass",value.usuario.get(value.Campo_Nombre).toString());
                    editor.putString("email",value.usuario.get(value.Campo_Password).toString());
                    editor.putBoolean("IsLogin",true);
                    editor.commit();//confirma los datos almacenados

                    //String N = preferencias.getString("name", null);
                    //String P= preferencias.getString("email",null);
                    //String P= preferencias.getString("pass",null);
                    Toast.makeText(getApplicationContext(), "Ingreso exitoso",Toast.LENGTH_SHORT).show();
                    Intent buton = new Intent(MainActivity.this, MainControles.class);
                    startActivity(buton);
                }
                else {
                    contador = contador+1;
                    if (contador < 4){
                        emailUsuario.setFocusable(false);
                        emailUsuario.setEnabled(false);
                        emailUsuario.setCursorVisible(false);

                        passwordUsuario.setFocusable(false);
                        passwordUsuario.setEnabled(false);
                        passwordUsuario.setCursorVisible(false);



                        botonSesion.setEnabled(false);
                        botonSesion.setFocusable(false);
                        reintente2.setVisibility(View.VISIBLE);
                        reintente.setVisibility(View.VISIBLE);
                        prueba.setVisibility(View.VISIBLE);
                        counter.start();
                }
                    else{
                           Toast.makeText(getApplicationContext(), "Datos incorrectos, intente nuevamente", Toast.LENGTH_SHORT).show();

                    }
                    //    Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                    //   x = x + 1;
                    //    DB.execSQL(UtilidadUsuario.IncrementarIntentos(email, x));
                    //    if (x == 3) {
                    //        ejecutar();
                    //       DB.execSQL(UtilidadUsuario.IncrementarIntentos(email, 0));
                    //       DB.close();
                    //   }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        else{
            contador = contador+1;
            if (contador == 3){
                //emailUsuario.setFocusable(false);
                emailUsuario.setEnabled(false);
                emailUsuario.setCursorVisible(false);

               //passwordUsuario.setFocusable(false);
                passwordUsuario.setEnabled(false);
                passwordUsuario.setCursorVisible(false);

                botonSesion.setEnabled(false);
                botonSesion.setFocusable(false);

                reintente2.setVisibility(View.VISIBLE);
                reintente.setVisibility(View.VISIBLE);
                prueba.setVisibility(View.VISIBLE);
                counter.start();
            }
            else{
                Toast.makeText(getApplicationContext(),"Datos Incorrectos",Toast.LENGTH_SHORT).show();

            }

        }
       // DB.close();
    }
    public class time extends AsyncTask<Void, Integer,Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
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
        /** Called when the activity is first created. */

        public class Contador extends CountDownTimer{

            public Contador(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }

            @Override
            public void onFinish() {
                prueba.setVisibility(View.INVISIBLE);
                reintente2.setVisibility(View.INVISIBLE);
                reintente.setVisibility(View.INVISIBLE);
             //   prueba.setText("FIN");
               // emailUsuario.setFocusable(true);
                emailUsuario.setEnabled(true);
                emailUsuario.setCursorVisible(true);

               // passwordUsuario.setFocusable(true);
                passwordUsuario.setEnabled(true);
                passwordUsuario.setCursorVisible(true);

                botonSesion.setEnabled(true);
                passwordUsuario.setFocusable(true);

                x=60;
                contador = 0;
            }

            @Override
            public void onTick(long millisUntilFinished) {
                prueba.setText(String.valueOf(x));
                x=x-1;
            }

        }

}