package com.fmi.evelina.unimobileapp.activity.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.helper.adapter.RoomsSpinnerAdapter;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.Room;
import com.fmi.evelina.unimobileapp.network.CallBack;

import org.joda.time.LocalDate;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
    This is the code-behind for the Create Calendar Event Activity.
    It is expected to be started as an activity for result which returns the date and time of the inserted event.
 */
public class CreateEventActivity extends DrawerBaseActivity {

    private final CalendarEvent eventToSave = new CalendarEvent();
    private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);

    //A list which will be populated with all rooms
    private List<Room> rooms = new ArrayList<>();
    //This adapret for the Rooms spinner
    private RoomsSpinnerAdapter roomsSpinnerAdapter;

    //Public keys used for the activity result bundle
    public static final String EVENT_DATE_KEY = "eventDate";
    public static final String EVENT_HOUR_KEY = "eventHour";

    public static final int EVENT_SAVED = 1;

    private EditText abbrEditText;
    private EditText titleEditText;
    private EditText textEditText;
    private EditText dateEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the content of the activity
        setDrawerContentView(R.layout.content_create_event);

        abbrEditText = (EditText) findViewById(R.id.create_event_abbr);
        titleEditText = (EditText) findViewById(R.id.create_event_title);
        textEditText = (EditText) findViewById(R.id.create_event_text);
        dateEditText = (EditText) findViewById(R.id.create_event_date);
        startTimeEditText = (EditText) findViewById(R.id.create_event_start_time);
        endTimeEditText = (EditText) findViewById(R.id.create_event_end_time);

        //Setup the date/time textviews to behave like date/time pickers
        setupDateTimePickers();

        //Setup the rooms spinner with the appropriate data collection and adapter
        roomsSpinnerAdapter = new RoomsSpinnerAdapter(CreateEventActivity.this,
                android.R.layout.simple_dropdown_item_1line,
                rooms);
        roomsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        Spinner roomsSpinner = (Spinner) findViewById(R.id.create_event_location);
        roomsSpinner.setAdapter(roomsSpinnerAdapter);
        //Set the rooms spinner selection item listener
        roomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Set the roomId depending on the selected item in the spinner
                eventToSave.RoomId = rooms.get(position).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        //Setup the Save button handler
        Button buttonSave = (Button) findViewById(R.id.create_event_save_button);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Verify for empty mandatory fields
                boolean hasEmptyFields = false;
                if (abbrEditText.getText().toString().isEmpty()) {
                    abbrEditText.setError(getString(R.string.error_field_required));
                    hasEmptyFields = true;
                }
                if (titleEditText.getText().toString().isEmpty()) {
                    titleEditText.setError(getString(R.string.error_field_required));
                    hasEmptyFields = true;
                }
                if (dateEditText.getText().toString().isEmpty()) {
                    dateEditText.setError(getString(R.string.error_field_required));
                    hasEmptyFields = true;
                }
                if (startTimeEditText.getText().toString().isEmpty()) {
                    startTimeEditText.setError(getString(R.string.error_field_required));
                    hasEmptyFields = true;
                }
                if (endTimeEditText.getText().toString().isEmpty()) {
                    endTimeEditText.setError(getString(R.string.error_field_required));
                    hasEmptyFields = true;
                }
                if(eventToSave.StartTime != null && eventToSave.EndTime != null){
                    //validate time
                    if (eventToSave.EndTime.before(eventToSave.StartTime)){
                        endTimeEditText.setError(getString(R.string.calendar_end_time_error));
                        return;
                    }
                }
                if (hasEmptyFields) {
                    return;
                }

                //Get the data from the user input
                eventToSave.Abbreviation = abbrEditText.getText().toString();
                eventToSave.Name = titleEditText.getText().toString();
                eventToSave.Description = textEditText.getText().toString();

                //Call the DataAPI to save the event
                ApplicationController.getDataProvider().createEvent(eventToSave,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                                //On successful save return the date/time information of the saved event
                                Bundle conData = new Bundle();
                                conData.putString(EVENT_DATE_KEY, dateFormat.format(eventToSave.EventDate));
                                conData.putInt(EVENT_HOUR_KEY, eventToSave.StartTime.getHours());

                                Intent intent = new Intent();
                                intent.putExtras(conData);

                                setResult(EVENT_SAVED, intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CreateEventActivity.this, "Error: Unable to save the event!", Toast.LENGTH_LONG).show();
                                Log.e("EVE_TRACE_ERROR", error.toString());
                            }
                        });
            }
        });

        //Set Cancel button handler
        Button buttonCancel = (Button) findViewById(R.id.create_event_cancel_button);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show cancel confirmation
                new AlertDialog.Builder(CreateEventActivity.this)
                        .setTitle(R.string.confirmation_label)
                        .setMessage(R.string.confirm_cancel)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.confirmation_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Go back
                                finish();
                            }
                        }).setNegativeButton(R.string.confirmation_no, null).show();
            }
        });


        //Call DataAPI to get all available rooms
        ApplicationController.getDataProvider().getRooms(new CallBack<List<Room>>() {
            @Override
            public void onSuccess(List<Room> data) {
                //Clear the rooms collection
                rooms.clear();
                //Update the rooms collection with the retrieved data
                rooms.addAll(data);

                //Add a predefined first item
                Room undefinedRoom = new Room();
                undefinedRoom.Location = getString(R.string.calendar_room_undefined);
                rooms.add(0, undefinedRoom);

                //A call to update the UI
                roomsSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(CreateEventActivity.this, "Error: Unable to get available rooms!", Toast.LENGTH_LONG).show();
                Log.e("EVE_TRACE_ERROR", msg);
            }
        });
    }

    //Set the the date/time input field to behave like date/time pickers
    private void setupDateTimePickers() {
        //Setup the Event Date picker
        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                final TextView textView = (TextView) v;

                final Calendar initialDate = Calendar.getInstance();
                if (eventToSave.EventDate != null) {
                    initialDate.setTime(eventToSave.EventDate);
                }
                final int selectedYear = initialDate.get(Calendar.YEAR);
                final int selectedMonth = initialDate.get(Calendar.MONTH);
                final int selectedDay = initialDate.get(Calendar.DAY_OF_MONTH);

                //setup the Date picker
                DatePickerDialog mDatePicker = new DatePickerDialog(CreateEventActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(selectedyear, selectedmonth, selectedday);
                        eventToSave.EventDate = cal.getTime();
                        textView.setText(dateFormat.format(eventToSave.EventDate));
                        textView.setError(null);

                    }
                }, selectedYear, selectedMonth, selectedDay);
                mDatePicker.setTitle(getString(R.string.calendar_select_date));
                mDatePicker.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirmation_yes), mDatePicker);
                mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.confirmation_no), mDatePicker);


                //show the Date picker
                mDatePicker.show();
            }
        });

        //Setup the Event Start Time picker
        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To show current date in the time picker
                final Calendar initialTime = Calendar.getInstance();
                if (eventToSave.StartTime != null) {
                    initialTime.setTime(eventToSave.StartTime);
                }
                final TextView textView = (TextView) v;
                final int selectedHour = initialTime.get(Calendar.HOUR_OF_DAY);
                final int selectedMinute = initialTime.get(Calendar.MINUTE);

                //Setup the Time picker
                TimePickerDialog mDatePicker = new TimePickerDialog(CreateEventActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        eventToSave.StartTime = new Time(hourOfDay, minute, 0);
                        textView.setText(timeFormat.format(eventToSave.StartTime));
                        textView.setError(null);
                        return;
                    }
                }, selectedHour, selectedMinute, true);
                mDatePicker.setTitle(getString(R.string.calendar_select_date));
                mDatePicker.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirmation_yes), mDatePicker);
                mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.confirmation_no), mDatePicker);


                //Show the Time picker
                mDatePicker.show();
            }
        });

        //Setup the End Time picker
        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To show current date in the time picker
                final Calendar initialTime = Calendar.getInstance();
                if (eventToSave.EndTime != null) {
                    initialTime.setTime(eventToSave.EndTime);
                }
                final TextView textView = (TextView) v;
                final int selectedHour = initialTime.get(Calendar.HOUR_OF_DAY);
                final int selectedMinute = initialTime.get(Calendar.MINUTE);

                //Setup the Time picker
                TimePickerDialog mDatePicker = new TimePickerDialog(CreateEventActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        eventToSave.EndTime = new Time(hourOfDay, minute, 0);
                        textView.setText(timeFormat.format(eventToSave.EndTime));
                        textView.setError(null);
                    }
                }, selectedHour, selectedMinute, true);
                mDatePicker.setTitle(getString(R.string.calendar_select_time));
                mDatePicker.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirmation_yes), mDatePicker);
                mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.confirmation_no), mDatePicker);

                //Show the time picker
                mDatePicker.show();
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
    //Handle the menu item clicks
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
        this.setTitle(getString(R.string.title_activity_create_event));
    }
}
