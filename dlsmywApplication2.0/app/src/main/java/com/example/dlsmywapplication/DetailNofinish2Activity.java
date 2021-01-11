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
public class DetailNofinish2Activity extends AppCompatActivity {
    TextView eqid,eqname,eqposi,worker,phone,faulttime,faultdes;
    Button upload;
    String Time,Phone,EqId;
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            eqid.setText(bundle.getString("eqid"));
            eqname.setText(bundle.getString("eqname"));
            eqposi.setText(bundle.getString("eqposit"));
            worker.setText(bundle.getString("worker"));
            phone.setText(bundle.getString("phone"));
            faulttime.setText(bundle.getString("time"));
            faultdes.setText(bundle.getString("faultdes"));
            super.handleMessage(msg);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.worker_details_notcompleted);
        Intent intent = getIntent();
        String eq_name=intent.getStringExtra("eq_name");
        final String time=intent.getStringExtra("time");
        eqid=findViewById(R.id.eqid2);
        eqname=findViewById(R.id.eqname2);
        eqposi=findViewById(R.id.eqposi2);
        worker=findViewById(R.id.worker2);
        phone=findViewById(R.id.phone2);
        faulttime=findViewById(R.id.upload_time2);
        faultdes=findViewById(R.id.faultdes2);
        TextView titleText=findViewById(R.id.titleText);
        titleText.setText("待维修");
        Button titleBack=findViewById(R.id.titleBack);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        upload=findViewById(R.id.uploadreport);
        System.out.println(eq_name+"    ***"+time);
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("Equipment_name",eq_name);
        FbBuilder.add("Fault_UploadTime",time);
        FormBody formBody = FbBuilder.build();
        String url = "http://10.242.146.175:8080/FaultController/GetNrfaultDetail0";
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
                EqId=(String)map.get("equipment_id");
                bundle.putString("eqid",EqId);
                bundle.putString("eqname",(String)map.get("equipment_name"));
                bundle.putString("eqposit",(String)map.get("equipment_position"));
                bundle.putString("worker",(String)map.get("postman_name"));
                bundle.putString("phone",(String)map.get("postman_PhoneNumber"));
                bundle.putString("time",(String)map.get("upload_time"));
                bundle.putString("faultdes",(String)map.get("fault_description"));
                Time=(String)map.get("upload_time");
                Phone=(String)map.get("equipmentmanager_PhoneNumber");
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailNofinish2Activity.this, RepairReportActivity.class);
               Bundle msg=new Bundle();
               msg.putString("time",Time);
               msg.putString("id",EqId);
               msg.putString("phone",Phone);
               intent.putExtras(msg);
                startActivity(intent);

            }
        });
    }
}