package com.example.dlsmywapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.entity.menumsg;

import java.util.List;

//需要移植
public class UploadAdapter extends ArrayAdapter{
    int resourceID;
    public UploadAdapter(@NonNull Context context, int resource, @NonNull List<menumsg> msg) {
        super(context, resource, msg);
resourceID=resource;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView msg1,msg2,time;
       View view= LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
msg1=view.findViewById(R.id.tv_username);
msg2=view.findViewById(R.id.tv_message);
time=view.findViewById(R.id.tv_time);
menumsg msg= (menumsg) getItem(position);
if (msg!=null)
{
    msg1.setText(msg.getMsg1());
    msg2.setText(msg.getMsg2());
    time.setText(msg.getMsg3());
}
        return view;
    }


}
