package com.fdanesse.jamedia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fdanesse.jamedia.PlayerList.ListActivity;

public class MainActivity extends AppCompatActivity {

    private Button radio;
    private Button television;
    private Button archivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radio = (Button) findViewById(R.id.radio);
        television = (Button) findViewById(R.id.television);
        archivos = (Button) findViewById(R.id.archivos);

        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        archivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        boolean i = network_check();
        findViewById(R.id.radio).setEnabled(i);
        findViewById(R.id.television).setEnabled(i);
        if (!i){
            Snackbar.make(radio, "No tienes conexión a internet", Snackbar.LENGTH_INDEFINITE);
        }
    }

    private boolean network_check(){
        // https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html?hl=es
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }
}