package com.example.dlsmywapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.app.DATA;
import com.google.gson.Gson;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//登录界面
public class LoginActivity extends AppCompatActivity {

    EditText user_name,password;
    Button loginbutton;
    TextView Forgetpwd,create_normal_account,usertype;
    ImageView imageView;
    CardView load;
    AnimationDrawable animation;

    private Handler handler = new Handler(){
        //3.秘书处理消息的方法
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            if(result.equals("successful")) {
                DATA data = (DATA) getApplication();
                String id = bundle.getString("id");
                data.setId(id);
                String username = bundle.getString("username");
                data.setUsername(username);
                finish();
                Intent intent = new Intent(LoginActivity.this,BackupActivity.class);
                startActivity(intent);
            }
            else if(result.equals("noresult"))
            {
                show_error("账户名或密码不正确！！！");
                animation.stop();
                load.setVisibility(View.INVISIBLE);
            }
            else if(result.equals("error"))
            {
                show_error("网络连接错误！！！");
                animation.stop();
                load.setVisibility(View.INVISIBLE);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        loginbutton = findViewById(R.id.login);
        Forgetpwd = findViewById(R.id.Forgetpwd);
        create_normal_account = findViewById(R.id.create_normal_account);
        usertype = findViewById(R.id.usertype);
        imageView = findViewById(R.id.loadingprogressimg);
        load = findViewById(R.id.load);



        DATA data = (DATA) getApplication();
        String permission = data.getPermission();
        if(permission == "0")
            usertype.setText("For Customer");
        else if(permission=="1")
            usertype.setText("For Maintenance");
        //通过client发起请求
        Forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this,ChangePwdActivity.class);
                startActivity(intent);
            }
        });
        create_normal_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Myonclck(view);
                load.setVisibility(View.VISIBLE);
                animation = (AnimationDrawable) imageView.getDrawable();
                animation.start();
                String username = user_name.getText().toString();
                String pwd = password.getText().toString();
                OkHttpClient client = new OkHttpClient();
                //创建一个Request
                FormBody.Builder FbBuilder = new FormBody.Builder();
                FbBuilder.add("uname",username);
                FbBuilder.add("pword",pwd);
                DATA data = (DATA)getApplication();
                String permission = data.getPermission();
                FbBuilder.add("permission",permission);
                FormBody formBody = FbBuilder.build();
                //String url = "http://10.242.198.87:8080/UserController/IsInvalid";
                String url = "http://10.242.146.175:8080/UserController/IsInvalid";
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
                        Bundle bundle = new Bundle();
                        bundle.putString("result","error");
                        Message msg = new Message();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            System.out.println(result);
                            Bundle bundle = new Bundle();
                            if(result.isEmpty()){
                                bundle.putString("result","noresult");
                            }
                            else {
                                //创建解析工具类
                                Gson gson = new Gson();
                                Map<String, Object> map = new HashMap<String, Object>();
                                System.out.println(result);
                                map = gson.fromJson(result, map.getClass());
                                bundle.putString("result","successful");
                                bundle.putString("id", map.get("id").toString());
                                bundle.putString("username", map.get("username").toString());
                            }
                            Message msg = new Message();
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
                System.out.println("request end!!!");
            }
        });
    }

    private void show_error(String info)
    {
        AestheticDialog.Builder builder =new  AestheticDialog.Builder(this, DialogStyle.FLAT, DialogType.ERROR);
        builder.setTitle("错误")
                .setMessage(info)
                .setCancelable(false)
                .setDarkMode(false)
                .setGravity(Gravity.CENTER)
                .setAnimation(DialogAnimation.SHRINK)
                .setOnClickListener(new OnDialogClickListener() {
                    @Override
                    public void onClick(@NotNull AestheticDialog.Builder builder) {
                        builder.dismiss();
                    }
                }).show();
    }
    private void Myonclck(View view)
    {
        if(user_name.getText().toString().isEmpty()) {
            user_name.setError("用户名不能为空");
            user_name.requestFocus();
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("密码不能为空");
            password.requestFocus();
        }
    }
}
