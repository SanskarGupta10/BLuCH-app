package com.example.bluch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity3 extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    SendReceive sendReceive;
   Button button2,button9,button10;
   ListView rv;
   EditText edit1;
   BluetoothDevice bdevice;
   BluetoothAdapter bluetoothAdapter;
   ArrayAdapter<String> mainAdapter;
   ArrayList<String> textArray = new ArrayList<String>();

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    public static final UUID MY_UUID = UUID.fromString("d56c6170-385b-11ed-a261-0242ac120002");
    public static final String APP_NAME = "BLuCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        button2 = findViewById(R.id.button2);
        button9 = findViewById(R.id.button9);
        button10 = findViewById(R.id.button10);
        rv = findViewById(R.id.rv);
        edit1= findViewById(R.id.edit1);
        Bundle recdData = getIntent().getExtras();
        String myVal = recdData.getString(MainActivity.DEVICE_NAME);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mainAdapter = new ArrayAdapter<String>(MainActivity3.this, android.R.layout.simple_list_item_1,textArray);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerClass serverClass=new ServerClass();
                serverClass.start();
            }
        });
      button10.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
              for (BluetoothDevice bt : pairedDevices) {
                  if (bt.getName().equals(myVal)) {
                      ClientClass clientClass = new ClientClass(bt);
                      clientClass.start();

                      Toast.makeText(MainActivity3.this, "Connecting !", Toast.LENGTH_SHORT).show();
                      break;
                  }

              }

          }
      });
      button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

                  String text = edit1.getText().toString();
                  String string = String.valueOf(edit1.getText());
                  sendReceive.write(string.getBytes());
                  textArray.add("Me :- " + text);
                  rv.setAdapter(mainAdapter);



          }
      });

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what)
            {
                case STATE_LISTENING:
                    Toast.makeText(MainActivity3.this,"Listening !",Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTING:
                    Toast.makeText(MainActivity3.this,"Connecting !",Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(MainActivity3.this,"Connected !",Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(MainActivity3.this,"Sorry Connection Failed !",Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) message.obj;
                    String tempMsg=new String(readBuff,0,message.arg1);
                    textArray.add(tempMsg);
                    rv.setAdapter(mainAdapter);
                    break;
            }

            return true;
        }
    });

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket=null;

            while (socket==null)
            {
                try {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass (BluetoothDevice device1)
        {
            device=device1;

            try {
                socket=device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try {
                socket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}