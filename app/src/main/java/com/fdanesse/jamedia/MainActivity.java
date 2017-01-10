package com.fdanesse.jamedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fdanesse.jamedia.Archivos.FileChooserActivity;
import com.fdanesse.jamedia.Archivos.FileManager;
import com.fdanesse.jamedia.JamediaPlayer.PlayerActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageButton cancel;
    private Button youtube;
    private Button radio;
    private Button television;
    private Button archivos;

    private NetworkChangeReceiver network_receiver;
    private WifiManager.WifiLock wifiLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancel = (ImageButton) findViewById(R.id.cancel);
        youtube = (Button) findViewById(R.id.youtube);
        radio = (Button) findViewById(R.id.radio);
        television = (Button) findViewById(R.id.television);
        archivos = (Button) findViewById(R.id.archivos);

        set_touch_listeners();
        Utils.setActiveView2(archivos);
        network_changed();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        network_receiver = new NetworkChangeReceiver();
        registerReceiver(network_receiver, filter);

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
    }

    private void set_touch_listeners(){

        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    finishAffinity();
                }
                return true;
            }
        });

        radio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ArrayList<ListItem> radios = FileManager.get_radios();
                    Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                    intent.putExtra("tracks", radios);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        archivos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void button_clicked(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                Utils.setInactiveView2(view);
                break;
            }
            case MotionEvent.ACTION_UP:{
                Utils.setActiveView2(view);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(network_receiver);
        wifiLock.release(); //FIXME: Revisar esto
    }

    private boolean network_check(){
        /**
         * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html?hl=es
         */
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

    private void network_changed(){
        boolean i = network_check();
        radio.setEnabled(i);
        //television.setEnabled(i);

        if (i == false){
            Utils.setInactiveView2(radio);
            //Utils.setInactiveView2(television);
            Snackbar.make(radio, "No tienes conexión a internet", Snackbar.LENGTH_LONG).show();
        }
        else{
            Utils.setActiveView2(radio);
            //Utils.setActiveView2(television);
            Snackbar.make(radio, "Conectando a internet...", Snackbar.LENGTH_LONG).show();
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        /**
         * http://www.mysamplecode.com/2013/04/android-automatically-detect-internet-connection.html
         */

        @Override
        public void onReceive(final Context context, final Intent intent) {
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {
            network_changed();
            return true;
        }

    } //NetworkChangeReceiver
}     //MainActivity