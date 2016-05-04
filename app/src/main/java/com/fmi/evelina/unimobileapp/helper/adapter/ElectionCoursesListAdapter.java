package com.fmi.evelina.unimobileapp.helper.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Course;

import java.util.List;

public class ElectionCoursesListAdapter extends ArrayAdapter<ElectionCourse> {
    private final Activity context;
    private final List<ElectionCourse> courseList;

    public ElectionCoursesListAdapter(Activity context, List<ElectionCourse> courseList) {
        super(context, R.layout.election_course_list_item, courseList);

        this.context=context;
        this.courseList=courseList;
    }

    public View getView(int position,View view,ViewGroup parent) {

        ElectionCourse currentCourse = courseList.get(position);

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.election_course_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.election_course_list_item_title);
        TextView thtCategory = (TextView) rowView.findViewById(R.id.election_course_list_item_category);
        TextView thtCredits = (TextView) rowView.findViewById(R.id.election_course_list_item_credits);

        txtTitle.setText(currentCourse.Name);
        thtCategory.setText(currentCourse.Category);
        thtCredits.setText(Double.toString(currentCourse.Credits));

        return rowView;

    };
}
