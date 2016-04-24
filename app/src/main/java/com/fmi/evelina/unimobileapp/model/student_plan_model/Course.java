package com.fmi.evelina.unimobileapp.model.student_plan_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {
    @Expose
    @SerializedName("name")
    public String Name;

    @Expose
    @SerializedName("hours")
    public String Hours;

    @Expose
    @SerializedName("credits")
    public double Credits;
}
