package com.example.entity;
//需要移植
public class menumsg {
    private String msg1;//名称
    private String msg2;//设备地点
    private String msg3;//时间
    private  String id;

    public menumsg(String msg1, String msg2, String msg3, String id) {
        this.msg1 = msg1;
        this.msg2 = msg2;
        this.msg3 = msg3;
        this.id = id;
    }


    public String getMsg1() {
        return msg1;
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public String getMsg2() {
        return msg2;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }

    public String getMsg3() {
        return msg3;
    }

    public void setMsg3(String msg3) {
        this.msg3 = msg3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
