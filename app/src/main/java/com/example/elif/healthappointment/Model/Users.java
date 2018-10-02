package com.example.elif.healthappointment.Model;

public class Users {

    private String name;
    private String image;
    private String thumb_image;
    private String admin;

    public Users()
    {

    }
    public Users(String name, String image,String thumb_image,String admin) {
        this.name = name;
        this.image = image;
        this.thumb_image=thumb_image;
        this.admin=admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
