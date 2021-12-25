package com.rewaagasem.university_project_android.model;

public class Food {


    private String id;
    private String name;
    private String category;
    private String calory;

    private String uid;
    private String image;


    public String getKey_name() {
        return image;
    }

    public void setKey_name(String key_name) {
        this.image = key_name;
    }

//    public byte [] getImage() {
//        return image;
//    }
//
//    public void setImage(byte [] image) {
//        this.image = image;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCalory() {
        return calory;
    }

    public void setCalory(String calory) {
        this.calory = calory;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
