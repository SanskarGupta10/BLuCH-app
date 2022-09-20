package com.example.bluch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Set;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    ListView lv;
    TextView tv14;
      Button button5,button6,button7,button8;
      EditText edit1;
      BluetoothAdapter bluetoothAdapter;
      ArrayAdapter<String> arrayAdapter;
      ArrayList<String> arrayList = new ArrayList<String>();
      BluetoothAdapter mbluetooth = BluetoothAdapter.getDefaultAdapter();
      public static final String DEVICE_NAME = "myDevice";
      public static final String DEVICE_ADDRESS = "Address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        tv14 = findViewById(R.id.tv14);
        Intent mintent = getIntent();
        String hisname = mintent.getStringExtra(MainActivity4.EXTRA_NAME);
        tv14.setText("Hello " + hisname + "!");
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
             startActivity(intent);

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"Connecting to the device !",Toast.LENGTH_SHORT).show();
                Object obj = lv.getAdapter().getItem(i);
                //i represents position
                String value= obj.toString();
                Intent nintent = new Intent(MainActivity.this,MainActivity3.class);
                nintent.putExtra(DEVICE_NAME,value);
                startActivity(nintent);
            }
        });


        noBluetooth();
    }


    public void visible(View view) {
        int requestCode = 1;
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, requestCode);


    }



    public void noBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         if(bluetoothAdapter == null){
             Toast.makeText(MainActivity.this,"No Available Bluetooth !",Toast.LENGTH_SHORT).show();

         }

    }
    public void enableBluetooth(View view){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(MainActivity.this,"Enabling the Bluetooth !",Toast.LENGTH_SHORT).show();

        }
    }
    public void listDevices(View view){
        if(bluetoothAdapter.isEnabled()){
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            ArrayList<String> devices = new ArrayList<>();
            for (BluetoothDevice bt : pairedDevices) {
                devices.add(bt.getName());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, devices);
            lv.setAdapter(arrayAdapter);
        }
           else{
               Toast.makeText(MainActivity.this,"Enable the Bluetooth first !",Toast.LENGTH_SHORT).show();
        }

    }

    public void disableBluetooth(View view){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
             if(bluetoothAdapter.isEnabled()) {
                 bluetoothAdapter.disable();
                 Toast.makeText(MainActivity.this, "Disabling the Bluetooth !", Toast.LENGTH_SHORT).show();

             }
    }

}
