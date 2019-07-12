package com.example.andrewspc.connectv6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ZhiAnTutorial extends AppCompatActivity {

    private EditText input1;
    private EditText input2;
    private Button match;

    private String name;
    private String name2;

    private boolean onOFF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_an_tutorial);

        input1 = findViewById(R.id.Input1);
        input2 = findViewById(R.id.Input2);
        match = findViewById(R.id.matchButton);

    }
}
