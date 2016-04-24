package com.fmi.evelina.unimobileapp.model.student_plan_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Grade {

    @Expose
    @SerializedName("id")
    public int ID;

    @Expose
    @SerializedName("semesters")
    public List<Semester> Semesters;

}
