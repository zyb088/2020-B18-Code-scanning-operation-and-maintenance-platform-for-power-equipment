package com.example.dlsmywapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//测试类
public class TestActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.tv_1);
        OkHttpClient client = new OkHttpClient();
        //创建一个Request
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("id","1");
        FormBody formBody = FbBuilder.build();
        String url = "http://10.242.146.175:8080/DbController/GetEquipmentInfo";
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
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    System.out.println(str);
                }
            }
        });
        System.out.println("request end!!!");
    }
}