package com.example.dlsmywapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.DATA;
import com.example.manager.MenuItemLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//需要移植
//个人中心用户类
public class UserActivity extends AppCompatActivity {

    MenuItemLayout menuItemLayout_Up,menuItemLayout_Safe,menuItemLayout_move;
    TextView username,usernum;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String uname = bundle.getString("uname");
            String unum = bundle.getString("unum");
            username.setText(uname);
            usernum.setText(unum);
            super.handleMessage(msg);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        menuItemLayout_Safe = findViewById(R.id.menuItemLayout_Safe);
        menuItemLayout_Up=findViewById(R.id.menuItemLayout_Up);
        username=findViewById(R.id.user_name);
        usernum=findViewById(R.id.user_num);
        menuItemLayout_move=findViewById(R.id.menuItemLayout_move);
        DATA data = (DATA) getApplication();
        String permission=data.getPermission();
        String userid=data.getId();
        System.out.println(userid+"******");
        OkHttpClient client = new OkHttpClient();
        //创建一个Request
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("UserId",userid);
        FormBody formBody = FbBuilder.build();
        String url = "http://10.242.146.175:8080/UserController/getUserInfo";
        // String url = "http://10.0.2.2:8080/DbController/IsAAccount";
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        System.out.println(request);
        System.out.println(formBody.toString());
        //通过client发起请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("----------error------------");
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("--------successful-----------");
                    String result = response.body().string();
                    Gson gson = new Gson();
                    Map<String, Object> map = new HashMap<String, Object>();
                    Bundle bundle = new Bundle();
                    System.out.println(result);
                    map = gson.fromJson(result, map.getClass());
                    bundle.putString("uname",(String)map.get("username"));
                    bundle.putString("unum",(String)map.get("phone_number"));
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        if(permission=="0"){
                menuItemLayout_Up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserActivity.this,UploadActivity.class);
                        startActivity(intent);
                    }
                });}
        else if(permission=="1")
        {menuItemLayout_Up.setTitleText("我的维修");

            menuItemLayout_Up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserActivity.this,MyRepaireActivity.class);
                    startActivity(intent);
                }
            });
        }
        menuItemLayout_Safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,AdjustPwdActivity.class);
                startActivity(intent);
            }
        });
        Button function,user;
        function=findViewById(R.id.function);
        user=findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        menuItemLayout_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserActivity.this,LoginChooseActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,BackupActivity.class);
                startActivity(intent);
            }
        });
    }
}
