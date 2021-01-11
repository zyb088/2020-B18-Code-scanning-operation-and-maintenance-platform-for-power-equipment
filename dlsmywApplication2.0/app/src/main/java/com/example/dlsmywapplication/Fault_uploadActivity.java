



package com.example.dlsmywapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.DATA;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.dlsmywapplication.R.layout.activity_my__upload;
import static com.example.dlsmywapplication.R.layout.activity_upload;

public class Fault_uploadActivity extends AppCompatActivity {
    TextView eq_id,eq_name,eq_adress,up_num,up_name,fault;
    Button submit;


    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String id = bundle.getString("eq_id");
            String name = bundle.getString("eq_name");
            String adress = bundle.getString("eq_adress");
            String wname= bundle.getString("up_name");
            String wnum= bundle.getString("up_num");
            eq_id.setText(id);
            eq_name.setText(name);
            eq_adress.setText(adress);
            up_name.setText(wname);
            up_num.setText(wnum);
            super.handleMessage(msg);
        }
    };
    final Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0)showmessage("故障上传成功！");
            if(msg.what==1)showmessage2("故障描述不能为空！");
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_upload);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        eq_id = findViewById(R.id.eq_id);
        eq_name = findViewById(R.id.eq_name);
        eq_adress= findViewById(R.id.eq_adress);
        up_name = findViewById(R.id.up_name);
        up_num = findViewById(R.id.up_num);
        fault = findViewById(R.id.fault);
        submit = findViewById(R.id.submit);
        TextView titleText=findViewById(R.id.titleText);
        titleText.setText("故障上传");
        Button titleBack=findViewById(R.id.titleBack);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button function,user;
        function=findViewById(R.id.function);
        user=findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fault_uploadActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fault_uploadActivity.this,BackupActivity.class);
                startActivity(intent);
            }
        });
        OkHttpClient client = new OkHttpClient();
        //创建一个Request
//        Intent intent = getIntent();
//        String id = intent.getStringExtra("id");
        DATA data = (DATA) getApplication();
        String id  = data.getEquipmentid();
        String workerid=data.getId();
        FormBody.Builder FbBuilder = new FormBody.Builder();
        System.out.println(id+"*************"+workerid);
        FbBuilder.add("manufacturersid",id);
        FbBuilder.add("userid",workerid);
        FormBody formBody2 = FbBuilder.build();
        String url = "http://10.242.146.175:8080/UserController/GetEquipment_UserInfo";
        // String url = "http://10.0.2.2:8080/FaultController/GetEquipmentInfo";
        Request request = new Request.Builder()
                .post(formBody2)
                .url(url)
                .build();
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
                    Double id = (Double) map.get("equipmentid");
                    bundle.putString("eq_id",id.toString());
                    bundle.putString("eq_name",(String)map.get("equipment_name"));//设备名称
                    bundle.putString("eq_adress",(String)map.get("equipment_position"));//设备地址
                    bundle.putString("up_name",(String)map.get("username"));//工人姓名
                    bundle.putString("up_num",(String)map.get("phone_number"));//工人电话
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });
        System.out.println("request end!!!");

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DATA data = (DATA) getApplication();
                String id  = data.getEquipmentid();
                String workerid=data.getId();
                String eq_fault= fault.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String datestr = simpleDateFormat.format(date);
                if(eq_fault.isEmpty())
                {
                    Message msg = new Message();
                    msg.what=1;
                    handler2.sendMessage(msg);
                }
                else{
                //创建一个Request
                FormBody.Builder FbBuilder1 = new FormBody.Builder();
                FbBuilder1.add("eq_id",id);
                FbBuilder1.add("postman_id",workerid);
                FbBuilder1.add("fault_des",eq_fault);
                FbBuilder1.add("up_time",datestr);
                OkHttpClient client = new OkHttpClient();
                FormBody formBody = FbBuilder1.build();
                String url = "http://10.242.146.175:8080/FaultController/PutFault";
                //  String url = "http://10.0.2.2:8080/FaultController/UpdateEquipment";
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();
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
                            String str = response.body().string();
                            System.out.println(str);
                            Message msg = new Message();
                            msg.what=0;
                            handler2.sendMessage(msg);
                        }
                    }
                });




            }}
        });


    }
    private void showmessage(String ifo)
    {
    AlertDialog.Builder builder1 = new AlertDialog.Builder(Fault_uploadActivity.this);
            builder1.setMessage(ifo);
            builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    });
            builder1.create().show();}
    private void showmessage2(String ifo)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Fault_uploadActivity.this);
        builder1.setMessage(ifo);
        builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder1.create().show();}
}