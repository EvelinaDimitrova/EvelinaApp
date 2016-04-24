package com.fmi.evelina.unimobileapp.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.RecurringCalendarEvent;

/**
 *
 */
public class ViewCalendarEvent extends DialogFragment {

    String eventName;
    String eventDesc;
    String eventLecturer;
    String eventLocation;
    Boolean eventRecurring;
    Boolean eventElective;
    String eventType;
    String eventStartTime;
    String eventEndTime;

    private static String EVENT_NAME_KEY = "name";
    private static String EVENT_DESC_KEY = "description";
    private static String EVENT_LECTURER_KEY = "lecturer";
    private static String EVENT_LOCATION_KEY = "location";
    private static String EVENT_ELECTIVE_KEY = "elective";
    private static String EVENT_RECURRING_KEY = "recurring";
    private static String EVENT_TYPE_KEY = "type";
    private static String EVENT_START_TIME_KEY = "start_time";
    private static String EVENT_END_TIME_KEY = "end_time";

    /**
     * Create a new instance of ViewCalendarEvent, providing CalendarEvent
     * as an argument.
     */
    public static ViewCalendarEvent newInstance(RecurringCalendarEvent event) {
        ViewCalendarEvent f = new ViewCalendarEvent();

        Bundle args = new Bundle();
        args.putString(EVENT_NAME_KEY, event.Name);
        args.putString(EVENT_DESC_KEY, event.Description);
        args.putString(EVENT_LECTURER_KEY, event.Lecturer);
        args.putString(EVENT_LOCATION_KEY, event.Location);
        args.putBoolean(EVENT_ELECTIVE_KEY, event.IsElective == 'Y');
        args.putString(EVENT_TYPE_KEY, event.TypeName);
        args.putString(EVENT_START_TIME_KEY, event.StartTime.toString());
        args.putString(EVENT_END_TIME_KEY, event.EndTime.toString());

        args.putBoolean(EVENT_RECURRING_KEY, true);

        f.setArguments(args);
        return f;
    }

    public static ViewCalendarEvent newInstance(CalendarEvent event) {
        ViewCalendarEvent f = new ViewCalendarEvent();


        Bundle args = new Bundle();
        args.putString(EVENT_NAME_KEY, event.Name);
        args.putString(EVENT_DESC_KEY, event.Description);
        args.putString(EVENT_LOCATION_KEY, event.Location);
        args.putString(EVENT_START_TIME_KEY, event.StartTime.toString());
        args.putString(EVENT_END_TIME_KEY, event.EndTime.toString());

        args.putBoolean(EVENT_RECURRING_KEY, false);

        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventName = getArguments().getString(EVENT_NAME_KEY);
        eventDesc = getArguments().getString(EVENT_DESC_KEY);
        eventLecturer = getArguments().getString(EVENT_LECTURER_KEY);
        eventLocation = getArguments().getString(EVENT_LOCATION_KEY);
        eventElective = getArguments().getBoolean(EVENT_ELECTIVE_KEY);
        eventType = getArguments().getString(EVENT_TYPE_KEY);
        eventStartTime = getArguments().getString(EVENT_START_TIME_KEY);
        eventEndTime = getArguments().getString(EVENT_END_TIME_KEY);
        eventRecurring = getArguments().getBoolean(EVENT_RECURRING_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dilaogView = inflater.inflate(R.layout.view_event_dialog, container, false);


        ((TextView) dilaogView.findViewById(R.id.tvEventName)).setText(eventName);
        ((TextView) dilaogView.findViewById(R.id.tvEventLocation)).setText(eventLocation);
        ((TextView) dilaogView.findViewById(R.id.tvEventStartTime)).setText(eventStartTime);
        ((TextView) dilaogView.findViewById(R.id.tvEventEndTime)).setText(eventEndTime);

        if (eventRecurring) {
            dilaogView.findViewById(R.id.trEventElective).setVisibility(View.VISIBLE);
            ((TextView) dilaogView.findViewById(R.id.tvEventElective)).setText(eventElective ? "Elective" : "Mandatory");

            dilaogView.findViewById(R.id.trEventType).setVisibility(View.VISIBLE);
            ((TextView) dilaogView.findViewById(R.id.tvEventType)).setText(eventType);

            dilaogView.findViewById(R.id.trEventLecturer).setVisibility(View.VISIBLE);
            ((TextView) dilaogView.findViewById(R.id.tvEventLecturer)).setText(eventLecturer);

        } else {
            dilaogView.findViewById(R.id.trEventDescription).setVisibility(View.VISIBLE);
            ((TextView) dilaogView.findViewById(R.id.tvEventDescription)).setText(eventDesc);
        }

        return dilaogView;
    }
}