package com.example.volkov.taskviewpager.model;

import com.google.gson.annotations.SerializedName;

public class ListModel {
    private int id;

    /*Model item Car*/
    private String Brand;
    private String Model;
    @SerializedName("License_Plate")
    private String LicensePlate;
    private String Picture;

    /*Model item Devices*/
    private String Name;
    private String Specs;
    @SerializedName("Serial_Number")
    private String SerialNumber;

    /*Model item Staff*/
    private String Position;
    @SerializedName("E-mail")
    private String EMail;

    public String getTitle(){
        if (Brand != null && Model != null)
            return Brand + " " + Model;
        else if (Name != null && Specs != null)
            return Name + " " + Specs;
        else if (Name != null && Position != null)
            return Name + " " + Position;
        return "";
    }

    public String getSubtitle(){
        if (LicensePlate != null)
            return LicensePlate;
        else if (SerialNumber != null)
            return SerialNumber;
        else if (EMail != null)
            return EMail;
        return "";
    }

    public String getPicture() {
        return Picture;
    }

    public Integer getId(){
        return ++id;
    }

}

