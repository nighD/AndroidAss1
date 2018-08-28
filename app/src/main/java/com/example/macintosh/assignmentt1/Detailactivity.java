package com.example.macintosh.assignmentt1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Detailactivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        TextView textView = findViewById( R.id.image_description);
        ImageView imageView = findViewById(R.id.image);
        Intent i = getIntent();
        final String name=i.getExtras().getString("Name");
        final int pos=i.getExtras().getInt("Position");
        String picImage = "pic"+Integer.toString( pos + 1 );
        int id = getApplicationContext().getResources().getIdentifier(picImage,"mipmap",getApplicationContext().getPackageName());
        imageView.setImageResource( id );

        textView.setText( name );
    }

}