package com.example.dlsmywapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ChangePwdActivity extends AppCompatActivity {
    EditText user_name,password,confirm,veri_code;
    Button change_button,veri_button;

    private Handler handler = new Handler(){
        //3.秘书处理消息的方法
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            if(text.equals("1"))
            {
                Intent intent = null;
                Toast.makeText(getApplicationContext(), "密码重置成功", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "密码重置成功", Toast.LENGTH_LONG).show();
            }

            else if(text.equals("-1"))
                show_error("服务器数据库操作错误");
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_change_pwd);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        user_name=findViewById(R.id.user_name);
        password=findViewById(R.id.old_pwd);
        confirm=findViewById(R.id.new_pwd);
        veri_code=findViewById(R.id.confirm);
        change_button=findViewById(R.id.adjust_button);
        veri_button=findViewById(R.id.GetPermi_work);

        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("1----"+password.getText().toString());
                System.out.println("1----"+confirm.getText().toString());
                if((user_name.getText().toString().isEmpty() )){
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();}
                else if((password.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();}
                else if(!password.getText().toString().equals(confirm.getText().toString())&&password.getText()!=null&&confirm.getText()!=null) {
                    Toast.makeText(getApplicationContext(), "两次输入的密码不一致，请确认", Toast.LENGTH_SHORT).show();}
                else if((veri_code.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_LONG).show();}
                String username = user_name.getText().toString();
                String pwd = password.getText().toString();
                String phonenumber = veri_code.getText().toString();
                OkHttpClient client = new OkHttpClient();
                //创建一个Request
                FormBody.Builder FbBuilder = new FormBody.Builder();
                FbBuilder.add("Username",username);
                FbBuilder.add("PhoneNumber",phonenumber);
                FbBuilder.add("NewPassword",pwd);
                FormBody formBody = FbBuilder.build();

                String url = "http://10.242.146.175:8080/UserController/UpdateUserByPhonenumber";
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
                            if(str.equals("true"))
                                msg.obj ="1";
                            else
                                msg.obj = "false";
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
}

