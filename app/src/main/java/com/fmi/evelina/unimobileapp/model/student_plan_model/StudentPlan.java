package com.fmi.evelina.unimobileapp.model.student_plan_model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


public class StudentPlan {

    @Expose
    @SerializedName("grades")
    public List<Grade> Grades;
}


