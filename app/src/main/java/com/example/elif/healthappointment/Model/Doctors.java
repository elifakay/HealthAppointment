package com.example.elif.healthappointment.Model;


public class Doctors {

    private String name_surname;
    private String image;
    private String department;
    private String experience;
    private String city;
    private String about;
    private String gsm;

    public Doctors()
    {

    }
    public Doctors(String name_surname, String image, String department, String experience,String city,String about, String gsm) {
        this.name_surname = name_surname;
        this.image = image;
        this.department=department;
        this.experience=experience;
        this.city=city;
        this.about=about;
        this.gsm=gsm;
    }

    public String getName_surname() {
        return name_surname;
    }

    public void setName_surname(String name_surname) {
        this.name_surname = name_surname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getAbout() { return about; }

    public void setAbout(String about) { this.about = about; }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }
}
