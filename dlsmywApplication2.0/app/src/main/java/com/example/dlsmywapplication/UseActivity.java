package com.example.dlsmywapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UseActivity extends AppCompatActivity {

    TextView manufacturersid,userid,dateid;
    Button submit;
    String datestr="";

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0)showmessage();

        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        manufacturersid = findViewById(R.id.i_manufacturers);
        submit = findViewById(R.id.submit_info);
        userid = findViewById(R.id.i_user);
        dateid = findViewById(R.id.i_date);
        TextView titleText=findViewById(R.id.titleText);
        titleText.setText("使用记录上传");
        Button titleBack=findViewById(R.id.titleBack);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent thisintent = getIntent();
        String mfid = thisintent.getStringExtra("manufacturersid");
        manufacturersid.setText(mfid);
        final DATA data = (DATA) getApplication();
        String username = data.getUsername();
        userid.setText(username);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        datestr = simpleDateFormat.format(date);
        dateid.setText(datestr);
        Button function,user;
        function=findViewById(R.id.function);
        user=findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UseActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UseActivity.this,BackupActivity.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = data.getId();
                OkHttpClient client = new OkHttpClient();//创建服务器对象
                //创建一个Request
                FormBody.Builder FbBuilder = new FormBody.Builder();
                FbBuilder.add("userid",id);
                FbBuilder.add("manufacturersid",manufacturersid.getText().toString());
                FbBuilder.add("date",datestr);
                FormBody formBody = FbBuilder.build();
                String url = "http://10.242.146.175:8080/UseRecordController/PutUse";
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();
                System.out.println(request);
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("----------error------------");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        System.out.println(result);
                        Message msg = new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                    }
                });
            }
        });


    }
    private void showmessage()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UseActivity.this);
        builder1.setMessage("使用记录上传成功！");
        builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Intent intent = new Intent(UseActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
        builder1.create().show();}
}
