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
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.Room;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationCategoryContacts;
import com.fmi.evelina.unimobileapp.model.contacts_model.LecturerContact;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.election_camaign_model.ElectionCourse;
import com.fmi.evelina.unimobileapp.model.student_plan_model.StudentPlan;
import com.fmi.evelina.unimobileapp.model.User;
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
public class NetworkAPI {

    private static final String URLRoot = ApplicationController.getServerURL();
    private static final Gson gson = ApplicationController.getGson();


    public static void signIn(final String userId, final String password, final CallBack<User> onCallBack) {

        String URL = URLRoot + "/signIn";

        final HashMap<String, String> authHeader = getAuthenticationHeader(userId, password);

        JsonRequest req = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObj = (JSONObject) response;

                try {
                    User user = gson.fromJson(jsonObj.toString(), User.class);
                    user.Password = password;

                    onCallBack.onSuccess(user);
                } catch (Exception ex) {
                    Log.e("FMI_APP_EXCEPTION", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FMI_APP_EXCEPTION", error.getMessage());
                onCallBack.onFail(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (authHeader != null && !authHeader.isEmpty()) {
                    return authHeader;
                }
                return super.getHeaders();
            }
        };


        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    //TODO remove this mock method
    public static void signInTest(final String userId, final String password, final CallBack<User> onCallBack) {

        onCallBack.onSuccess(new User(userId, password));
        return;
    }

    public static void getStudentSchedule(final CallBack<List<RecurringStudentCalendarEvent>> onCallBack) {
        User loggedUser = ApplicationController.getLoggedUser();
        if (loggedUser != null && loggedUser.Role.equals("STUD")) {
            String URL = URLRoot + "/schedule?role=" + loggedUser.Role;
            makeJsonArrayRequest(RecurringStudentCalendarEvent.class, URL, true, onCallBack);
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
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

    public static void getLecturerSchedule(final CallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        User loggedUser = ApplicationController.getLoggedUser();
        if (loggedUser != null && loggedUser.Role.equals("LECT")) {
            String URL = URLRoot + "/schedule?role=" + loggedUser.Role;
            makeJsonArrayRequest(RecurringLecturerCalendarEvent.class, URL, true, onCallBack);
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
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

    public static void getEvents(final CallBack<List<CalendarEvent>> onCallBack) {
        String URL = URLRoot + "/schedule/events";
        makeJsonArrayRequest(CalendarEvent.class, URL, false, onCallBack);
    }

    public static void getNews(final Integer newsId, final int chunkSize, final CallBack<List<News>> onCallBack) {
        String URL = URLRoot + "/news?newsId=" + newsId + "&chunkSize=" + chunkSize;
        makeJsonArrayRequest(News.class, URL, false, onCallBack);
    }

    public static void getNewsImage(final News news, final CallBack<News> onCallBack) {

        String imageURL = URLRoot + "/images/" + news.ImageName;

        getImage(imageURL, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(Bitmap image) {
                news.Image = image;
                onCallBack.onSuccess(news);
            }

            @Override
            public void onFail(String msg) {
                //Unable to retrieve news image
                news.Image = null;
            }
        });
    }

    public static void getNewsImages(final List<News> newsList, final CallBack<News> onCallBack) {
        for (final News news : newsList) {
            if (news.ImageName != null) {
                getNewsImage(news, onCallBack);
            }
        }
    }

    public static void getNewsDetails(final int newsId, final CallBack<News> onCallBack) {
        String URL = URLRoot + "/news/" + newsId;
        makeJsonObjectRequest(News.class, URL, false, onCallBack);
    }

    public static void getStudentPlan(final CallBack<StudentPlan> onCallBack) {
        String URL = URLRoot + "/studentPlan";
        makeJsonObjectRequest(StudentPlan.class, URL, true, onCallBack);
    }

    public static void getCurrentElectionCampaign(final CallBack<ElectionCampaign> onCallBack) {
        String URL = URLRoot + "/electivesCampaign";
        makeJsonObjectRequest(ElectionCampaign.class, URL, false, onCallBack);
    }

    public static void getElectionCampaignCourses(final CallBack<List<ElectionCourse>> onCallBack) {
        User loggedUser = ApplicationController.getLoggedUser();

        if (loggedUser != null) {
            String URL = URLRoot + "/electivesCampaign/courses";
            makeJsonArrayRequest(ElectionCourse.class, URL, true, onCallBack);
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
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

    public static void addNews(final News newsToSave, final Response.Listener onSuccess, final Response.ErrorListener onError) {
        String urlAddNews = URLRoot + "/news/add";
        News temp = new News();
        temp.Text = newsToSave.Text;
        temp.Title = newsToSave.Title;

        String requestBody = gson.toJson(temp, News.class);

        //Save the title and the text first
        makeJsonObjectRequest(News.class, urlAddNews, Request.Method.POST, requestBody, true, new CallBack<News>() {
            @Override
            public void onSuccess(News data) {
                newsToSave.Id = data.Id;

                if (newsToSave.Image != null) {
                    try {
                        String urlAddNewsImage = URLRoot + "/news/add/image?newsId=" + newsToSave.Id;
                        //Store the image
                        PostPhotoRequest imageUploadReq = new PostPhotoRequest(urlAddNewsImage,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        onError.onErrorResponse(error);
                                    }
                                },
                                new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        onSuccess.onResponse(response);
                                    }
                                },
                                newsToSave.getImageFile());


                        //Add authentication header
                        imageUploadReq.getHeaders().putAll(getAuthenticationHeader());

                        ApplicationController.getInstance().addToRequestQueue(imageUploadReq);

                    } catch (AuthFailureError authFailureError) {
                        authFailureError.printStackTrace();
                    }
                } else {
                    onSuccess.onResponse(null);
                }
            }

            @Override
            public void onFail(String msg) {
                Log.e("FMI_APP_EXCEPTION", msg);
            }
        });
    }

    public static void enrollCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = URLRoot + "/electivesCampaign/courses/enroll?courseId=" + courseId;

        HashMap<String, String> authHeader = getAuthenticationHeader();

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.POST,
                URL,
                authHeader,
                "",
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void cancelCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = URLRoot + "/electivesCampaign/courses/cancel?courseId=" + courseId;

        HashMap<String, String> authHeader = getAuthenticationHeader();

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.POST,
                URL,
                authHeader,
                "",
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getAdministrationContacts(final CallBack<List<AdministrationCategoryContacts>> onCallBack) {
        String URL = URLRoot + "/contacts/administration";
        makeJsonArrayRequest(AdministrationCategoryContacts.class, URL, false, onCallBack);
    }

    public static void getLecturersContacts(final CallBack<List<LecturerContact>> onCallBack) {
        String URL = URLRoot + "/contacts/lecturers";
        makeJsonArrayRequest(LecturerContact.class, URL, false, onCallBack);
    }

    public static void getRooms(final CallBack<List<Room>> onCallBack) {
        String URL = URLRoot + "/schedule/events/rooms";
        makeJsonArrayRequest(Room.class, URL, false, onCallBack);
    }

    public static void createEvent(final CalendarEvent event, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = URLRoot + "/schedule/events";

        String requestBody = gson.toJson(event);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.POST,
                URL,
                null,
                requestBody,
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }


    private static void getImage(final String url, final CallBack<Bitmap> onCallBack) {

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        onCallBack.onSuccess(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });
        ApplicationController.getInstance().addToRequestQueue(request);
    }

    private static HashMap<String, String> getAuthenticationHeader() {

        //TODO add the real user credentials in the header
        //return getAuthenticationHeader("student1", "s1");

        User loggedUser = ApplicationController.getLoggedUser();
        if (loggedUser != null) {
            return getAuthenticationHeader(loggedUser.UserId, loggedUser.Password);
        } else {
            return null;
        }
    }

    private static HashMap<String, String> getAuthenticationHeader(String userid, String password) {
        HashMap<String, String> res = new HashMap<>();
        res.put("Authorization", getB64Auth(userid, password));
        return res;
    }

    private static String getB64Auth(String login, String pass) {
        String source = login + ":" + pass;
        String ret = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

    private static <T> void makeJsonObjectRequest(final Class<T> typeParameterClass, final String url, final int method, String requestBody, final boolean addAuthHeader, final CallBack<T> onCallBack) {
        final HashMap<String, String> authHeader = new HashMap<>();
        if (addAuthHeader) {
            authHeader.putAll(getAuthenticationHeader());
        }

        JsonRequest req = new JsonObjectRequest(method, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObj = (JSONObject) response;

                try {
                    T result = gson.fromJson(jsonObj.toString(), typeParameterClass);
                    onCallBack.onSuccess(result);
                } catch (Exception ex) {
                    Log.e("FMI_APP_EXCEPTION", ex.toString());
                    onCallBack.onFail(ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FMI_APP_EXCEPTION", error.toString());
                onCallBack.onFail(error.toString());
                return;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (addAuthHeader && authHeader != null && !authHeader.isEmpty()) {
                    return authHeader;
                }
                return super.getHeaders();
            }
        };

        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private static <T> void makeJsonObjectRequest(final Class<T> typeParameterClass, final String url, final boolean addAuthHeader, final CallBack<T> onCallBack) {
        makeJsonObjectRequest(typeParameterClass, url, Request.Method.GET, null, addAuthHeader, onCallBack);
    }

    private static <T> void makeJsonArrayRequest(final Class<T> typeParameterClass, final String url, final boolean addAuthHeader, final CallBack<List<T>> onCallBack) {
        final Class<T> classType = typeParameterClass;
        final HashMap<String, String> authHeader = new HashMap<>();
        if (addAuthHeader) {
            authHeader.putAll(getAuthenticationHeader());
        }

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        final List<T> result = new ArrayList<T>();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObj = (JSONObject) response.get(i);

                                try {
                                    T item = gson.fromJson(jsonObj.toString(), typeParameterClass);
                                    result.add(item);
                                } catch (Exception ex) {
                                    Log.e("FMI_APP_EXCEPTION", ex.getMessage());
                                }
                            }

                            //Call the success callback
                            onCallBack.onSuccess(result);

                        } catch (JSONException e) {
                            Log.e("FMI_APP_EXCEPTION", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FMI_APP_EXCEPTION", error.getMessage());
                onCallBack.onFail(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (addAuthHeader && authHeader != null && !authHeader.isEmpty()) {
                    return authHeader;
                }
                return super.getHeaders();
            }
        };

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);

    }


}
