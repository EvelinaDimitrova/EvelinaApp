package com.fmi.evelina.unimobileapp.activity.ElectionCampaign;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

public class ElectionCourseActivity extends DrawerBaseActivity implements CallBack<ElectionCourse> {

    public static final String COURSE_KEY = "course";
    public static final String CAMPAIGN_KEY = "campaign";

    private static ElectionCampaign currentCampaign;
    private static ElectionCourse currentCourse;

    private static Button enrollButton;
    private static Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        currentCourse = b.getParcelable(COURSE_KEY);
        currentCampaign = b.getParcelable(CAMPAIGN_KEY);

        setDrawerContentView(R.layout.content_election_course);

        TextView tv = (TextView) this.findViewById(R.id.election_course_name);
        tv.setText(currentCourse.Name);

        tv = (TextView) this.findViewById(R.id.election_course_description);
        tv.setText(currentCourse.Description);

        tv = (TextView) this.findViewById(R.id.election_course_category);
        tv.setText(currentCourse.Category);

        tv = (TextView) this.findViewById(R.id.election_course_credits);
        tv.setText(Double.toString(currentCourse.Credits));

        tv = (TextView) this.findViewById(R.id.election_course_hours);
        tv.setText(currentCourse.Hours);

        tv = (TextView) this.findViewById(R.id.election_course_lecturer);
        tv.setText(currentCourse.Lecturer);

        if (currentCourse.IsForMyProgram) {
            tv = (TextView) this.findViewById(R.id.election_course_min_grade);
            tv.setText(Integer.toString(currentCourse.MinGrade));
        }

        enrollButton = (Button) this.findViewById(R.id.election_course_btn_enroll);
        cancelButton = (Button) this.findViewById(R.id.election_course_btn_cancel);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkAPI.enrollCourse(currentCourse.Id,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                currentCourse.Enrolled = true;
                                UpdateEnrollButton();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ElectionCourseActivity.this, "Unable to perform operation", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkAPI.cancelCourse(currentCourse.Id,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                currentCourse.Enrolled = false;
                                currentCourse.HasFreeSpaces = true;
                                UpdateEnrollButton();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ElectionCourseActivity.this, "Unable to perform operation", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


        UpdateEnrollButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_back:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ElectionCourse course) {

    }

    private void UpdateEnrollButton() {
        if (currentCampaign.IsOpen()) {

            if (currentCourse.Enrolled) {
                enrollButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
            } else {
                cancelButton.setVisibility(View.GONE);

                //Show the enroll button
                enrollButton.setVisibility(View.VISIBLE);
                //Enable/Disable the enroll button depending on Free Spaces
                enrollButton.setEnabled(currentCourse.HasFreeSpaces);
            }

        } else if (currentCampaign.IsClosed()) {
            if (currentCourse.Enrolled) {
                enrollButton.setVisibility(View.GONE);

                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setEnabled(true);
            } else {
                cancelButton.setVisibility(View.GONE);
                enrollButton.setVisibility(View.GONE);
            }
        } else if (currentCampaign.IsEnded()) {
            cancelButton.setVisibility(View.GONE);
            enrollButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.GONE);
            enrollButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFail(String msg) {

    }
}
