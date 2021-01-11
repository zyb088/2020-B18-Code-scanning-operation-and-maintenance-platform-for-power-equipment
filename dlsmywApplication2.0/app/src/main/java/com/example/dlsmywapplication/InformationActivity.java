package com.example.dlsmywapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.DATA;
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


//信息设备查看界面
public class InformationActivity  extends AppCompatActivity {

    TextView i_id,i_manufacturers,i_date,i_enable,i_circuit,i_administrator,i_ad_number,i_name,titleText;
    Button use,upload;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String id = bundle.getString("id");
            String manufacturers = bundle.getString("i_manufacturers");
            String date = bundle.getString("i_date");
            String enable = bundle.getString("i_enable");
            String circuit = bundle.getString("i_circuit");
            String administrator = bundle.getString("i_administrator");
            String ad_number = bundle.getString("i_ad_number");
            String name = bundle.getString("i_name");
            i_id.setText(id);
            i_manufacturers.setText(manufacturers);
            i_date.setText(date);
            i_enable.setText(enable);
            i_circuit.setText(circuit);
            i_administrator.setText(administrator);
            i_ad_number.setText(ad_number);
            i_name.setText(name);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        i_id = findViewById(R.id.i_id);
        i_manufacturers = findViewById(R.id.i_manufacturers);
        i_date = findViewById(R.id.i_date);
        i_enable = findViewById(R.id.i_enable);
        i_circuit = findViewById(R.id.i_circuit);
        i_administrator = findViewById(R.id.i_administrator);
        i_ad_number = findViewById(R.id.i_ad_number);
        i_name = findViewById(R.id.i_name);
        upload=findViewById(R.id.upload);
        titleText=findViewById(R.id.titleText);
        titleText.setText("设备信息");
        Button titleBack=findViewById(R.id.titleBack);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        use=findViewById(R.id.check_sub);
        Button function,user;
        function=findViewById(R.id.function);
        user=findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationActivity.this,BackupActivity.class);
                startActivity(intent);
            }
        });
//        //刷新代码
//        final RefreshableView refreshableView;
//        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
//        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
//            @Override
//            public void onRefresh() {
//                try {
//                    Thread.sleep(1000);
//                    finish();
//                    Intent intent = new Intent(getApplicationContext(),InformationActivity.class);
//                    startActivity(intent);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                refreshableView.finishRefreshing();
//            }
//        }, 0);

        OkHttpClient client = new OkHttpClient();
        //创建一个Request
//        Intent intent = getIntent();
//        String id = intent.getStringExtra("id");
        DATA data = (DATA) getApplication();
        String id  = data.getEquipmentid();
        System.out.println("id-------"+id);
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("id",id);
        FormBody formBody = FbBuilder.build();
        //String url = "http://10.242.198.87:8080/EquipmentController/GetEquipmentInfo";
        String url = "http://10.242.146.175:8080/EquipmentController/GetEquipmentInfo";
        Request request = new Request.Builder()
                .post(formBody)
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
                    Double id = (Double) map.get("id");
                    Integer idint = new Integer(id.intValue());
                    bundle.putString("id",idint.toString());
                    bundle.putString("i_manufacturers",(String)map.get("manufacturer"));
                    bundle.putString("i_date",(String)map.get("production_Date"));
                    bundle.putString("i_enable",(String)map.get("activation_Date"));
                    String circuit_min = map.get("voltage_min").toString();
                    String circuit_max = map.get("voltage_max").toString();
                    String circuit = circuit_min+"V - "+circuit_max+"V";
                    bundle.putString("i_circuit",circuit);
                    bundle.putString("i_administrator",(String)map.get("equipment_manager"));
                    bundle.putString("i_ad_number",(String)map.get("contact_information"));
                    bundle.putString("i_name",(String)map.get("equipment_name"));
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });
        System.out.println("request end!!!");

        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manufacturersid = i_id.getText().toString();
                Intent intent = new Intent(getApplicationContext(),UseActivity.class);
                intent.putExtra("manufacturersid",manufacturersid);
                startActivity(intent);
//                finish();
//                Intent intent = new Intent(getApplicationContext(),InformationActivity.class);
//                startActivity(intent);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manufacturersid = i_id.getText().toString();
                Intent intent = new Intent(getApplicationContext(),Fault_uploadActivity.class);
                intent.putExtra("manufacturersid",manufacturersid);
                startActivity(intent);

            }
        });
    }

}
