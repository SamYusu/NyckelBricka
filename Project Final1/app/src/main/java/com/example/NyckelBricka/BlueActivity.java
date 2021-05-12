package com.example.NyckelBricka;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BlueActivity extends AppCompatActivity {

    Button btn_bluetoothEnable;

    BluetoothAdapter mBlueToothAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBlueToothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBlueToothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(getApplicationContext(),"STATE ON", Toast.LENGTH_LONG).show();
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(getApplicationContext(),"STATE TURNING ON", Toast.LENGTH_LONG).show();
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(getApplicationContext(),"STATE OFF", Toast.LENGTH_LONG).show();
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(getApplicationContext(),"STATE TURNING OFF", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mBroadcastReceiver1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        btn_bluetoothEnable = findViewById(R.id.btn_bluetoothEnable);

        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        btn_bluetoothEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }
        });


    }

    public void enableDisableBT(){
        if(mBlueToothAdapter == null){
            Toast.makeText(getApplicationContext(), "Din enhet kan inte användas Bluetooth", Toast.LENGTH_LONG).show();
        }else{
            if(!mBlueToothAdapter.isEnabled()){
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBTIntent);

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBroadcastReceiver1, BTIntent);
            }
            if(mBlueToothAdapter.isEnabled()){
                mBlueToothAdapter.disable();

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBroadcastReceiver1, BTIntent);
            }
        }

    }
}