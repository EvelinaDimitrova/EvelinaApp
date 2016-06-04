package com.fmi.evelina.unimobileapp.activity.Calendar;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.fragment.ViewCalendarEvent;
import com.fmi.evelina.unimobileapp.model.UserRole;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;
import com.fmi.evelina.unimobileapp.network.ICallBack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/*
    This is the code-behind for the Calendar Activity. It contains the WeekView control and the logic to populate it.
 */
public class CalendarActivity extends DrawerBaseActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, ICalendarActivity {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    //This is the collection that is displayed in the calendar
    private List<WeekViewEvent> weekViewEvents = new ArrayList<WeekViewEvent>();
    //This is the collection that holds the recurring events
    private List<RecurringCalendarEvent> recurringCalendarEvents = new ArrayList<RecurringCalendarEvent>();
    //This is the collection that holds one-time  events
    private List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();

    private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);

    boolean calledNetwork = false;
    boolean showRecurringEvents;
    boolean showEvents;
    private static UserRole userRole;

    //A key for opening the Create Events activity for results
    private static final int CREATE_EVENT_REQUEST = 1;  // The create event request code
    public static final int DIALOG_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDrawerContentView(R.layout.content_calendar);

        //Get the logged user role
        userRole = ApplicationController.getLoggedUser().Role;

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        //A call to set the correct state of the Add Event button
        updateAddEventButton();

        //Focus the calendar to 8 O'clock on load
        mWeekView.goToHour(8);
    }

    //Set the correct state of the Add Event button and set the onClick listener
    private void updateAddEventButton() {
        if (ApplicationController.isLoggedIn()) {
            //If the logged user is Administrator or Lecturer
            if (userRole.equals(UserRole.ADMN) || userRole.equals(UserRole.LECT)) {
                FloatingActionButton addEventButton = (FloatingActionButton) findViewById(R.id.add_calendar_event_button);
                //Set the button Visibility
                addEventButton.setVisibility(View.VISIBLE);

                //Set the button action
                addEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Open the Create Event activity
                        Intent createEvent = new Intent(CalendarActivity.this, CreateEventActivity.class);
                        startActivityForResult(createEvent, CREATE_EVENT_REQUEST);
                    }
                });
            }
        }
    }

    @Override
    //We expect to get here after opening the Create Event activity for result. This is the callback handler
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_EVENT_REQUEST) {
            if (resultCode == CreateEventActivity.EVENT_SAVED) {
                //Show an information toast
                Toast.makeText(CalendarActivity.this, R.string.calendar_event_saved, Toast.LENGTH_LONG).show();

                // The event was saved so update the display data
                ApplicationController.getDataProvider().getEvents(new GetEventsCallback());

                //Go to the event date/time
                try {
                    Bundle b = data.getExtras();
                    Calendar cal = Calendar.getInstance();

                    Date date = dateFormat.parse(b.getString(CreateEventActivity.EVENT_DATE_KEY));
                    int hour = b.getInt(CreateEventActivity.EVENT_HOUR_KEY);
                    cal.set(date.getYear(), date.getMonth(), date.getDay(), hour, 0);
                    //TODO fix dates
                    //mWeekView.goToDate(cal);
                    //mWeekView.goToHour(hour);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    //Add the Calendar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar, menu);

        showEvents = (menu.findItem(R.id.cbShowEvents)).isChecked();
        showRecurringEvents = (menu.findItem(R.id.cbShowScheduledEvents)).isChecked();

        if (userRole.equals(UserRole.ADMN)) {
            menu.setGroupVisible(R.id.calendar_menu_filter_group, false);
            showEvents = true;
        }

        return true;
    }

    @Override
    //Handle Calendar menu items selection
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id) {
            //Go to Today
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            //Show Day view
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));

                    //Focus to 8 O'clock
                    mWeekView.goToHour(8);
                }
                return true;
            //Show Three day view
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));

                    //Focus to 8 O'clock
                    mWeekView.goToHour(8);
                }
                return true;
            //Show Week view
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

                    //Focus to 8 O'clock
                    mWeekView.goToHour(8);
                }
                return true;
            //Change the state of the Show Recurring checkbox
            case R.id.cbShowScheduledEvents:
                if (item.isChecked()) {
                    hideScheduledEvents();
                    item.setChecked(false);
                } else {
                    showSheduledEvents();
                    item.setChecked(true);
                }
                return true;
            //Change the state of the Show One-time checkbox
            case R.id.cbShowEvents:
                if (item.isChecked()) {
                    hideEvents();
                    item.setChecked(false);
                } else {
                    showEvents();
                    item.setChecked(true);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Show One-time events in the calendar
    private void showEvents() {
        //Add all one-time event to the display list
        for (CalendarEvent event : calendarEvents) {
            weekViewEvents.add(event.getWeekViewEvent());
        }

        getWeekView().notifyDatasetChanged();
        showEvents = true;
    }

    //Hide all one-time events
    private void hideEvents() {
        //Remove all one-time events from the calendar
        Iterator<WeekViewEvent> i = weekViewEvents.iterator();
        while (i.hasNext()) {
            WeekViewEvent weekViewEvent = i.next();
            if (weekViewEvent.getId() < 0) {
                i.remove();
            }
        }

        getWeekView().notifyDatasetChanged();
        showEvents = false;
    }

    //Show recurring events
    private void showSheduledEvents() {
        //Add all recurring events to the display collection
        for (RecurringCalendarEvent event : recurringCalendarEvents) {
            weekViewEvents.addAll(event.getWeekViewEvents());
        }

        getWeekView().notifyDatasetChanged();
        showRecurringEvents = true;
    }

    //Hide all recurring event
    private void hideScheduledEvents() {
        //Remove the recurring events from the display collection
        Iterator<WeekViewEvent> i = weekViewEvents.iterator();
        while (i.hasNext()) {
            WeekViewEvent weekViewEvent = i.next();
            if (weekViewEvent.getId() > 0) {
                i.remove();
            }
        }

        getWeekView().notifyDatasetChanged();
        showRecurringEvents = false;
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    //Handler for the click on an event
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        viewEventDetails(event);

        //Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    //Main method for loading data
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Download events from network if it hasn't been done already.
        if (!calledNetwork) {

            //Clear all loaded events if there are such
            weekViewEvents.clear();
            recurringCalendarEvents.clear();
            calendarEvents.clear();

            //Call different procedure to ger recurring events depending on the user role
            if (userRole.equals(UserRole.STUD)) {
                ApplicationController.getDataProvider().getStudentSchedule(new GetStudentSchedulerCallback());
            }
            if (userRole.equals(UserRole.LECT)) {
                ApplicationController.getDataProvider().getLecturerSchedule(new GetLecturerSchedulerCallback());
            }

            //A call to get all one-time events
            ApplicationController.getDataProvider().getEvents(new GetEventsCallback());
            calledNetwork = true;

        }

        // Return only the events that matches newYear and newMonth.
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : weekViewEvents) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }

        return matchedEvents;
    }

    /**
     * Checks if an event falls into a specific year and month.
     *
     * @param event The event to check for.
     * @param year  The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    //The handler for the event click in the calendar
    private void viewEventDetails(WeekViewEvent event) {
        // recurring events have positive Id and ontime events - negative
        if (event.getId() > 0) {
            RecurringCalendarEvent ev = null;
            for (RecurringCalendarEvent e : recurringCalendarEvents) {
                if (e.Id == event.getId()) {
                    ev = e;
                    break;
                }
            }

            if (ev != null) {
                // Create and show the dialog.
                if (userRole.equals(UserRole.STUD)) {
                    RecurringStudentCalendarEvent se = (RecurringStudentCalendarEvent) ev;
                    if (se != null) {
                        DialogFragment newFragment = ViewCalendarEvent.newInstance(se);
                        newFragment.show(getFragmentManager(), "dialog");
                        return;
                    }
                } else if (userRole.equals(UserRole.LECT)) {
                    RecurringLecturerCalendarEvent le = (RecurringLecturerCalendarEvent) ev;
                    if (le != null) {
                        DialogFragment newFragment = ViewCalendarEvent.newInstance(le);
                        newFragment.show(getFragmentManager(), "dialog");
                        return;
                    }
                }


            }
        } else {
            CalendarEvent cev = null;
            for (CalendarEvent e : calendarEvents) {
                if (e.Id == event.getId()) {
                    cev = e;
                    break;
                }
            }

            if (cev != null) {
                // Create and show the dialog.
                DialogFragment newFragment = ViewCalendarEvent.newInstance(cev);
                newFragment.show(getFragmentManager(), "dialog");
                return;
            }
        }

        Toast.makeText(this, "Unable to find event", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onEventDeleted(long eventId) {
        //Show an information toast
        Toast.makeText(this, R.string.calendar_event_deleted, Toast.LENGTH_LONG).show();

        //Remove the event from the calendar
        Iterator<WeekViewEvent> i = weekViewEvents.iterator();
        while (i.hasNext()) {
            WeekViewEvent weekViewEvent = i.next();
            if (weekViewEvent.getId() == eventId) {
                i.remove();
                getWeekView().notifyDatasetChanged();
                break;
            }
        }

    }

    //A callback for the retrieval of the available One-time events
    private class GetEventsCallback implements ICallBack<List<CalendarEvent>> {

        @Override
        public void onSuccess(List<CalendarEvent> events) {

            //Clear the events currently displayed. All one-time events have negative IDs
            Iterator<WeekViewEvent> i = weekViewEvents.iterator();
            while (i.hasNext()) {
                WeekViewEvent weekViewEvent = i.next();
                if (weekViewEvent.getId() < 0) {
                    i.remove();
                    break;
                }
            }
            calendarEvents.clear();
            int id = -1;

            for (CalendarEvent event : events) {
                event.Id = id;
                id--;

                calendarEvents.add(event);
                if (showEvents) {
                    weekViewEvents.add(event.getWeekViewEvent());
                }
            }
            getWeekView().notifyDatasetChanged();
        }

        @Override
        public void onFail(String msg) {
            ApplicationController.showErrorToast();
        }
    }

    //A callback for the retrieval of the available recurring events for a student
    private class GetStudentSchedulerCallback implements ICallBack<List<RecurringStudentCalendarEvent>> {

        @Override
        public void onSuccess(List<RecurringStudentCalendarEvent> events) {

            int id = 1;

            for (RecurringCalendarEvent event : events) {
                event.Id = id;
                id++;

                recurringCalendarEvents.add(event);
                if (showRecurringEvents) {
                    weekViewEvents.addAll(event.getWeekViewEvents());
                }
            }

            getWeekView().notifyDatasetChanged();
        }

        @Override
        public void onFail(String msg) {
            ApplicationController.showErrorToast();
        }
    }

    //A callback for the retrieval of the available recurring events for a lecturer
    private class GetLecturerSchedulerCallback implements ICallBack<List<RecurringLecturerCalendarEvent>> {
        @Override
        public void onSuccess(List<RecurringLecturerCalendarEvent> events) {

            int id = 1;

            for (RecurringCalendarEvent event : events) {
                event.Id = id;
                id++;

                recurringCalendarEvents.add(event);
                if (showRecurringEvents) {
                    weekViewEvents.addAll(event.getWeekViewEvents());
                }
            }

            getWeekView().notifyDatasetChanged();
        }

        @Override
        public void onFail(String msg) {
            ApplicationController.showErrorToast();
        }
    }

    //Reset the title
    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.title_activity_calendar));
    }
}
