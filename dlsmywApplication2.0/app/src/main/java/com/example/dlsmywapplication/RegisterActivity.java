package com.example.dlsmywapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.app.DATA;
import com.example.app.DATA;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText username,password,confirm;
    Button register_button;
    TextView name,pwd,con_pwd,flag;

    private Handler handler = new Handler(){
        //3.秘书处理消息的方法
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            if(text.equals("1"))
            {
                Intent intent = null;
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

            }
            else if(text.equals("0"))
                show_error("用户名已存在！");
            else if(text.equals("-1"))
                show_error("服务器数据库操作错误");
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_resigister);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        username=findViewById(R.id.user_name);
        password=findViewById(R.id.use_pwd);
        confirm=findViewById(R.id.confirm_pwd);
        register_button=findViewById(R.id.register_button);
        name=findViewById(R.id.name);
        pwd=findViewById(R.id.pwd);
        con_pwd=findViewById(R.id.con_pwd);
        flag=findViewById(R.id.flag);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((username.getText().toString().isEmpty() )){
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();}
                else if((password.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();}
                else if(password.getText().toString().equals(confirm.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "两次输入的密码不一致，请确认", Toast.LENGTH_SHORT).show();}
                DATA data = (DATA)getApplication();
                String permission = data.getPermission();
                String user_name = username.getText().toString();
                String pwd = password.getText().toString();
                String phonenumber = confirm.getText().toString();
                OkHttpClient client = new OkHttpClient();
                //创建一个Request
                FormBody.Builder FbBuilder = new FormBody.Builder();
                FbBuilder.add("Username",user_name);
                FbBuilder.add("Employee_permissions",permission);
                FbBuilder.add("Phone_number",phonenumber);
                FbBuilder.add("Login_password",pwd);
                FormBody formBody = FbBuilder.build();
                //String url = "http://10.242.146.175:8080/UserController/Register";
                String url = "http://10.242.146.175:8080/UserController/Register";
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
                        Message msg = new Message();
                        msg.obj ="false";
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            System.out.println("%%%%%%%%%"+str);
                            Message msg = new Message();
                            if(str.equals("1"))
                                msg.obj ="1";
                            else if(str.equals("0"))
                                msg.obj ="0";
                            else
                                msg.obj = "-1";
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
        if(username.getText().toString().isEmpty()) {
            username.setError("用户名不能为空");
            username.requestFocus();
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("密码不能为空");
            password.requestFocus();
        }
    }
}
