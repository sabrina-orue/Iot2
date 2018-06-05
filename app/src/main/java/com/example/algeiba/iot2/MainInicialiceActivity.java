package com.example.algeiba.iot2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Algeiba on 5/26/2018.
 */

public class MainInicialiceActivity extends AppCompatActivity {
    TextView temperatura;
    Switch estadoSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        estadoSwitch = findViewById(R.id.Switch);
        estadoSwitch.setClickable(false);

       temperatura = (TextView) findViewById(R.id.idTemperatura);
        time time = new time();
        time.execute();
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

    public void getData(){
        String sql= "https://jsonplaceholder.typicode.com/posts";
        StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new  StringBuffer();

            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();
            JSONArray jsonArray = null;

            jsonArray = new JSONArray(json);

            for (int i = 0; i< jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Toast.makeText(MainActivity.this,jsonObject.optString("id"),Toast.LENGTH_SHORT).show();
                temperatura.setText(jsonObject.optString("id"));
            }
            ControlNotificacion();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            //   Toast.makeText(MainActivity.this,"cada 5 segundos",Toast.LENGTH_SHORT).show();
            getData();
        }
    }

    public void ControlNotificacion() {
        if (temperatura.getText().toString().equals("100")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainInicialiceActivity.this);
            alert.setMessage("Desea prender la estufa?");
            alert.setCancelable(false);
            alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    encenderCalentar();
                }
            });
            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    apagarCalentar();
                }
            });

            AlertDialog TITULO = alert.create();
            TITULO.setTitle("Hace menos de 10ºC");
            TITULO.show();
        }

    }
    public void encenderCalentar(){
        estadoSwitch.setChecked(true);
    }

    public void apagarCalentar(){
        estadoSwitch.setChecked(false);
    }
}
