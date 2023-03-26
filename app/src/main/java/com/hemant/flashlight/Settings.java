package com.hemant.flashlight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    TextView title1,textBox1,textBox2,textBox3,textBox4,textBox5;
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
    boolean c1,c2,c3,c4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        title1= findViewById(R.id.title1);
        checkBox1 = findViewById(R.id.checkbox1);
        textBox1 = findViewById(R.id.textbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        textBox2 = findViewById(R.id.textbox2);
        checkBox3=findViewById(R.id.checkbox3);
        textBox3=findViewById(R.id.textbox3);
        checkBox4=findViewById(R.id.checkbox4);
        textBox4=findViewById(R.id.textbox4);
        textBox5=findViewById(R.id.textbox5);


        textBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this app ");
                intent.putExtra(Intent.EXTRA_TEXT,"Click on the link to download:\n https://drive.google.com/file/d/12rRqCInaHcA_phDpuL1bAe2lkjx2opB4/view?usp=drivesdk");
                startActivity(Intent.createChooser(intent,"Share this app"));
            }
        });
        startTouch();
        exitTouch();
        switchSound();
        switchVibration();
        }

    public void startTouch(){
            SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            checkBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c1 = checkBox1.isChecked();
                    ed.putBoolean("checkbox1",c1);
                    ed.apply();
                    if(c1){
                        textBox1.setText("Enabled");
                    }
                    else{
                        textBox1.setText("Disabled");
                    }
                }
            });
            boolean new1 = sharedPreferences.getBoolean("checkbox1",false);
            if(new1){
                textBox1.setText("Enabled");
            }
            else{
                textBox1.setText("Disabled");
            }
            checkBox1.setChecked(new1);
        }

        public void exitTouch(){
            SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            checkBox2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c2 = checkBox2.isChecked();
                    ed.putBoolean("checkbox2",c2);
                    ed.apply();
                    if(c2){
                        textBox2.setText("Enabled");
                    }
                    else{
                        textBox2.setText("Disabled");
                    }
                }
            });
            boolean new2 = sharedPreferences.getBoolean("checkbox2",false);
            if(new2){
                textBox2.setText("Enabled");
            }
            else{
                textBox2.setText("Disabled");
            }
            checkBox2.setChecked(new2);
        }
        public void switchSound(){
            SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c3= checkBox3.isChecked();
                ed.putBoolean("checkbox3",c3);
                ed.apply();
                if(c3){
                    textBox3.setText("Enabled");
                }
                else{
                    textBox3.setText("Disabled");
                }
            }
        });
            boolean new3 = sharedPreferences.getBoolean("checkbox3",false);
            if(new3){
                textBox3.setText("Enabled");
            }
            else{
                textBox3.setText("Disabled");
            }
            checkBox3.setChecked(new3);

        }
    public void switchVibration() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c4= checkBox4.isChecked();
                ed.putBoolean("checkbox4",c4);
                ed.apply();
                if(c4){
                    textBox4.setText("Enabled");
                }
                else{
                    textBox4.setText("Disabled");
                }
            }
        });
        boolean new4 = sharedPreferences.getBoolean("checkbox4",false);
        if(new4){
            textBox4.setText("Enabled");
        }
        else{
            textBox4.setText("Disabled");
        }
        checkBox4.setChecked(new4);
    }
}