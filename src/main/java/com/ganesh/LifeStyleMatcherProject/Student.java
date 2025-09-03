package com.ganesh.LifeStyleMatcherProject;

import org.bson.Document;

public class Student {
    private int VID;
    private String Name;
    private String LPUEmail;

    public Student(Document doc) {
        this.VID = doc.getInteger("VID");
        this.Name = doc.getString("Name");
        this.LPUEmail = doc.getString("LPU Email id");
    }

    // Getters
    public int getVID() { return VID; }
    public String getName() { return Name; }
    public String getLPUEmail() { return LPUEmail; }
}
