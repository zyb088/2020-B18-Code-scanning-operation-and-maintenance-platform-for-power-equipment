package com.example.app;

import android.app.Application;

//该类继承Application类，可以在全局的任一地方获取
//用于存储角色信息，在用户选择界面处对角色进行初始化
public class DATA extends Application {
    private String permission;
    private String id;
    private String equipmentid;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public void onCreate() {
        permission = "0";//工人端
        id="0";//初始化id
        equipmentid="0";//初始化设备id
        username="username";
        super.onCreate();
    }
}
