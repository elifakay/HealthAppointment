package com.example.elif.healthappointment.Model;

public class ShowAppointments {

    private String date;
    private String time;
    private String complaint;
    private String doctor_name;
    private String department;

    public ShowAppointments()
    {

    }

    public ShowAppointments(String date, String time, String complaint, String doctor_name, String department) {
        this.date = date;
        this.time = time;
        this.complaint = complaint;
        this.doctor_name=doctor_name;
        this.department=department;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
