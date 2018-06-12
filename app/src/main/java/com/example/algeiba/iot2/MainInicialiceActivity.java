package com.example.algeiba.iot2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;

/**
 * Created by Algeiba on 5/26/2018.
 */

public class MainInicialiceActivity extends AppCompatActivity {
    TextView temperatura;
    Switch estadoSwitch;
    EditText minParam;
    EditText maxParam;

    public final String API = "http://192.168.0.32:8080/apiInformacion";

    private static boolean cambioParametro;
    private static boolean isModalVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        cambioParametro = false;
        isModalVisible = false;

        estadoSwitch = (Switch) findViewById(R.id.Switch);
        estadoSwitch.setClickable(false);

        minParam = (EditText)findViewById(R.id.tempMinima);
        maxParam = (EditText)findViewById(R.id.tempMaxima);

        temperatura = (TextView) findViewById(R.id.idTemperatura);
        GetTemperaturaThread time = new GetTemperaturaThread();
        time.execute();
        }

    public class GetTemperaturaThread extends AsyncTask<Void, Integer,String> {
        @Override
        protected String doInBackground(Void... params){
            String temp = "sin datos";
            try {
                JSONObject resp =RequestRESTServicio.requestURL(API+"/ultimaMedicionTemp",
                        "GET",null);
                temp = resp.getString("magnitud");
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch(JSONException je){
                je.printStackTrace();
            }
            return temp;
        }

        @Override
        protected void onPostExecute(String tempActual) {
            if(!isModalVisible) {
                TextView tempActualTextView = (TextView) findViewById(R.id.idTemperatura);
                tempActualTextView.setText(tempActual);
                try {
                    Float temp = Float.parseFloat(tempActual);
                    String minInput = ((EditText) findViewById(R.id.tempMinima)).getText().toString();
                    String maxInput = ((EditText) findViewById(R.id.tempMaxima)).getText().toString();

                    //pongo en número irrealista las temperaturas cuando no ingresa nada para que no lanze el NumberFormatException
                    Float tempMinima = !"".equals(minInput) ? Float.parseFloat(minInput) : -9999;
                    Float tempMaxima = !"".equals(maxInput) ? Float.parseFloat(maxInput) : 9999;
                    Boolean estaPrendidoCalefactor = ((Switch) findViewById(R.id.Switch)).isChecked();

                    if (tempMinima > tempMaxima) {
                        throw new InvalidParameterException("Los parámetros deben ser coherentes");
                    }
                    if (!estaPrendidoCalefactor && temp < tempMinima) {
                        ControlNotificacion(
                                "Desea prender la estufa?",
                                "/encenderCalentador",
                                "Hace menos de " + tempMinima + " ºC"
                        );
                    }
                    if (estaPrendidoCalefactor && temp > tempMaxima) {
                        ControlNotificacion(
                                "Desea apagar la estufa?",
                                "/apagarCalentador",
                                "Hace más de " + tempMaxima + " ºC"
                        );
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Los parámetros deben ser numéricos", Toast.LENGTH_LONG).show();
                } catch (InvalidParameterException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            GetTemperaturaThread nuevoThread = new GetTemperaturaThread();
            nuevoThread.execute();
        }
    }

    public void ControlNotificacion (String mensaje, String url, String tituloModal) throws NumberFormatException {
        Float tempSelec = Float.valueOf(
                temperatura.getText().toString()
        );
        AlertDialog.Builder alert = new AlertDialog.Builder(MainInicialiceActivity.this);
        alert.setCancelable(false);

        final String urlClick = url;

        alert.setMessage(mensaje);

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject respuesta = RequestRESTServicio.requestURL(API + urlClick,"GET",null);
                try{
                    estadoSwitch.setChecked(respuesta.getBoolean("estadoCalentador"));
                }catch(JSONException e){
                    e.printStackTrace();
                }finally {
                    isModalVisible = false;
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isModalVisible = false;
            }
        });
        AlertDialog titulo = alert.create();
        titulo.setTitle(tituloModal);
        titulo.show();
        isModalVisible = true;

    }
}
