package com.fmi.evelina.unimobileapp.activity.ElectionCampaign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.network.ICallBack;

public class ElectionCourseActivity extends DrawerBaseActivity implements ICallBack<ElectionCourse> {

    public static final String COURSE_KEY = "course";
    public static final String CAMPAIGN_KEY = "campaign";
    public static final String COURSE_ID_KEY = "courseId";
    public static final int COURSE_ENROLLED = 1;
    public static final int COURSE_CANCELED = 2;

    private static ElectionCampaign currentCampaign;
    private static ElectionCourse currentCourse;

    private static Button enrollButton;
    private static Button cancelButton;
    private static Button backButton;

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
        backButton = (Button) this.findViewById(R.id.election_course_btn_back);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationController.getDataProvider().enrollCourse(currentCourse.Id,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                //Show conformation information
                                Toast.makeText(ElectionCourseActivity.this, R.string.election_course_enrolled, Toast.LENGTH_LONG).show();

                                currentCourse.Enrolled = true;
                                UpdateEnrollButton();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ApplicationController.showErrorToast();
                            }
                        });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show confirmation
                new AlertDialog.Builder(ElectionCourseActivity.this)
                        .setTitle(R.string.confirmation_label)
                        .setMessage(R.string.election_course_cancel_confirm)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.confirmation_yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ApplicationController.getDataProvider().cancelCourse(currentCourse.Id,
                                        new Response.Listener() {
                                            @Override
                                            public void onResponse(Object response) {
                                                currentCourse.Enrolled = false;
                                                currentCourse.HasFreeSpaces = true;
                                                UpdateEnrollButton();

                                                //Show conformation information
                                                Toast.makeText(ElectionCourseActivity.this, R.string.election_course_cancelled, Toast.LENGTH_LONG).show();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                ApplicationController.showErrorToast();
                                            }
                                        });
                            }
                        }).setNegativeButton(R.string.confirmation_no, null).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
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
                goBack();
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

    private void goBack() {
        Bundle conData = new Bundle();
        conData.putInt(COURSE_ID_KEY, currentCourse.Id);

        Intent intent = new Intent();
        intent.putExtras(conData);

        setResult(currentCourse.Enrolled ? COURSE_ENROLLED : COURSE_CANCELED, intent);
        finish();
    }

    //Reset the title
    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.title_election_course));
    }
}
