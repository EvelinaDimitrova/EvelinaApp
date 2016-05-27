package com.fmi.evelina.unimobileapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.Calendar.ICalendarActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.UserRole;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;

/**
 *
 */
public class ViewCalendarEvent extends DialogFragment {

    Integer eventDbId;
    Integer eventUiId;
    String eventName;
    String eventDesc;
    String eventLecturer;
    String eventProgram;
    String eventLocation;
    String eventClass;
    Boolean eventElective;
    String eventType;
    String eventStartTime;
    String eventEndTime;

    private static final String EVENT_DB_ID_KEY = "dbKey";
    private static final String EVENT_UI_ID_KEY = "uiKey";
    private static final String EVENT_NAME_KEY = "name";
    private static final String EVENT_DESC_KEY = "description";
    private static final String EVENT_LECTURER_KEY = "lecturer";
    private static final String EVENT_LOCATION_KEY = "location";
    private static final String EVENT_ELECTIVE_KEY = "elective";
    private static final String EVENT_CLASS_KEY = "class";
    private static final String EVENT_TYPE_KEY = "type";
    private static final String EVENT_START_TIME_KEY = "start_time";
    private static final String EVENT_END_TIME_KEY = "end_time";
    private static final String EVENT_PROGRAM_KEY = "program";

    private static final String EVENT_CLASS_EVENT = "event";
    private static final String EVENT_CLASS_STUDENT_EVENT = "student_event";
    private static final String EVENT_CLASS_LECTURER_EVENT = "lecturer_event";

    /**
     * Create a new instance of ViewCalendarEvent, providing CalendarEvent
     * as an argument.
     */
    public static ViewCalendarEvent newInstance(RecurringStudentCalendarEvent event) {
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

        args.putString(EVENT_CLASS_KEY, EVENT_CLASS_STUDENT_EVENT);

        f.setArguments(args);
        return f;
    }

    public static ViewCalendarEvent newInstance(RecurringLecturerCalendarEvent event) {
        ViewCalendarEvent f = new ViewCalendarEvent();

        Bundle args = new Bundle();
        args.putString(EVENT_NAME_KEY, event.Name);
        args.putString(EVENT_DESC_KEY, event.Description);
        args.putString(EVENT_PROGRAM_KEY, event.Program);
        args.putString(EVENT_LOCATION_KEY, event.Location);
        args.putString(EVENT_TYPE_KEY, event.TypeName);
        args.putString(EVENT_START_TIME_KEY, event.StartTime.toString());
        args.putString(EVENT_END_TIME_KEY, event.EndTime.toString());

        args.putString(EVENT_CLASS_KEY, EVENT_CLASS_LECTURER_EVENT);

        f.setArguments(args);
        return f;
    }

    public static ViewCalendarEvent newInstance(CalendarEvent event) {
        ViewCalendarEvent f = new ViewCalendarEvent();


        Bundle args = new Bundle();
        args.putInt(EVENT_DB_ID_KEY, event.DbKey);
        args.putInt(EVENT_UI_ID_KEY, event.Id);
        args.putString(EVENT_NAME_KEY, event.Name);
        args.putString(EVENT_DESC_KEY, event.Description);
        args.putString(EVENT_LOCATION_KEY, event.Location);
        args.putString(EVENT_START_TIME_KEY, event.StartTime.toString());
        args.putString(EVENT_END_TIME_KEY, event.EndTime.toString());

        args.putString(EVENT_CLASS_KEY, EVENT_CLASS_EVENT);

        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventDbId = getArguments().getInt(EVENT_DB_ID_KEY);
        eventUiId = getArguments().getInt(EVENT_UI_ID_KEY);
        eventName = getArguments().getString(EVENT_NAME_KEY);
        eventDesc = getArguments().getString(EVENT_DESC_KEY);
        eventLecturer = getArguments().getString(EVENT_LECTURER_KEY);
        eventProgram = getArguments().getString(EVENT_PROGRAM_KEY);
        eventLocation = getArguments().getString(EVENT_LOCATION_KEY);
        eventElective = getArguments().getBoolean(EVENT_ELECTIVE_KEY);
        eventType = getArguments().getString(EVENT_TYPE_KEY);
        eventStartTime = getArguments().getString(EVENT_START_TIME_KEY);
        eventEndTime = getArguments().getString(EVENT_END_TIME_KEY);
        eventClass = getArguments().getString(EVENT_CLASS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dilaogView = inflater.inflate(R.layout.view_event_dialog, container, false);

        ((TextView) dilaogView.findViewById(R.id.tvEventName)).setText(eventName);
        ((TextView) dilaogView.findViewById(R.id.tvEventLocation)).setText(eventLocation);
        ((TextView) dilaogView.findViewById(R.id.tvEventStartTime)).setText(eventStartTime);
        ((TextView) dilaogView.findViewById(R.id.tvEventEndTime)).setText(eventEndTime);

        switch (eventClass) {
            case EVENT_CLASS_EVENT: {
                dilaogView.findViewById(R.id.trEventDescription).setVisibility(View.VISIBLE);
                ((TextView) dilaogView.findViewById(R.id.tvEventDescription)).setText(eventDesc);
                break;
            }
            case EVENT_CLASS_STUDENT_EVENT: {
                dilaogView.findViewById(R.id.trEventElective).setVisibility(View.VISIBLE);
                ((TextView) dilaogView.findViewById(R.id.tvEventElective)).setText(eventElective ? this.getActivity().getString(R.string.calendar_event_elective) : this.getActivity().getString(R.string.calendar_event_mandatory));

                dilaogView.findViewById(R.id.trEventType).setVisibility(View.VISIBLE);
                ((TextView) dilaogView.findViewById(R.id.tvEventType)).setText(eventType);

                dilaogView.findViewById(R.id.trEventLecturer).setVisibility(View.VISIBLE);
                ((TextView) dilaogView.findViewById(R.id.tvEventLecturer)).setText(eventLecturer);
                break;
            }
            case EVENT_CLASS_LECTURER_EVENT: {
                dilaogView.findViewById(R.id.trEventProgram).setVisibility(View.VISIBLE);
                ((TextView) dilaogView.findViewById(R.id.tvEventProgram)).setText(eventProgram);
                break;
            }
        }

        if (ApplicationController.isLoggedIn() && ApplicationController.getLoggedUser().Role.equals(UserRole.ADMN)) {
            Button deleteBtn = (Button) dilaogView.findViewById(R.id.btnEventDelete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Show delete confirmation
                    new AlertDialog.Builder(ViewCalendarEvent.this.getActivity())
                            .setTitle(getString(R.string.confirmation_label))
                            .setMessage(getString(R.string.confirm_delete))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(R.string.confirmation_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ApplicationController.getDataProvider().deleteEvent(eventDbId, new Response.Listener() {
                                                @Override
                                                public void onResponse(Object response) {
                                                    ICalendarActivity caller = (ICalendarActivity) getActivity();
                                                    if (caller != null){
                                                        caller.onEventDeleted(eventUiId);
                                                    }
                                                    dismiss();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //TODO show error
                                                }
                                            });
                                }
                            }).setNegativeButton(R.string.confirmation_no, null).show();


                }
            });
            deleteBtn.setVisibility(View.VISIBLE);
        }

        return dilaogView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.calendar_event_details);

        return dialog;
    }
}