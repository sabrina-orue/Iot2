package com.example.algeiba.iot2;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class RequestRESTServicio {
    public static JSONObject requestURL(String pathUrl,String method, HashMap params){
        //TODO: agregar params
        StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(pathUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(method);

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

            //jsonArray = new JSONArray();

            //for (int i = 0; i< jsonArray.length();i++){
            JSONObject jsonObject = new JSONObject(json);
            // Toast.makeText(MainActivity.this,jsonObject.optString("id"),Toast.LENGTH_SHORT).show();
            //}
            return jsonObject;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    };
}
