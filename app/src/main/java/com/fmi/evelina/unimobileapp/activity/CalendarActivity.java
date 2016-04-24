package com.fmi.evelina.unimobileapp.activity;

import android.app.DialogFragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.fragment.ViewCalendarEvent;
import com.fmi.evelina.unimobileapp.model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.RecurringCalendarEvent;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.DataAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends DrawerBaseActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener,
        WeekView.EventLongPressListener,
        WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    private List<WeekViewEvent> weekViewEvents = new ArrayList<WeekViewEvent>();
    private List<RecurringCalendarEvent> recurringCalendarEvents = new ArrayList<RecurringCalendarEvent>();
    private List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();

    boolean calledNetwork = false;
    boolean showRecurringEvents;
    boolean showEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDrawerContentView(R.layout.content_calendar);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar, menu);

        showEvents = (menu.findItem(R.id.cbShowEvents)).isChecked();
        showRecurringEvents = (menu.findItem(R.id.cbShowScheduledEvents)).isChecked();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id) {
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.cbShowScheduledEvents:
                if (item.isChecked()) {
                    hideScheduledEvents();
                    item.setChecked(false);
                } else {
                    showSheduledEvents();
                    item.setChecked(true);
                }
                return true;
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

    private void showEvents() {
        for (CalendarEvent event : calendarEvents) {
            weekViewEvents.add(event.getWeekViewEvent());
        }

        getWeekView().notifyDatasetChanged();
        showEvents = true;
    }

    private void hideEvents() {
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

    private void showSheduledEvents() {
        for (RecurringCalendarEvent event : recurringCalendarEvents) {
            weekViewEvents.addAll(event.getWeekViewEvents());
        }

        getWeekView().notifyDatasetChanged();
        showRecurringEvents = true;
    }

    private void hideScheduledEvents() {
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
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        viewEventDetails(event);

        //Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Download events from network if it hasn't been done already.
        if (!calledNetwork) {

            weekViewEvents.clear();
            recurringCalendarEvents.clear();
            calendarEvents.clear();

            DataAPI.getStudentSchedule(new GetStudentSchedulerCallback());
            DataAPI.getEvents(new GetEventsCallback());
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

//    public void onSuccess(List<RecurringCalendarEvent> events) {
//
//        this.weekViewEvents.clear();
//        this.recurringCalendarEvents.clear();
//        int id = 0;
//
//        for (RecurringCalendarEvent event : events) {
//            event.Id = id;
//            id++;
//
//            this.recurringCalendarEvents.add(event);
//            this.weekViewEvents.addAll(event.getWeekViewEvents());
//        }
//        getWeekView().notifyDatasetChanged();
//    }
//
//    //RetrofitError error
//    public void onFail(String error) {
//        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
//    }

    private void viewEventDetails(WeekViewEvent event) {
        // Create and show the dialog.

        if (event.getId() > 0) {
            RecurringCalendarEvent ev = null;
            for (RecurringCalendarEvent e : recurringCalendarEvents) {
                if (e.Id == event.getId()) {
                    ev = e;
                    break;
                }
            }

            if (ev != null) {

                DialogFragment newFragment = ViewCalendarEvent.newInstance(ev);
                newFragment.show(getFragmentManager(), "dialog");
                return;
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
                DialogFragment newFragment = ViewCalendarEvent.newInstance(cev);
                newFragment.show(getFragmentManager(), "dialog");
                return;
            }
        }

        Toast.makeText(this, "Unable to find event", Toast.LENGTH_LONG).show();

    }

    private class GetStudentSchedulerCallback implements CallBack<List<RecurringCalendarEvent>> {

        @Override
        public void onSuccess(List<RecurringCalendarEvent> events) {

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
            Toast.makeText(CalendarActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetEventsCallback implements CallBack<List<CalendarEvent>> {

        @Override
        public void onSuccess(List<CalendarEvent> events) {

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
            Toast.makeText(CalendarActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
