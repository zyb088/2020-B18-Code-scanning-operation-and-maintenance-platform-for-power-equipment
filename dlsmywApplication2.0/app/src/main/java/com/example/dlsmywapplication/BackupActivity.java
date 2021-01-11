package com.example.dlsmywapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.DATA;
import com.example.qrcodetool.QrcodetoolActivity;
import com.example.qrcodetool.codetool;


//功能选择界面
public class BackupActivity extends AppCompatActivity {

    Button facility_upload,function,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_backup);
        facility_upload = findViewById(R.id.facility_upload);//设备二维码上传按钮
        function = findViewById(R.id.function);//功能选择按钮
        user = findViewById(R.id.user);//个人中心按钮

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackupActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });

        //扫码按钮点击事件
        facility_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开摄像机
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
//                DATA data = (DATA) getApplication();//获取全局变量data
//                String permission = data.getPermission();//获取角色权限
//                if (permission=="0")
//                    intent = new Intent(BackupActivity.this, InformationActivity.class);
//                else if(permission=="1")
//                    intent = new Intent(BackupActivity.this, CheckActivity.class);
//                DATA applicationdata = (DATA) getApplication();
//                applicationdata.setEquipmentid("101");
//                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
//            thumbnail = BitmapFactory.decodeResource(getResources(), thumbnail);  //调用测试图片test
            //text3.setText(codetool.syncDecodeQRCode(thumbnail));
            String id = codetool.syncDecodeQRCode(thumbnail);//解析得到的结果
            System.out.println("------------\n");
            System.out.println(id);

            if (id != null) {
                DATA applicationdata = (DATA) getApplication();
                applicationdata.setEquipmentid(id);
                String permission = applicationdata.getPermission();
                Intent intent = null;
                if (permission == "0")
                    intent = new Intent(BackupActivity.this, InformationActivity.class);
                else if (permission == "1")
                    intent = new Intent(BackupActivity.this, CheckActivity.class);
                startActivity(intent);
            }
        }
        }
}
