package com.fmi.evelina.unimobileapp.activity.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.fmi.evelina.unimobileapp.helper.adapter.RoomsSpinnerAdapter;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.Room;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the content of the activity
        setDrawerContentView(R.layout.content_create_event);

        //Setup the date/time textviews to behave like date/time pickers
        setupDateTimePickers();

        //Setup the rooms spinner with the appropriate data collection and adapter
        roomsSpinnerAdapter = new RoomsSpinnerAdapter(CreateEventActivity.this,
                android.R.layout.simple_spinner_item,
                rooms);
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
                //Get the data from the user input
                eventToSave.Abbreviation = ((EditText) findViewById(R.id.create_event_abbr)).getText().toString();
                eventToSave.Name = ((EditText) findViewById(R.id.create_event_title)).getText().toString();
                eventToSave.Description = ((EditText) findViewById(R.id.create_event_text)).getText().toString();

                //Call the DataAPI to save the event
                NetworkAPI.createEvent(eventToSave,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {

                                //On successful save return the date/time information of the saved event
                                Bundle conData = new Bundle();
                                conData.putString(EVENT_DATE_KEY, dateFormat.format(eventToSave.EventDate));
                                conData.putInt(EVENT_HOUR_KEY, eventToSave.StartTime.getHours());
                                Intent intent = new Intent();
                                intent.putExtras(conData);
                                setResult(RESULT_OK, intent);
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

        //Call DataAPI to get all available rooms
        NetworkAPI.getRooms(new CallBack<List<Room>>() {
            @Override
            public void onSuccess(List<Room> data) {
                //Clear the rooms collection
                rooms.clear();
                //Update the rooms collection with the retrieved data
                rooms.addAll(data);

                //Add a predefined first item
                Room undefinedRoom = new Room();
                undefinedRoom.Location = "Undefined";
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
        EditText setDate = (EditText) findViewById(R.id.create_event_date);
        setDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                final Calendar mcurrentDate = Calendar.getInstance();
                final TextView textView = (TextView) v;
                int selectedYear = mcurrentDate.get(Calendar.YEAR);
                int selectedMonth = mcurrentDate.get(Calendar.MONTH);
                int selectedDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                //setup the Date picker
                DatePickerDialog mDatePicker = new DatePickerDialog(CreateEventActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        LocalDate ld = new LocalDate(selectedyear, selectedmonth, selectedday);
                        eventToSave.EventDate = ld.toDate();
                        textView.setText(dateFormat.format(eventToSave.EventDate));

                    }
                }, selectedYear, selectedMonth, selectedDay);
                mDatePicker.setTitle("Select date");

                //show the Date picker
                mDatePicker.show();
            }
        });

        //Setup the Event Start Time picker
        EditText setStartTime = (EditText) findViewById(R.id.create_event_start_time);
        setStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To show current date in the time picker
                final Calendar mcurrentDate = Calendar.getInstance();
                final TextView textView = (TextView) v;
                final int selectedHour = mcurrentDate.get(Calendar.HOUR);
                final int selectedMinute = mcurrentDate.get(Calendar.MINUTE);

                //Setup the Time picker
                TimePickerDialog mDatePicker = new TimePickerDialog(CreateEventActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        eventToSave.StartTime = new Time(hourOfDay, minute, 0);
                        textView.setText(timeFormat.format(eventToSave.StartTime));

                        return;
                    }
                }, selectedHour, selectedMinute, true);
                mDatePicker.setTitle("Select date");

                //Show the Time picker
                mDatePicker.show();
            }
        });

        //Setup the End Time picker
        EditText endStartTime = (EditText) findViewById(R.id.create_event_end_time);
        endStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To show current date in the time picker
                final Calendar mcurrentDate = Calendar.getInstance();
                final TextView textView = (TextView) v;
                final int selectedHour = mcurrentDate.get(Calendar.HOUR);
                final int selectedMinute = mcurrentDate.get(Calendar.MINUTE);

                //Setup the Time picker
                TimePickerDialog mDatePicker = new TimePickerDialog(CreateEventActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        eventToSave.EndTime = new Time(hourOfDay, minute, 0);
                        textView.setText(timeFormat.format(eventToSave.EndTime));
                    }
                }, selectedHour, selectedMinute, true);
                mDatePicker.setTitle("Select date");

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
}
