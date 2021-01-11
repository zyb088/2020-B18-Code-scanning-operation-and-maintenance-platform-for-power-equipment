package com.example.entity;

public class Employee {
    private String Username;
    private int Id;
    private int Employee_permissions;
    private String Phone_number;
    private String Login_password;

    public Employee() {
    }

    public Employee(String username, int id, int employee_permissions, String phone_number, String login_password) {
        Username = username;
        Id = id;
        Employee_permissions = employee_permissions;
        Phone_number = phone_number;
        Login_password = login_password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getEmployee_permissions() {
        return Employee_permissions;
    }

    public void setEmployee_permissions(int employee_permissions) {
        Employee_permissions = employee_permissions;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number) {
        Phone_number = phone_number;
    }

    public String getLogin_password() {
        return Login_password;
    }

    public void setLogin_password(String login_password) {
        Login_password = login_password;
    }
}
