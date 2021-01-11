package com.example.dlsmywapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//公共控件栏类（无业务逻辑）
public class BottomTitleActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_title);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
