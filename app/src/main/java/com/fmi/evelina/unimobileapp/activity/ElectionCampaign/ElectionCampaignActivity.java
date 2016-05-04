package com.fmi.evelina.unimobileapp.activity.ElectionCampaign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.helper.adapter.ElectionCoursesListAdapter;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.util.ArrayList;
import java.util.List;

public class ElectionCampaignActivity extends DrawerBaseActivity implements CallBack<ElectionCampaign> {

    private ElectionCampaign currentElectionCampaign;
    private List<ElectionCourse> allElectionCourseList = new ArrayList<>();
    private List<ElectionCourse> displayElectionCourseList = new ArrayList<>();
    private ElectionCoursesListAdapter coursesListAdapter;

    private Spinner studentPlanSpinner;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.content_election_campaign);

        coursesListAdapter = new ElectionCoursesListAdapter(ElectionCampaignActivity.this, displayElectionCourseList);
        ListView coursesListView = (ListView) findViewById(R.id.election_courses_listView);
        coursesListView.setAdapter(coursesListAdapter);

        studentPlanSpinner = (Spinner)findViewById(R.id.student_plan_spinner);
        categorySpinner = (Spinner)findViewById(R.id.course_category_spinner);

        studentPlanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDisplayData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDisplayData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("EVE_TRACE", "onItemClick");

                showElectionCourseView(displayElectionCourseList.get(position));
            }
        });

        NetworkAPI.getCurrentElectionCampaign(this);
    }

    private void showElectionCourseView(ElectionCourse course) {
        Intent intent = new Intent(ElectionCampaignActivity.this, ElectionCourseActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(ElectionCourseActivity.COURSE_KEY, course);
        b.putParcelable(ElectionCourseActivity.CAMPAIGN_KEY, currentElectionCampaign);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void populateStudentPlanFilter() {
        Spinner spinner = (Spinner) findViewById(R.id.student_plan_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.student_plan_filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateCourseCategoryFilter(){

        List<String> availableCategories = new ArrayList<>();
        ArrayAdapter<String> courseCategoryAdapter;

        for(ElectionCourse course : allElectionCourseList) {
            if (!availableCategories.contains(course.Category)) {
                availableCategories.add(course.Category);
            }
        }

        availableCategories.add(0,"-- All --");

        Spinner spinner = (Spinner) findViewById(R.id.course_category_spinner);
        courseCategoryAdapter = new ArrayAdapter<>(ElectionCampaignActivity.this,android.R.layout.simple_spinner_item, availableCategories);
        courseCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(courseCategoryAdapter);
    }

    @Override
    public void onSuccess(ElectionCampaign electionCampaign) {

        currentElectionCampaign = electionCampaign;
        View dataContainer = findViewById(R.id.election_campaign_data);
        dataContainer.setVisibility(View.VISIBLE);

        View errorMessage = findViewById(R.id.election_campaign_error);
        errorMessage.setVisibility(View.GONE);

        TextView startTime = (TextView) findViewById(R.id.election_campaign_start);
        startTime.setText(currentElectionCampaign.OpenDate.toString());

        TextView closeTime = (TextView) findViewById(R.id.election_campaign_close);
        closeTime.setText(currentElectionCampaign.CloseDate.toString());

        TextView endTime = (TextView) findViewById(R.id.election_campaign_end);
        endTime.setText(currentElectionCampaign.EndDate.toString());

        NetworkAPI.getElectionCampaignCourses(new CallBack<List<ElectionCourse>>() {
            @Override
            public void onSuccess(List<ElectionCourse> data) {
                allElectionCourseList.addAll(data);

                populateStudentPlanFilter();
                populateCourseCategoryFilter();

                updateDisplayData();
            }

            @Override
            public void onFail(String msg) {
                Log.v("EVE_TRACE_ERROR", msg);
            }
        });
    }

    @Override
    public void onFail(String msg) {
        TextView title = (TextView) findViewById(R.id.election_campaign_error);
        String msg2 = "ASD";
        title.setText(msg2);
        title.setVisibility(View.VISIBLE);

    }

    private void updateDisplayData() {
        displayElectionCourseList.clear();

        int selectedStudentPlanInd = studentPlanSpinner.getSelectedItemPosition();
        int selectedCategoryInd = categorySpinner.getSelectedItemPosition();
        String selectedCategoryVal = categorySpinner.getSelectedItem().toString();

        Log.v("EVE_TRACE", "selectedStudentPlanInd " + selectedStudentPlanInd);
        Log.v("EVE_TRACE", "selectedCategoryInd " + selectedCategoryInd);
        Log.v("EVE_TRACE", "selectedCategoryVal " + selectedCategoryVal);

        switch (selectedStudentPlanInd){
            //Only for my program and grade
            case 0: {
                //All categories
                if(selectedCategoryInd == 0) {
                    for(ElectionCourse c : allElectionCourseList) {
                        if (c.IsForMyProgram == true && c.IsForMyGrade == true) {
                            displayElectionCourseList.add(c);
                        }
                    }
                }
                //Specified category
                else {
                    for(ElectionCourse c : allElectionCourseList) {
                        if (c.Category.equals(selectedCategoryVal) && c.IsForMyProgram == true && c.IsForMyGrade == true) {
                            displayElectionCourseList.add(c);
                        }
                    }
                }
                break;
            }
            //Only for my program
            case 1: {
                //All categories
                if(selectedCategoryInd == 0) {
                    for(ElectionCourse c : allElectionCourseList) {
                        if (c.IsForMyProgram) {
                            displayElectionCourseList.add(c);
                        }
                    }
                }
                //Specified category
                else {
                    for(ElectionCourse c : allElectionCourseList) {
                        if (c.Category.equals(selectedCategoryVal) && c.IsForMyProgram  == true) {
                            displayElectionCourseList.add(c);
                        }
                    }
                }
                break;
            }
            //All programs and grades
            case 2: {
                //All categories
                if(selectedCategoryInd == 0) {
                     displayElectionCourseList.addAll(allElectionCourseList);
                }
                //Specified category
                else {
                    for(ElectionCourse c : allElectionCourseList) {
                        if (c.Category.equals(selectedCategoryVal)) {
                            displayElectionCourseList.add(c);
                        }
                    }
                }
                break;
            }
        }

        //displayElectionCourseList.addAll(allElectionCourseList);

        coursesListAdapter.notifyDataSetChanged();

    }
}
