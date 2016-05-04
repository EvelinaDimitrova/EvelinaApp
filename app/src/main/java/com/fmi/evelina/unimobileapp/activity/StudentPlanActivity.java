package com.fmi.evelina.unimobileapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.helper.adapter.CoursesListAdapter;
import com.fmi.evelina.unimobileapp.helper.adapter.GradesSpinnerAdapter;
import com.fmi.evelina.unimobileapp.helper.adapter.SemestersSpinnerAdapter;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Course;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Grade;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Semester;
import com.fmi.evelina.unimobileapp.model.student_plan_model.StudentPlan;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.util.ArrayList;
import java.util.List;

public class StudentPlanActivity extends DrawerBaseActivity implements CallBack<StudentPlan> {

    private GradesSpinnerAdapter gradesSpinnerAdapter;
    private SemestersSpinnerAdapter semestersSpinnerAdapter;
    private CoursesListAdapter coursesListAdapter;

    private Spinner gradesSpinner;
    private Spinner semesterSpinner;

    private List<Grade> grades = new ArrayList<>();
    private List<Semester> semesters = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();

    private StudentPlan studentPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.content_student_plan);

        gradesSpinnerAdapter = new GradesSpinnerAdapter(StudentPlanActivity.this,
                android.R.layout.simple_spinner_item,
                grades);
        gradesSpinner = (Spinner) findViewById(R.id.grades_spinner);
        gradesSpinner.setAdapter(gradesSpinnerAdapter);

        gradesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Grade selectedGrade = (Grade) parent.getItemAtPosition(position);
                semesters.clear();
                semesters.addAll(selectedGrade.Semesters);
                semestersSpinnerAdapter.notifyDataSetChanged();

                semesterSpinner.setSelection(0, true);
                // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
                semesterSpinner.getOnItemSelectedListener().onItemSelected(semesterSpinner, semesterSpinner.getSelectedView(), 0, semesterSpinner.getSelectedItemId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //semesters.clear();
            }
        });

        semestersSpinnerAdapter = new SemestersSpinnerAdapter(StudentPlanActivity.this,
                android.R.layout.simple_spinner_item,
                semesters);
        semesterSpinner = (Spinner) findViewById(R.id.semester_spinner);
        semesterSpinner.setAdapter(semestersSpinnerAdapter);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Semester selectedSemester = (Semester) parent.getItemAtPosition(position);
                courses.clear();
                courses.addAll(selectedSemester.Courses);
                coursesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        coursesListAdapter = new CoursesListAdapter(StudentPlanActivity.this, courses);
        ListView coursesListView = (ListView) findViewById(R.id.courses_listView);
        coursesListView.setAdapter(coursesListAdapter);

        NetworkAPI.getStudentPlan(this);
        //DataAPI.getStudentPlanTest(this);
    }

    @Override
    public void onSuccess(StudentPlan data) {

        Log.v("EVE", "data.Grades.size()=" + data.Grades.size());
        studentPlan = data;
        grades.clear();
        grades.addAll(data.Grades);
        gradesSpinnerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onFail(String msg) {
        Log.v("EVE_TRACE_ERROR", msg);
    }
}

