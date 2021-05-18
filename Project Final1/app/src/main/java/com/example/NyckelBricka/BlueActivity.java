package com.example.NyckelBricka;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BlueActivity extends MainPageActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "BlueActivity";

    Button btn_bluetoothEnable, btn_BTenableDiscovery, btn_findUnpairedDevice;

    BluetoothAdapter mBlueToothAdapter;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lv_newDevices;

    public int connected = 0;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBlueToothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBlueToothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(getApplicationContext(),"STATE ON", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(getApplicationContext(),"STATE TURNING ON", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(getApplicationContext(),"STATE OFF", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(getApplicationContext(),"STATE TURNING OFF", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    };



    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Toast.makeText(getApplicationContext(),"Discover Enabled", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Toast.makeText(getApplicationContext(),"Discover Turned Off, Paired Devices OK", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Toast.makeText(getApplicationContext(),"Discover Turned Off", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Toast.makeText(getApplicationContext(),"Connection...", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    };



    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // newDevices.setAdapter(null);
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                //Toast.makeText(getApplicationContext(),"name: " + device.getName() + "adress: " + device.getAddress(), Toast.LENGTH_LONG).show();

                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lv_newDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(getApplicationContext(), "Device is Bonded", Toast.LENGTH_LONG).show();
                    connected = 1;
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Toast.makeText(getApplicationContext(),"Device is Bonding", Toast.LENGTH_LONG).show();
                }
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Toast.makeText(getApplicationContext(),"No Bonds", Toast.LENGTH_LONG).show();
                    connected = 0;
                }
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        btn_bluetoothEnable = findViewById(R.id.btn_bluetoothEnable);
        btn_BTenableDiscovery = findViewById(R.id.btn_BTenableDiscovery);
        btn_findUnpairedDevice = findViewById(R.id.btn_findUnpairedDevice);

        lv_newDevices = findViewById(R.id.lv_newDevices);

        lv_newDevices.setOnItemClickListener(this);

        mBTDevices = new ArrayList<>();

        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);


        btn_bluetoothEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }
        });

        btn_BTenableDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableDisc();
            }
        });

        btn_findUnpairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverDevice();
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

    public void enableDisableDisc(){
        Toast.makeText(getApplicationContext(), "making device discoverable for 100sec...", Toast.LENGTH_LONG).show();
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100);

        IntentFilter intentFilter = new IntentFilter(mBlueToothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }

    public void DiscoverDevice(){
        if(mBlueToothAdapter.isDiscovering()){
            mBlueToothAdapter.cancelDiscovery();

            checkBTPermissons();

            mBlueToothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBlueToothAdapter.isDiscovering()){

            checkBTPermissons();

            mBlueToothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }

    }

    public void checkBTPermissons(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            int permissonCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissonCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissonCheck != 0){

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);

            }else{
                Toast.makeText(getApplicationContext(), "No need to check Permisson", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBlueToothAdapter.cancelDiscovery();

        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();

        // Toast.makeText(getApplicationContext(), "name: " + deviceName + "address: " + deviceAddress, Toast.LENGTH_LONG).show();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Toast.makeText(getApplicationContext(), "Trying to pair..." , Toast.LENGTH_LONG).show();
            mBTDevices.get(position).createBond();
        }
    }
}