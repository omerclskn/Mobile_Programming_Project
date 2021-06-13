package com.example.dolap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button cekmece;
    Button kabin;
    Button etkinlik;
    Button kombinpaylas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineVariables();
        defineListeners();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.cekmece:
                intent = new Intent(v.getContext(), DrawerActivity.class);
                startActivity(intent);
                break;

            case R.id.kabin:
                intent = new Intent(v.getContext(), KabinActivity.class);
                startActivity(intent);
                break;

            case R.id.etkinlik:
                intent = new Intent(v.getContext(), EtkinlikActivity.class);
                startActivity(intent);
                break;

            case R.id.kombinpaylas:
                intent = new Intent(v.getContext(), ShareCombine.class);
                startActivity(intent);
                break;

            default:
                break;

        }
    }

    public void defineVariables(){
        cekmece = (Button) findViewById(R.id.cekmece);
        kabin = (Button) findViewById(R.id.kabin);
        etkinlik = (Button) findViewById(R.id.etkinlik);
        kombinpaylas = (Button) findViewById(R.id.kombinpaylas);
    }
    public void defineListeners(){
        cekmece.setOnClickListener(this);
        kabin.setOnClickListener(this);
        etkinlik.setOnClickListener(this);
        kombinpaylas.setOnClickListener(this);
    }

    BatteryOnCharge batteryOnCharge;

    @Override
    protected void onResume() {
        super.onResume();

        batteryOnCharge =  new BatteryOnCharge();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryOnCharge, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(batteryOnCharge);
    }
}