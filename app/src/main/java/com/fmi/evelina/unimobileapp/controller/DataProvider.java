package com.fmi.evelina.unimobileapp.controller;

import android.content.Context;

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
import com.fmi.evelina.unimobileapp.network.ICallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;
import com.fmi.evelina.unimobileapp.network.NetworkAPIMock;

import java.util.List;

/**
 *
 */
public class DataProvider {
    //private static NetworkAPI networkApi;
    private static NetworkAPI networkApi;
    private static DataBaseAPI dataBaseAPI;
    private static Context context;

    public DataProvider(Context con) {
        context = con;
        networkApi = new NetworkAPI();
        dataBaseAPI = new DataBaseAPI(context);
    }

    public static void signIn(final String userId, final String password, final ICallBack<User> onCallBack) {
        networkApi.signIn(userId, password, onCallBack);
    }

    public static void getStudentSchedule(final ICallBack<List<RecurringStudentCalendarEvent>> onCallBack) {
        if (ApplicationController.hasNetwork()) {
            networkApi.getStudentSchedule(new ICallBack<List<RecurringStudentCalendarEvent>>() {
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

    public static void getLecturerSchedule(final ICallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        if (ApplicationController.hasNetwork()) {
            networkApi.getLecturerSchedule(new ICallBack<List<RecurringLecturerCalendarEvent>>() {
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

    public static void getEvents(final ICallBack<List<CalendarEvent>> onCallBack) {
        if (ApplicationController.hasNetwork()) {
            networkApi.getEvents(new ICallBack<List<CalendarEvent>>() {
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

    public static void getNews(final Integer newsId, final int chunkSize, final ICallBack<List<News>> onCallBack) {
        networkApi.getNews(newsId, chunkSize, onCallBack);
    }

    public static void getNewsImage(final News news, final ICallBack<News> onCallBack) {
        networkApi.getNewsImage(news, onCallBack);
    }

    public static void getNewsImages(final List<News> newsList, final ICallBack<News> onCallBack) {
        networkApi.getNewsImages(newsList, onCallBack);
    }

    public static void getNewsDetails(final int newsId, final ICallBack<News> onCallBack) {
        networkApi.getNewsDetails(newsId, onCallBack);
    }

    public static void addNews(final News newsToSave, final Response.Listener onSuccess, final Response.ErrorListener onError) {
        networkApi.addNews(newsToSave, onSuccess, onError);
    }

    public static void deleteNews(final Integer newsId, final String newsImageName, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.deleteNews(newsId, newsImageName, onSuccess, onError);
    }

    public static void getStudentPlan(final ICallBack<StudentPlan> onCallBack) {
        if (ApplicationController.hasNetwork()) {
            networkApi.getStudentPlan(new ICallBack<StudentPlan>() {
                @Override
                public void onSuccess(StudentPlan data) {
                    //Copy the retrieved information in a local DB
                    dataBaseAPI.putStudentPlan(data);
                    //Pass the result back to the caller
                    onCallBack.onSuccess(data);
                }

                @Override
                public void onFail(String msg) {
                    onCallBack.onFail(msg);
                }
            });
        } else {
            dataBaseAPI.getStudentPlan(onCallBack);
        }
    }

    public static void getCurrentElectionCampaign(final ICallBack<ElectionCampaign> onCallBack) {
        networkApi.getCurrentElectionCampaign(onCallBack);
    }

    public static void getElectionCampaignCourses(final ICallBack<List<ElectionCourse>> onCallBack) {
        networkApi.getElectionCampaignCourses(onCallBack);
    }

    public static void enrollCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.enrollCourse(courseId, onSuccess, onError);
    }

    public static void cancelCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.cancelCourse(courseId, onSuccess, onError);
    }

    public static void getAdministrationContacts(final ICallBack<List<AdministrationCategoryContacts>> onCallBack) {
        if (ApplicationController.hasNetwork()) {
            networkApi.getAdministrationContacts(new ICallBack<List<AdministrationCategoryContacts>>() {
                @Override
                public void onSuccess(List<AdministrationCategoryContacts> data) {
                    //Copy the retrieved information in a local DB
                    dataBaseAPI.putAdministrationContacts(data);
                    //Pass the result back to the caller
                    onCallBack.onSuccess(data);
                }

                @Override
                public void onFail(String msg) {
                    onCallBack.onFail(msg);
                }
            });
        } else {
            dataBaseAPI.getAdministrationContacts(onCallBack);
        }
    }

    public static void getLecturersContacts(final ICallBack<List<LecturerContact>> onCallBack) {
        if (ApplicationController.hasNetwork()) {
            networkApi.getLecturersContacts(new ICallBack<List<LecturerContact>>() {
                @Override
                public void onSuccess(List<LecturerContact> data) {
                    //Copy the retrieved information in a local DB
                    dataBaseAPI.putLecturersContacts(data);
                    //Pass the result back to the caller
                    onCallBack.onSuccess(data);
                }

                @Override
                public void onFail(String msg) {
                    onCallBack.onFail(msg);
                }
            });
        } else {
            dataBaseAPI.getLecturersContacts(onCallBack);
        }
    }

    public static void getRooms(final ICallBack<List<Room>> onCallBack) {
        networkApi.getRooms(onCallBack);
    }

    public static void createEvent(final CalendarEvent event, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.createEvent(event, onSuccess, onError);
    }

    public static void deleteEvent(final Integer eventId, Response.Listener onSuccess, Response.ErrorListener onError) {
        networkApi.deleteEvent(eventId, onSuccess, onError);
    }


}
