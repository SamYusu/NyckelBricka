package com.example.nyckelbricka;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SecondActivity extends AppCompatActivity {

    private Button getLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getLoc = (Button) findViewById(R.id.btnLocation);
        ActivityCompat.requestPermissions(SecondActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        getLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps g = new gps(getApplicationContext());
                Location l = g.getLocation();
                if (l != null){
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    Toast.makeText(getApplicationContext(),"Lat: "+lat+"\n lon: "+lon,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}