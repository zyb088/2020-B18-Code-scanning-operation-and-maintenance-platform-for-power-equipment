package com.example.dlsmywapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Timer;
import java.util.TimerTask;

public class DialogDemoActivity extends AppCompatActivity {

    private ShapeLoadingDialog shapeLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_demo);
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                .loadText("加载中...")
                .build();


        //findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {

                shapeLoadingDialog.show();
            //}
        //});
        final Intent localIntent=new Intent(this,CheckActivity.class);//你要转向的Activity
        Timer timer=new Timer();
        TimerTask tast=new TimerTask() {
            @Override
            public void run(){
                startActivity(localIntent);//执行
            }
        };
        timer.schedule(tast,2000);//10秒后

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_dialog_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}