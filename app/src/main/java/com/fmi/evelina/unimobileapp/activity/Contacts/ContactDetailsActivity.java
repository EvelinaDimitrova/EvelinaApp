package com.fmi.evelina.unimobileapp.activity.Contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.model.contacts_model.ContactData;

/*
    This is the code-behind for the Contact Detail Activity.
    It can display both Administration and Lecturer contacts.
    The difference between the two is the job row in the Administration contact.
 */
public class ContactDetailsActivity extends DrawerBaseActivity {

    //Keys for the Activity Intent Bundle.
    public static final String CONTACT_NAME_KEY = "contactName";
    public static final String CONTACT_JOB_KEY = "contactJob";
    public static final String CONTACT_DATA_KEY = "contactData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the activity content
        setDrawerContentView(R.layout.content_contacts_details);

        //Get the passed input data for the activity
        Bundle b = getIntent().getExtras();
        String contactName = b.getString(CONTACT_NAME_KEY);
        String contactJob = b.getString(CONTACT_JOB_KEY);
        final ContactData contactData = b.getParcelable(CONTACT_DATA_KEY);

        //Populate the IU fields with the passed contact information
        TextView nameTxt = (TextView) findViewById(R.id.contact_details_name);
        nameTxt.setText(contactName);

        //Show the contact job row only if such information is passed
        if (contactJob != null) {
            //Show the row
            TableRow jobRow = (TableRow) findViewById(R.id.contact_derails_job_row);
            jobRow.setVisibility(View.VISIBLE);

            //Set the data
            TextView jobTxt = (TextView) findViewById(R.id.contact_details_job);
            jobTxt.setText(contactJob);
        }

        TextView roomTxt = (TextView) findViewById(R.id.contact_details_room);
        roomTxt.setText(contactData.Room);

        TextView timeTxt = (TextView) findViewById(R.id.contact_details_hours);
        timeTxt.setText(contactData.Time);

        TextView phoneTxt = (TextView) findViewById(R.id.contact_details_phone);
        phoneTxt.setText(contactData.Phone);

        TextView emailTxt = (TextView) findViewById(R.id.contact_details_email);
        emailTxt.setText(contactData.Email);

        //Setup the Dial button handler
        ImageButton dialBtn = (ImageButton) findViewById(R.id.contact_details_dial_button);
        dialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Construct a predefined Dial intent
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                //Set the intent data (the contact telephone)
                callIntent.setData(Uri.parse("tel:" + contactData.Phone));
                startActivity(callIntent);
                return;
            }
        });

        //Setup the MailTo button handler
        ImageButton sendMailBtn = (ImageButton) findViewById(R.id.contact_details_email_button);
        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Construct a predefined Send To intent
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //Set the intent data (the contact email)
                String[] to = new String[]{contactData.Email};
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        //Set the back button handler
        Button backButton = (Button) findViewById(R.id.contact_details_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //End the activity and go back
                finish();
            }
        });

    }

    @Override
    //Add the Back menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    //HAndle the back menu item click
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_back:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Reset the title
    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.title_contact_detail));
    }
}
