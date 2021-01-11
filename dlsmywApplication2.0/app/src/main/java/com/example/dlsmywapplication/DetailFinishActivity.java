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
public class DetailFinishActivity extends AppCompatActivity {
    TextView eqid,eqname,worker,phone,uptime,retime,faultreason,rere;
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            eqid.setText(bundle.getString("eqid"));
            eqname.setText(bundle.getString("eqname"));
            worker.setText(bundle.getString("worker"));
            phone.setText(bundle.getString("phone"));
            uptime.setText(bundle.getString("uptime"));
            retime.setText(bundle.getString("retime"));
            faultreason.setText(bundle.getString("faultreason"));
            rere.setText(bundle.getString("rere"));
            super.handleMessage(msg);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.details_completed);
        Intent intent = getIntent();
        String eq_name=intent.getStringExtra("eq_name");
        String time=intent.getStringExtra("time");
        eqid=findViewById(R.id.Eqid);
        eqname=findViewById(R.id.Eqname);
        worker=findViewById(R.id.Worker);
        phone=findViewById(R.id.Phone);
        uptime=findViewById(R.id.upload_time);
        retime=findViewById(R.id.Repair_time);
        faultreason=findViewById(R.id.faultreason);
        rere=findViewById(R.id.repair_report);
        TextView titleText=findViewById(R.id.titleText);
        titleText.setText("已维修");
        Button titleBack=findViewById(R.id.titleBack);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        System.out.println(eq_name+"    ***"+time);
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("Equipment_name",eq_name);
        FbBuilder.add("Fault_RepairedTime",time);
        FormBody formBody = FbBuilder.build();
        String url = "http://10.242.146.175:8080/FaultController/GetRfaultDetail0";
        // String url = "http://10.0.2.2:8080/DbController/GetEquipmentInfo";
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        //通过client发起请求
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("----------error------------");
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println("--------successful-----------");
                String result = response.body().string();
                System.out.println(result);
                Gson gson = new Gson();
                Map<String, Object> map = new HashMap<String, Object>();
                Bundle bundle = new Bundle();
                map = gson.fromJson(result, map.getClass());
               bundle.putString("eqid",(String)map.get("equipment_id"));
               bundle.putString("eqname",(String)map.get("equipment_name"));
               bundle.putString("worker",(String)map.get("managermanName"));
               bundle.putString("phone",(String)map.get("managermanPhonenumber"));
                bundle.putString("uptime",(String)map.get("upload_time"));
                bundle.putString("retime",(String)map.get("repaired_time"));
                bundle.putString("faultreason",(String)map.get("repaired_Reason"));
                bundle.putString("rere",(String)map.get("repaired_report"));
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
    }
}