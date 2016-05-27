package com.fmi.evelina.unimobileapp.network;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.User;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.Room;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationCategoryContacts;
import com.fmi.evelina.unimobileapp.model.contacts_model.LecturerContact;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.model.student_plan_model.StudentPlan;
import com.google.gson.Gson;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The network API
 */
//TODO remove this mock class
public class NetworkAPITest {

    private static final String URLRoot = ApplicationController.getServerURLPref();
    private static final Gson gson = ApplicationController.getGson();


    public static void signInTest(final String userId, final String password, final CallBack<User> onCallBack) {

        onCallBack.onSuccess(new User(userId, password));
        return;
    }


    public static void getStudentScheduleTest(final CallBack<List<RecurringStudentCalendarEvent>> onCallBack) {
        List<RecurringStudentCalendarEvent> result = new ArrayList<>();
        RecurringStudentCalendarEvent re = new RecurringStudentCalendarEvent();

        LocalDate start = new LocalDate(2016, 1, 1);
        LocalDate end = new LocalDate(2016, 9, 9);
        re.StartDate = start.toDate();
        re.StartTime = new Time(12, 00, 00);

        re.EndDate = end.toDate();
        re.EndTime = new Time(13, 00, 00);

        re.Location = "101";
        re.Abbreviation = "AN";
        re.IsElective = 'N';
        re.Lecturer = "ASD";
        re.WeekDay = 3;
        re.TypeCode = "LECT";

        result.add(re);

        onCallBack.onSuccess(result);

    }


    public static void getLecturerScheduleTest(final CallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        List<RecurringLecturerCalendarEvent> result = new ArrayList<>();
        RecurringLecturerCalendarEvent re = new RecurringLecturerCalendarEvent();

        LocalDate start = new LocalDate(2016, 1, 1);
        LocalDate end = new LocalDate(2016, 9, 9);
        re.StartDate = start.toDate();
        re.StartTime = new Time(12, 00, 00);

        re.EndDate = end.toDate();
        re.EndTime = new Time(13, 00, 00);

        re.Location = "101";
        re.Abbreviation = "AN";
        re.Program = "SoftInj";
        re.WeekDay = 3;
        re.TypeCode = "LECT";

        result.add(re);

        onCallBack.onSuccess(result);

    }


    public static void getElectionCampaignCoursesTest(final CallBack<List<ElectionCourse>> onCallBack) {
        List<ElectionCourse> res = new ArrayList<>();

        ElectionCourse ec = new ElectionCourse();
        ec.Name = "Name";
        ec.Category = "ЯКН";
        ec.Description = "Desc1";
        ec.IsForMyProgram = true;
        ec.IsForMyGrade = true;
        ec.Credits = 5.0;
        res.add(ec);

        ElectionCourse ec0 = new ElectionCourse();
        ec0.Name = "Name0";
        ec0.Description = "Desc2";
        ec0.Category = "ЯКН0";
        ec0.IsForMyProgram = false;
        ec0.IsForMyGrade = false;
        ec0.Credits = 5.0;
        res.add(ec0);

        ElectionCourse ec2 = new ElectionCourse();
        ec2.Name = "Name2";
        ec2.Description = "Desc3";
        ec2.Category = "КН";
        ec2.Credits = 5.0;
        ec2.IsForMyProgram = true;
        ec2.IsForMyGrade = false;
        res.add(ec2);

        onCallBack.onSuccess(res);
    }


}
