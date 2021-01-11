package com.example.dlsmywapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//需要移植
public class RepairReportActivity extends AppCompatActivity {
Button upload;
TextView eqid,phone;
EditText faultre,rere;
    String uptime,EQID,Phone;
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==0)
            showmessage1();
            else if(msg.what==1)
                showmessage2();

        }
    };
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
         uptime=intent.getStringExtra("time");
         EQID=intent.getStringExtra("id");
         Phone=intent.getStringExtra("phone");
        setContentView(R.layout.m_worker_upload);
        upload=findViewById(R.id.progress_button);
        eqid=findViewById(R.id.id_show);
        phone=findViewById(R.id.phone_show);
        faultre=findViewById(R.id.faultre);
        rere=findViewById(R.id.rere);
        eqid.setText(EQID);
        TextView titleText=findViewById(R.id.titleText);
        titleText.setText("修复报告上传");
        Button titleBack=findViewById(R.id.titleBack);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        phone.setText(Phone);
        System.out.println(EQID+"&&&&"+Phone+"&&&&"+uptime);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fre=faultre.getText().toString();
                String rre=rere.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String datestr = simpleDateFormat.format(date);
                if(fre.isEmpty()||rre.isEmpty())
                {
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
                else
                {
                    FormBody.Builder FbBuilder = new FormBody.Builder();
                    FbBuilder.add("EquipmentId",EQID);
                    FbBuilder.add("Fault_UploadTime", uptime);
                    FbBuilder.add("Repaired_Reason",fre);
                    FbBuilder.add("Repaired_report",rre);
                    FbBuilder.add("Repaired_time",datestr);
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = FbBuilder.build();
                    String url = "http://10.242.146.175:8080/FaultController/UpdateFault";
                    //  String url = "http://10.0.2.2:8080/DbController/UpdateEquipment";
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
                                handler.sendMessage(msg);
                            }
                        }
                    });

                }
            }
        });
        Button function,user;
        function=findViewById(R.id.function);
        user=findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RepairReportActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RepairReportActivity.this, BackupActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showmessage1()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RepairReportActivity.this);
        builder1.setMessage("修复报告上传成功！");
        builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Intent intent = new Intent(getApplicationContext(),MyRepaireActivity.class);
                startActivity(intent);
            }
        });
        builder1.create().show();}
    private void showmessage2()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RepairReportActivity.this);
        builder1.setMessage("故障原因和修复报告不得为空！");
        builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder1.create().show();}
}