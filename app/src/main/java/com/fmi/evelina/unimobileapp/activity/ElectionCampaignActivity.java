package com.fmi.evelina.unimobileapp.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.DataAPI;

public class ElectionCampaignActivity extends DrawerBaseActivity implements CallBack<ElectionCampaign> {

    private ElectionCampaign currentElectionCampaign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.content_election_campaign);

        populateStudentPlanFilter();

        DataAPI.getCurrentElectionCampaign(this);
    }

    private void populateStudentPlanFilter() {
        Spinner spinner = (Spinner) findViewById(R.id.student_plan_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.student_plan_filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

        TextView endTime = (TextView) findViewById(R.id.election_campaign_start);
        endTime.setText(currentElectionCampaign.EndDate.toString());
    }

    @Override
    public void onFail(String msg) {
        TextView title = (TextView) findViewById(R.id.election_campaign_error);
        String msg2 = "ASD";
        title.setText(msg2);
        title.setVisibility(View.VISIBLE);

    }
}
