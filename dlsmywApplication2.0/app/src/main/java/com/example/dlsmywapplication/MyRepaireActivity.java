package com.example.dlsmywapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.DATA;
import com.example.entity.menumsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.dlsmywapplication.R.layout.activity_my__repaire;

public class MyRepaireActivity extends AppCompatActivity {
    Button norepaire,repaire,back;
    ListView menu;
    int button=1;
    ArrayList<menumsg> msglist=new ArrayList<>();
    UploadAdapter adapter;

    final Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();}
    };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activity_my__repaire);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        norepaire = findViewById(R.id.norepaire);//待维修按键
        repaire= findViewById(R.id.repaire);//已维修按键
        back = findViewById(R.id.Back);
        menu = findViewById(R.id.menu);
        Button function, user;
        function = findViewById(R.id.function);
        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRepaireActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRepaireActivity.this, BackupActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new UploadAdapter(this, R.layout.itemview, msglist);
        menu.setAdapter(adapter);
        DATA data = (DATA) getApplication();
        String userid = data.getId();
        String permission=data.getPermission();
        OkHttpClient client = new OkHttpClient();
        //创建一个Request
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("Userid", userid);
        FbBuilder.add("permission", permission);
        FormBody formBody = FbBuilder.build();
        String url = "http://10.242.146.175:8080/FaultController/getNrfaultByUserid";
        // String url = "http://10.0.2.2:8080/DbController/IsAAccount";
        final Request request = new Request.Builder()
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

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    System.out.println(result+"******");
                    if(!result.isEmpty()){
                    Gson gson = new Gson();
                    ArrayList<Map<String, Object>> list = new ArrayList<>();
                    Type type1=new TypeToken<ArrayList< Map<String, Object>>>(){}.getType();
                    list=gson.fromJson(result, type1);
                    msglist.clear();
                    list.forEach(new Consumer<Map<String, Object> >() {
                        @Override
                        public void accept(Map<String, Object>  map) {
                            msglist.add(new menumsg(map.get("equipment_name").toString(),map.get("fault_description").toString(),map.get("upload_time").toString(),map.get("id").toString()));
                        }
                    });
                    Message msg = new Message();
                    msg.what=0;
                    handler.sendMessage(msg);
                }
                    else {
                        msglist.clear();
                        Message msg = new Message();
                        msg.what=0;
                        handler.sendMessage(msg);}}
            }
        });

        norepaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button=1;
                DATA data = (DATA) getApplication();
                String userid = data.getId();
                String permission=data.getPermission();
                OkHttpClient client = new OkHttpClient();
                //创建一个Request
                FormBody.Builder FbBuilder = new FormBody.Builder();
                FbBuilder.add("Userid", userid);
                FbBuilder.add("permission", permission);
                FormBody formBody = FbBuilder.build();
                String url = "http://10.242.146.175:8080/FaultController/getNrfaultByUserid";
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

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            System.out.println(result);
                            if(!result.isEmpty()){
                            Gson gson = new Gson();
                            ArrayList< Map<String, Object>> list = new ArrayList<>();
                            Type type1=new TypeToken<ArrayList< Map<String, Object>>>(){}.getType();
                            list=gson.fromJson(result, type1);
                            msglist.clear();
                            list.forEach(new Consumer<Map<String, Object> >() {
                                @Override
                                public void accept(Map<String, Object>  map) {
                                    msglist.add(new menumsg(map.get("equipment_name").toString(),map.get("fault_description").toString(),map.get("upload_time").toString(),map.get("id").toString()));
                                }
                            });
                            Message msg = new Message();
                            msg.what=0;
                            handler.sendMessage(msg);
                        }
                        else {
                                msglist.clear();
                                Message msg = new Message();
                                msg.what=0;
                                handler.sendMessage(msg);}
                        }
                    }
                });
            }
        });
        final RefreshableView refreshableView;
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {

            //刷新
            public void onRefresh() {
                try {
                    Thread.sleep(1000);
                    DATA data = (DATA) getApplication();
                    String userid = data.getId();
                    String permission=data.getPermission();
                    String url=null;
                    OkHttpClient client = new OkHttpClient();
                    //创建一个Request
                    FormBody.Builder FbBuilder = new FormBody.Builder();
                    FbBuilder.add("Userid", userid);
                        FbBuilder.add("permission", permission);
                    FormBody formBody = FbBuilder.build();
                   if(button==1)
                        url = "http://10.242.146.175:8080/FaultController/getNrfaultByUserid";
                    else if(button==2)
                        url = "http://10.242.146.175:8080/FaultController/getRfaultByUserid";
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

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String result = response.body().string();
                                System.out.println(result);
                                if(!result.isEmpty()){
                                Gson gson = new Gson();
                                ArrayList< Map<String, Object>> list = new ArrayList<>();
                                Type type1=new TypeToken<ArrayList< Map<String, Object>>>(){}.getType();
                                list=gson.fromJson(result, type1);
                                msglist.clear();
                                if(button==1){
                                    list.forEach(new Consumer<Map<String, Object> >() {
                                        @Override
                                        public void accept(Map<String, Object>  map) {
                                            msglist.add(new menumsg(map.get("equipment_name").toString(),map.get("fault_description").toString(),map.get("upload_time").toString(),map.get("id").toString()));
                                        }
                                    });}
                                else if(button==2){
                                    list.forEach(new Consumer<Map<String, Object> >() {
                                        @Override
                                        public void accept(Map<String, Object>  map) {
                                            msglist.add(new menumsg(map.get("equipment_name").toString(),map.get("repaired_Reason").toString(),map.get("repaired_time").toString(),map.get("id").toString()));
                                        }
                                    });}
                                Message msg = new Message();
                                msg.what=0;
                                handler.sendMessage(msg);
                            }
                                else {
                                    msglist.clear();
                                    Message msg = new Message();
                                    msg.what=0;
                                    handler.sendMessage(msg);}}
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);

        repaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button=2;
                DATA data = (DATA) getApplication();
                String userid = data.getId();
                String permission=data.getPermission();
                OkHttpClient client = new OkHttpClient();
                //创建一个Request
                FormBody.Builder FbBuilder = new FormBody.Builder();
                FbBuilder.add("Userid", userid);
                FbBuilder.add("permission", permission);
                FormBody formBody = FbBuilder.build();
                String url = "http://10.242.146.175:8080/FaultController/getRfaultByUserid";
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

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            System.out.println(result);
                            if(!result.isEmpty()){
                            Gson gson = new Gson();
                            ArrayList< Map<String, Object>> list = new ArrayList<>();
                            Type type1=new TypeToken<ArrayList< Map<String, Object>>>(){}.getType();
                            list=gson.fromJson(result, type1);
                            msglist.clear();
                            list.forEach(new Consumer<Map<String, Object> >() {
                                @Override
                                public void accept(Map<String, Object>  map) {
                                    msglist.add(new menumsg(map.get("equipment_name").toString(),map.get("repaired_Reason").toString(),map.get("repaired_time").toString(),map.get("id").toString()));
                                }
                            });
                            Message msg = new Message();
                            msg.what=0;
                            handler.sendMessage(msg);
                        }
                            else {
                                msglist.clear();
                                Message msg = new Message();
                                msg.what=0;
                                handler.sendMessage(msg);}}
                    }
                });

            }
        });

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(button==1){
                    menumsg m=msglist.get(i);
                    Intent intent = new Intent(MyRepaireActivity.this,  DetailNofinish2Activity .class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("eq_name",m.getMsg1());
                    mBundle.putString("time",m.getMsg3());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
                else if(button==2)
                {
                    menumsg m=msglist.get(i);
                    Intent intent = new Intent(MyRepaireActivity.this,  DetailFinishActivity .class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("eq_name",m.getMsg1());
                    mBundle.putString("time",m.getMsg3());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
        });
    }
}