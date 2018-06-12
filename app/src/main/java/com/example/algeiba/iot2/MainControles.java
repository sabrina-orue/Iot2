package com.example.algeiba.iot2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Algeiba on 6/3/2018.
 */

public class MainControles extends AppCompatActivity {

    private Button controlTemperatura, controlHumedad, ControlLuz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controles);
        controlTemperatura = (Button)findViewById(R.id.ButtonTemperatura);
        controlTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainControles.this, MainInicialiceActivity.class);
                startActivity(intent);
            }
        });
    }


}
