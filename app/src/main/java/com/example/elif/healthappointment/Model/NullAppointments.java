package com.example.elif.healthappointment.Model;

import java.util.ArrayList;

public class NullAppointments{

    private String date;
    public ArrayList<String> time=new ArrayList<>();

    public NullAppointments() {

    }

    public NullAppointments(String date) {
        super();
        this.date=date;
        //this.time = time;
    }
    /*public String getTime() {
        return time;
    }*/

    /*public void setTime(String time) {
        this.time = time;
    }*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
