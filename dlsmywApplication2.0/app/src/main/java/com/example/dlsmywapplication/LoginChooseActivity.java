package com.example.dlsmywapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.DATA;


//登录选择界面
public class LoginChooseActivity extends AppCompatActivity {

    Button normal_login,worker_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choose);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        normal_login = findViewById(R.id.ToNormalLogin);
        worker_login = findViewById(R.id.ToWorkerLogin);
        normal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick(view);
            }
        });
        worker_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick(view);
            }
        });
    }



        public void myClick(View view) {
            DATA permission = (DATA) getApplication();
            int id = view.getId();
            Intent intent;
            switch (id){
                case R.id.ToNormalLogin:
                    permission.setPermission("0");
                    break;
                case R.id.ToWorkerLogin:
                    permission.setPermission("1");
                    break;
        }
            intent = new Intent(LoginChooseActivity.this, LoginActivity.class);
            startActivity(intent);
    }
}
