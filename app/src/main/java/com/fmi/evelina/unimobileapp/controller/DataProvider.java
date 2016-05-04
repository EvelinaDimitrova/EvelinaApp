package com.fmi.evelina.unimobileapp.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Response;
import com.fmi.evelina.unimobileapp.localDB.DataBaseAPI;
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
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.util.List;

/**
 *
 */
public class DataProvider {
    private static NetworkAPI networkApi;
    private static DataBaseAPI dataBaseAPI;
    private static Context context;

    public DataProvider(Context con) {
        context = con;
        networkApi = new NetworkAPI();
        dataBaseAPI = new DataBaseAPI(context);
    }

    public static void signIn(final String userId, final String password, final CallBack<User> onCallBack) {
        networkApi.signIn(userId, password, onCallBack);
    }

    public static void getStudentSchedule(final CallBack<List<RecurringStudentCalendarEvent>> onCallBack) {
        if (hasNetwork()) {
            networkApi.getStudentSchedule(new CallBack<List<RecurringStudentCalendarEvent>>() {
                @Override
                public void onSuccess(List<RecurringStudentCalendarEvent> data) {
                    //Copy the retrieved information in a local DB
                    dataBaseAPI.putStudentSchedule(data);
                    //Pass the result back to the caller
                    onCallBack.onSuccess(data);
                }

                @Override
                public void onFail(String msg) {
                    onCallBack.onFail(msg);
                }
            });
        } else {
            dataBaseAPI.getStudentSchedule(onCallBack);
        }
    }

    public static void getLecturerSchedule(final CallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        if (hasNetwork()) {
            networkApi.getLecturerSchedule(new CallBack<List<RecurringLecturerCalendarEvent>>() {
                @Override
                public void onSuccess(List<RecurringLecturerCalendarEvent> data) {
                    //Copy the retrieved information in a local DB
                    dataBaseAPI.putLecturerSchedule(data);
                    //Pass the result back to the caller
                    onCallBack.onSuccess(data);
                }

                @Override
                public void onFail(String msg) {
                    onCallBack.onFail(msg);
                }
            });
        } else {
            dataBaseAPI.getLecturerSchedule(onCallBack);
        }
    }

    public static void getEvents(final CallBack<List<CalendarEvent>> onCallBack) {
        if (hasNetwork()) {
            networkApi.getEvents(new CallBack<List<CalendarEvent>>() {
                @Override
                public void onSuccess(List<CalendarEvent> data) {
                    //Copy the retrieved information in a local DB
                    dataBaseAPI.putEvents(data);
                    //Pass the result back to the caller
                    onCallBack.onSuccess(data);
                }

                @Override
                public void onFail(String msg) {
                    onCallBack.onFail(msg);
                }
            });
        } else {
            dataBaseAPI.getEvents(onCallBack);
        }
    }

    public static void getNews(final Integer newsId, final int chunkSize, final CallBack<List<News>> onCallBack) {
        networkApi.getNews(newsId, chunkSize, onCallBack);
    }

    public static void getNewsImage(final News news, final CallBack<News> onCallBack) {
        networkApi.getNewsImage(news, onCallBack);
    }

    public static void getNewsImages(final List<News> newsList, final CallBack<News> onCallBack) {
        networkApi.getNewsImages(newsList, onCallBack);
    }

    public static void getNewsDetails(final int newsId, final CallBack<News> onCallBack) {
        networkApi.getNewsDetails(newsId, onCallBack);
    }

    public static void getStudentPlan(final CallBack<StudentPlan> onCallBack) {
        networkApi.getStudentPlan(onCallBack);
    }

    public static void getCurrentElectionCampaign(final CallBack<ElectionCampaign> onCallBack) {
        networkApi.getCurrentElectionCampaign(onCallBack);
    }

    public static void getElectionCampaignCourses(final CallBack<List<ElectionCourse>> onCallBack) {
        networkApi.getElectionCampaignCourses(onCallBack);
    }

    public static void addNews(final News newsToSave, final Response.Listener onSuccess, final Response.ErrorListener onError) {
        networkApi.addNews(newsToSave, onSuccess, onError);
    }

    public static void enrollCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.enrollCourse(courseId, onSuccess, onError);
    }

    public static void cancelCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.cancelCourse(courseId, onSuccess, onError);
    }

    public static void getAdministrationContacts(final CallBack<List<AdministrationCategoryContacts>> onCallBack) {
        networkApi.getAdministrationContacts(onCallBack);
    }

    public static void getLecturersContacts(final CallBack<List<LecturerContact>> onCallBack) {
        networkApi.getLecturersContacts(onCallBack);
    }

    public static void getRooms(final CallBack<List<Room>> onCallBack) {
        networkApi.getRooms(onCallBack);
    }


    private static boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
