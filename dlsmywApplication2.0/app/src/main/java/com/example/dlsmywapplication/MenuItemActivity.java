package com.example.dlsmywapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


//工具类
public class MenuItemActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_layout);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
