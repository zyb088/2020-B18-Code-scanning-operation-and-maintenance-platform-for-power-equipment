package com.example.dlsmywapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.app.DATA;
import com.google.gson.Gson;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//检修界面
//在检修界面可以修改设备的各项信息
public class CheckActivity  extends AppCompatActivity {

    RadioButton radioButtonA1,radioButtonB1,radioButtonC1,radioButtonD1;//设备等级
    RadioButton State1Y,State1N;//设备运行是否正常
    RadioButton State2Y,State2N;//计量仪表、监测仪表及故障指示灯是否正常
    RadioButton State3Y,State3N;//配电回路是否正常
    RadioButton State4Y,State4N;//供电方式、母线状态是否正常
    String Equipment_level,Equipment_name,Voltage_max,Voltage_min,Equipment_situation,Instrument_situation,
            Circuit_situation,Generatrix_situation;
    EditText eq_name,eq_number,v_min,v_max;
    ProgressButton progress_button;
    int progress = 0;
    Timer timer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //秘书 用于异步多线程处理图像渲染的内容
        final Handler handler = new Handler() {
            //3.秘书处理消息的方法
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                String result1 = data.getString("result1");
                if (!(result1==null)&&result1.equals("successful")) {
                    String equipment_name = data.getString("equipment_name");
                    eq_name.setText(equipment_name);
                    String equipment_id = data.getString("equipment_id");
                    eq_number.setText(equipment_id);
                    String voltage_min = data.getString("voltage_min");
                    v_min.setText(voltage_min);
                    String voltage_max = data.getString("voltage_max");
                    v_max.setText(voltage_max);
                } else if (!(result1==null)&&result1.equals("null")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setMessage("数据库中不存在该设备的信息！！！");
                    alertDialog.show();
                }
                else if(!(result1==null)&&result1.equals("error"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setMessage("网络连接错误！！！");
                    alertDialog.show();
                }

                if (msg.what == 0)
                    setProgress();
                super.handleMessage(msg);
            }
        };

        radioButtonA1 = findViewById(R.id.check_A);
        radioButtonB1 = findViewById(R.id.check_B);
        radioButtonC1 = findViewById(R.id.check_C);
        radioButtonD1 = findViewById(R.id.check_D);
        eq_name = findViewById(R.id.eq_name);
        eq_number = findViewById(R.id.eq_number);
        v_min = findViewById(R.id.voltage_min);
        v_max = findViewById(R.id.voltage_max);
        State1Y = findViewById(R.id.state1_Y);
        State1N = findViewById(R.id.state1_N);
        State2Y = findViewById(R.id.state2_Y);
        State2N = findViewById(R.id.state2_N);
        State3Y = findViewById(R.id.state3_Y);
        State3N = findViewById(R.id.state3_N);
        State4Y = findViewById(R.id.state4_Y);
        State4N = findViewById(R.id.state4_N);
        progress_button = findViewById(R.id.progress_button);
        progress_button.setMaxProgress(100);
        progress_button.setMinProgress(0);
        progress_button.setProgress(1);
        TextView titleText=findViewById(R.id.titleText);
        titleText.setText("保养信息");
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
                Intent intent = new Intent(CheckActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckActivity.this,BackupActivity.class);
                startActivity(intent);
            }
        });


