package com.example.qrcodetool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.DATA;
import com.example.dlsmywapplication.CheckActivity;
import com.example.dlsmywapplication.InformationActivity;
import com.example.dlsmywapplication.R;
import com.google.zxing.WriterException;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;


public class QrcodetoolActivity extends AppCompatActivity {

    Button go,bu2,upload_qrcode;
    EditText code;
    ImageView image,image3;
    TextView text2,text3;
    Bitmap b=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodetool);
        go=(Button)findViewById(R.id.go);
        bu2=(Button)findViewById(R.id.bu2);
        code=(EditText)findViewById(R.id.code);
        image=(ImageView)findViewById(R.id.image);
        text2=(TextView)findViewById(R.id.text2);
        image3=(ImageView)findViewById(R.id.image3);
        text3=(TextView)findViewById(R.id.text3);
        upload_qrcode = findViewById(R.id.upload_qrcode);
        //隐藏系统默认的标题
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        ZXingLibrary.initDisplayOpinion(this);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap qrcode = null;
                String text=code.getText().toString();
                try {
                    qrcode=codetool.QRcode(text,500);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(qrcode);
                text2.setText(codetool.syncDecodeQRCode(qrcode));
            }
        });
        bu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        });
        upload_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String) text3.getText();
                if(id!=null)
                {
                    DATA data = (DATA) getApplication();
                    String permission = data.getPermission();
                    Intent intent = null;
                    if (permission=="0")
                        intent = new Intent(QrcodetoolActivity.this, InformationActivity.class);
                    else if(permission=="1")
                        intent = new Intent(QrcodetoolActivity.this, CheckActivity.class);
                    intent.putExtra("id", "12345");
                    startActivity(intent);
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            image3.setImageBitmap(thumbnail);
            thumbnail = BitmapFactory.decodeResource(getResources(), R.drawable.test12345);  //调用测试图片test
            text3.setText(codetool.syncDecodeQRCode(thumbnail));

        }
    }
}