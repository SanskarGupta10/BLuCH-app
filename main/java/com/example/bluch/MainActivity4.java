package com.example.bluch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bluch.datas.Data;
import com.example.bluch.login.Contact;

import java.util.List;

public class MainActivity4 extends AppCompatActivity {
       Button bv1,bv2,bv3;
       EditText ed1,ed2;
       ImageView iv;
       TextView tv1,tv2;
    public static  final String EXTRA_NAME = "com.example.firstmul.extra.NAME";
    public static  final String EXTRA_PHONE_NO = "com.example.secondmul.extra.NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        bv1 = findViewById(R.id.bv1);
        bv2 = findViewById(R.id.bv2);
        bv3 = findViewById(R.id.bv3);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        tv1= findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        iv = findViewById(R.id.iv);
    }
    public void save(View view){

        Toast.makeText(MainActivity4.this,"Saving the User Info !",Toast.LENGTH_SHORT).show();
        Data db = new Data(MainActivity4.this);
        String aname = ed1.getText().toString();
        String aphoneno = ed2.getText().toString();
        if(!(aname.equals("") || aphoneno.equals(""))){
            Contact contact = new Contact();
            contact.setName(aname);
            contact.setPhoneNumber(aphoneno);
            db.addContact(contact);
        }
        else{
            Toast.makeText(MainActivity4.this,"Incomplete Info !",Toast.LENGTH_SHORT).show();
        }
    }

    public void search(View view){
        Data db = new Data(MainActivity4.this);
        List<Contact> contacts = db.getAllContacts();
        String aname = ed1.getText().toString();
        String aphoneno = ed2.getText().toString();
        if(aname.equals("") && aphoneno.equals("")){
            Toast.makeText(MainActivity4.this,"No User Found !",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity4.this,"Searching !",Toast.LENGTH_SHORT).show();
            for(Contact con : contacts){
                if(con.getName().equals(aname) && aphoneno.equals("")){

                    Intent intent = new Intent(this,MainActivity.class);
                    String pname = aname;
                    intent.putExtra(EXTRA_NAME,pname);
                    startActivity(intent);

                }
                if(con.getPhoneNumber().equals(aphoneno) && aname.equals("")){

                    Intent intent = new Intent(this,MainActivity.class);
                    String pname = con.getName();
                    intent.putExtra(EXTRA_NAME,pname);
                    startActivity(intent);

                }
                if(con.getName().equals(aname) && con.getPhoneNumber().equals(aphoneno)){

                    Intent intent = new Intent(this,MainActivity.class);
                    String pname = aname;
                    intent.putExtra(EXTRA_NAME,pname);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(MainActivity4.this,"User Not Present !",Toast.LENGTH_SHORT).show();
                }

            }

        }

    }
    public void delete(View view){
        Data db = new Data(MainActivity4.this);
        List<Contact> contacts = db.getAllContacts();
        String aname = ed1.getText().toString();
        String aphoneno = ed2.getText().toString();
        if(aname.equals("") || aphoneno.equals("")){
            Toast.makeText(MainActivity4.this,"Nothing to Delete !",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity4.this,"Deleting the User Account !",Toast.LENGTH_SHORT).show();
            for(Contact con : contacts){
                if(con.getPhoneNumber().equals(aphoneno)){
                    db.deleteCon(con);
                    break;
                }
            }


        }

    }
}