package com.example.entity;

public class Maintenance {
    private int Maintenance_level;
    private int Id;
    private int Equipment_id;
    private int Employee_id;
    private String Maintenance_time;

    public Maintenance() {
    }

    public Maintenance(int maintenance_level, int id, int equipment_id, int employee_id, String maintenance_time) {
        Maintenance_level = maintenance_level;
        Id = id;
        Equipment_id = equipment_id;
        Employee_id = employee_id;
        Maintenance_time = maintenance_time;
    }

    public int getMaintenance_level() {
        return Maintenance_level;
    }

    public void setMaintenance_level(int maintenance_level) {
        Maintenance_level = maintenance_level;
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

    public int getEmployee_id() {
        return Employee_id;
    }

    public void setEmployee_id(int employee_id) {
        Employee_id = employee_id;
    }

    public String getMaintenance_time() {
        return Maintenance_time;
    }

    public void setMaintenance_time(String maintenance_time) {
        Maintenance_time = maintenance_time;
    }
}
