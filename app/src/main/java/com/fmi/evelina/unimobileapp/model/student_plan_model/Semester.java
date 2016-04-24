package com.fmi.evelina.unimobileapp.model.student_plan_model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Semester {

    @Expose
    @SerializedName("id")
    public String ID;

    @Expose
    @SerializedName("courses")
    public List<Course> Courses;
}
