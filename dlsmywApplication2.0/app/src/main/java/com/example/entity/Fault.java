package com.example.entity;

public class Fault {
    private int Id;
    private int Equipment_id;
    private int Equipmentmanager_id;
    private int Postman_id;
    private String Fault_description;
    private String Upload_time;

    public Fault() {
    }

    public Fault(int id, int equipment_id, int equipmentmanager_id, int postman_id, String fault_description, String upload_time) {
        Id = id;
        Equipment_id = equipment_id;
        Equipmentmanager_id = equipmentmanager_id;
        Postman_id = postman_id;
        Fault_description = fault_description;
        Upload_time = upload_time;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getEquipment_id() {
        return Equipment_id;
    }

    public void setEquipment_id(int equipment_id) {
        Equipment_id = equipment_id;
    }

    public int getEquipmentmanager_id() {
        return Equipmentmanager_id;
    }

    public void setEquipmentmanager_id(int equipmentmanager_id) {
        Equipmentmanager_id = equipmentmanager_id;
    }

    public int getPostman_id() {
        return Postman_id;
    }

    public void setPostman_id(int postman_id) {
        Postman_id = postman_id;
    }

    public String getFault_description() {
        return Fault_description;
    }

    public void setFault_description(String fault_description) {
        Fault_description = fault_description;
    }

    public String getUpload_time() {
        return Upload_time;
    }

    public void setUpload_time(String upload_time) {
        Upload_time = upload_time;
    }
}
