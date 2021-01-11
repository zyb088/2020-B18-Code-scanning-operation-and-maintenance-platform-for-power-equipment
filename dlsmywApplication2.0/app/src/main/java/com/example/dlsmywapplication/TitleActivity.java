package com.example.dlsmywapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


//公共标题类（无业务逻辑）
public class TitleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
