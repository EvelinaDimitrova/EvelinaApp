package com.fmi.evelina.unimobileapp.helper.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Course;

import java.util.List;

public class CoursesListAdapter extends ArrayAdapter<Course> {
    private final Activity context;
    private final List<Course> courseList;

    public CoursesListAdapter(Activity context, List<Course> courseList) {
        super(context, R.layout.course_list_item, courseList);

        this.context=context;
        this.courseList=courseList;
    }

    public View getView(int position,View view,ViewGroup parent) {

        Course currentCourse = courseList.get(position);

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.course_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.course_list_item_title);
        TextView txtHours = (TextView) rowView.findViewById(R.id.course_list_item_hours);
        TextView thtCredits = (TextView) rowView.findViewById(R.id.course_list_item_credits);

        txtTitle.setText(currentCourse.Name);
        txtHours.setText(currentCourse.Hours);
        thtCredits.setText(Double.toString(currentCourse.Credits));

        return rowView;

    };
}