//        Intent intent = getIntent();
//        String id = intent.getStringExtra("id");//拿到设备二维码解析得到的id
//        eq_number.setText(id);
        DATA data = (DATA) getApplication();
        String id  = data.getEquipmentid();
        OkHttpClient client = new OkHttpClient();//创建服务器对象
        //创建一个Request
        FormBody.Builder FbBuilder = new FormBody.Builder();
        FbBuilder.add("id",id);
        FormBody formBody = FbBuilder.build();
        //String url = "http://10.242.198.87:8080/EquipmentController/GetEquipmentInfo";
        String url = "http://10.242.146.175:8080/EquipmentController/GetEquipmentInfo";
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        System.out.println(request);
        System.out.println(formBody.toString());
        //通过client发起请求
        client.newCall(request).enqueue(new Callback() {
            //无response体
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("----------error------------");
                e.printStackTrace();
                Bundle B = new Bundle();
                B.putString("result1","error");
                Message message = new Message();
                message.setData(B);
                handler.sendMessage(message);
            }
            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //拿到返回的字符串 字符串为json格式
                    String result = response.body().string();
                    Bundle B = new Bundle();
                    if(result.isEmpty())
                    {
                        B.putString("result1", "null");
                    }
                    else {
                        System.out.println(result);
                        //创建解析工具类
                        Gson gson = new Gson();
                        Map<String, Object> map = new HashMap<String, Object>();
                        System.out.println(result);
                        map = gson.fromJson(result, map.getClass());
                        //将拿到的设备信息存在bundle中并通过messgae发送给handle，在handle中处理相应的逻辑
                        String equipment_name = map.get("equipment_name").toString();
                        String equipment_id = map.get("id").toString();
                        Double equ_id_double = Double.parseDouble(equipment_id);
                        Integer equ_id_integer = new Integer(equ_id_double.intValue());
                        B.putString("equipment_id", equ_id_integer.toString());
                        B.putString("equipment_name", equipment_name);
                        String voltage_min = map.get("voltage_min").toString();
                        Double voltage_min_double = Double.parseDouble(voltage_min);
                        Integer voltage_min_integer = new Integer(voltage_min_double.intValue());
                        B.putString("voltage_min", voltage_min_integer.toString());
                        String voltage_max = map.get("voltage_max").toString();
                        Double voltage_max_double = Double.parseDouble(voltage_max);
                        Integer voltage_max_integer = new Integer(voltage_max_double.intValue());
                        B.putString("voltage_max", voltage_max_integer.toString());
                        B.putString("result1", "successful");
                    }
                    Message message = new Message();
                    message.setData(B);
                    handler.sendMessage(message);
                }
            }
        });



        Log.d("--------------", "onCreate: ");
        progress_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(timer == null)
                    timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what=0;
                        handler.sendMessage(message);
                    }
                }, 0, 10);


                if(radioButtonA1.isChecked())
                    Equipment_level = "1";
                else if(radioButtonB1.isChecked())
                    Equipment_level = "2";
                else if(radioButtonC1.isChecked())
                    Equipment_level = "3";
                else if(radioButtonD1.isChecked())
                    Equipment_level = "4";

                Equipment_name = eq_name.getText().toString();
                Voltage_min = v_min.getText().toString();
                Voltage_max = v_max.getText().toString();

                if(State1Y.isChecked())
                    Equipment_situation = "1";
                else if(State1N.isChecked())
                    Equipment_situation = "0";

                if(State2Y.isChecked())
                    Instrument_situation = "1";
                else if(State2N.isChecked())
                    Instrument_situation = "0";

                if(State3Y.isChecked())
                    Circuit_situation = "1";
                else if(State3N.isChecked())
                    Circuit_situation = "0";

                if(State4Y.isChecked())
                    Generatrix_situation = "1";
                else if(State4N.isChecked())
                    Generatrix_situation = "0";
                //创建一个Request
                FormBody.Builder FbBuilder1 = new FormBody.Builder();
                FbBuilder1.add("id",eq_number.getText().toString());
                FbBuilder1.add("Equipment_level",Equipment_level);
                FbBuilder1.add("Equipment_name",Equipment_name);
                FbBuilder1.add("Voltage_min",Voltage_min);
                FbBuilder1.add("Voltage_max",Voltage_max);
                FbBuilder1.add("Equipment_situation",Equipment_situation);
                FbBuilder1.add("Instrument_situation",Instrument_situation);
                FbBuilder1.add("Circuit_situation",Circuit_situation);
                FbBuilder1.add("Generatrix_situation",Generatrix_situation);
                OkHttpClient client = new OkHttpClient();
                FormBody formBody = FbBuilder1.build();
                //String url = "http://10.242.198.87:8080/EquipmentController/UpdateEquipment";
                String url = "http://10.242.146.175:8080/EquipmentController/UpdateEquipment";
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
                        }
                    }
                });
                Message message = new Message();
                message.what=0;
                handler.sendMessage(message);
    }});
    }

    private void setProgress() {
        progress = progress + 1;
        System.out.println(progress);
        progress_button.setProgress(progress);
        if (progress == 100) {
            show_message("提交成功");
            timer.cancel();
            timer = null;
            progress = 1;
            progress_button.setProgress(0);
        }
    }

    private void show_message(String info)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckActivity.this);
        builder1.setMessage("提示");
        builder1.setNegativeButton(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder1.create().show();
    }
}

//
//    private void showDialog(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(CheckActivity.this);
//        if(!(radioButtonA1.isChecked()||radioButtonB1.isChecked()||radioButtonC1.isChecked()||radioButtonD1.isChecked())){
//            builder.setTitle("您好");
//            builder.setMessage("请选择巡检等级！");
//            builder.setNegativeButton("好的", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }
//            });
//        }
//
//        var Enumber:String=eq_number.text.toString();
//        var Ename:String=eq_name.text.toString();
//        var Elevel:String=ele_level.text.toString();
//        if(TextUtils.isEmpty(Enumber)||TextUtils.isEmpty(Ename)||TextUtils.isEmpty(Elevel)){
//            builder.setTitle("您好")
//            builder.setMessage("请输入设备编号，设备名称或电压等级！")
//            builder.setNegativeButton("好的") { dialog, which -> }
//            var alert = builder.create()
//            alert.show()
//        }
//        if(!(state1_Y.isChecked||state1_N.isChecked)||!(state2_Y.isChecked||state2_N.isChecked)
//                ||!(state3_Y.isChecked||state3_N.isChecked)||!(state4_Y.isChecked||state4_N.isChecked)
//                ||!(state5_Y.isChecked||state5_N.isChecked)||!(state6_Y.isChecked||state6_N.isChecked)
//                ||!(state7_Y.isChecked||state7_N.isChecked)||!(state8_Y.isChecked||state8_N.isChecked)
//                ||!(state9_Y.isChecked||state9_N.isChecked)){
//            builder.setTitle("您好")
//            builder.setMessage("有些巡检项目未填写，请检查！")
//            builder.setNegativeButton("好的") { dialog, which -> }
//            var alert = builder.create()
//            alert.show()
//
//        }
//        else {
//            builder.setTitle("您好")
//            builder.setMessage("还需要修改吗")
//            builder.setPositiveButton("不需要") { dialog, which ->
//                if (state9_Y.isChecked) {
//                    val intent = Intent(this, UploadActivity::class.java)
//                    startActivity(intent);
//                } else {
//                }
//            }
//            builder.setNegativeButton("需要") { dialog, which -> }
//            var alert = builder.create()
//            alert.show()
//        }
//
//    }