package com.example.bluch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    ListView lv2;
    Button button;
    BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> mdevices = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
         lv2 = findViewById(R.id.lv2);
         button = findViewById(R.id.button);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (myAdapter.isEnabled()) {
                     myAdapter.startDiscovery();
                 }
             }
         });
         IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mreceiver,filter);

         arrayAdapter = new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_list_item_1,mdevices);
         lv2.setAdapter(arrayAdapter);

    }
    BroadcastReceiver mreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mdevices.add(device.getName() + "\n" + device.getAddress());
                arrayAdapter.notifyDataSetChanged();
            }
        }
        };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mreceiver);
    }

}
