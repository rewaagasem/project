package com.rewaagasem.university_project_android.model;

public class BMIRecord {

    private String id;
    private String length;
    private String weight;
    private String date;
    private String status;
    private String uid;
    private String bmi;
    private boolean is_last = false;


    public boolean isIs_last() {
        return is_last;
    }

    public void setIs_last(boolean is_last) {
        this.is_last = is_last;
    }

    public String getBMI() {
        return bmi;
    }

    public void setBMI(String BMI) {
        this.bmi = BMI;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String UID) {
        this.uid = UID;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
