package com.fmi.evelina.unimobileapp.network;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.UserRole;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The network API
 */
public class NetworkAPI {

    private static String getURLRoot() {
        return ApplicationController.getServerURLPref();
    }

    private static final Gson gson = ApplicationController.getGson();
    private static final String LOCALE_HEADER_KEY = "Locale";
    private static final String CONTENT_MODE_HEADER_KEY = "ContentMode";
    private static final String CONTENT_MODE_COMPACT_KEY = "Compact";
    private static final String CONTENT_MODE_FULL_KEY = "Full";

    public static void signIn(final String userId, final String password, final ICallBack<User> onCallBack) {

        String URL = getURLRoot() + "/user";

        JsonRequest req = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObj = (JSONObject) response;

                try {
                    User user = gson.fromJson(jsonObj.toString(), User.class);
                    user.Password = password;

                    onCallBack.onSuccess(user);
                } catch (Exception ex) {
                    //Log.e("FMI_APP_EXCEPTION", ex.toString());
                    onCallBack.onFail("Exception in signIn!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onCallBack.onFail("Exception in signIn!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> result = new HashMap<>();
                HashMap<String, String> authHeader = getAuthenticationHeader(userId, password);
                if (authHeader != null && !authHeader.isEmpty()) {
                    result.putAll(authHeader);
                }
                addControlHeaders(result);
                return result;
            }
        };


        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getStudentSchedule(final ICallBack<List<RecurringStudentCalendarEvent>> onCallBack) {
        User loggedUser = ApplicationController.getLoggedUser();
        if (loggedUser != null && loggedUser.Role.equals(UserRole.STUD)) {
            String URL = getURLRoot() + "/schedule?role=" + loggedUser.Role;
            makeJsonArrayRequest(RecurringStudentCalendarEvent.class, URL, true, onCallBack);
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
    }

    public static void getLecturerSchedule(final ICallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        User loggedUser = ApplicationController.getLoggedUser();
        if (loggedUser != null && loggedUser.Role.equals(UserRole.LECT)) {
            String URL = getURLRoot() + "/schedule?role=" + loggedUser.Role;
            makeJsonArrayRequest(RecurringLecturerCalendarEvent.class, URL, true, onCallBack);
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
    }

    public static void getEvents(final ICallBack<List<CalendarEvent>> onCallBack) {
        String URL = getURLRoot() + "/schedule/events";
        makeJsonArrayRequest(CalendarEvent.class, URL, false, onCallBack);
    }

    public static void getNews(final Integer newsId, final int chunkSize, final ICallBack<List<News>> onCallBack) {
        String URL = getURLRoot() + "/news?newsId=" + newsId + "&chunkSize=" + chunkSize;
        makeJsonArrayRequest(News.class, URL, false, onCallBack);
    }

    public static void getNewsImage(final News news, final ICallBack<News> onCallBack) {

        String imageURL = getURLRoot() + "/images/" + news.ImageName;

        getImage(imageURL, new ICallBack<Bitmap>() {
            @Override
            public void onSuccess(Bitmap image) {
                news.Image = image;
                onCallBack.onSuccess(news);
            }

            @Override
            public void onFail(String msg) {
                //Unable to retrieve news image
                news.Image = null;
                onCallBack.onFail("Exception in getNewsImage!");
            }
        });
    }

    public static void getNewsImages(final List<News> newsList, final ICallBack<News> onCallBack) {
        for (final News news : newsList) {
            if (news.ImageName != null) {
                getNewsImage(news, onCallBack);
            }
        }
    }

    public static void getNewsDetails(final int newsId, final ICallBack<News> onCallBack) {
        String URL = getURLRoot() + "/news/" + newsId;
        makeJsonObjectRequest(News.class, URL, false, onCallBack);
    }

    public static void addNews(final News newsToSave, final Response.Listener onSuccess, final Response.ErrorListener onError) {
        String urlAddNews = getURLRoot() + "/news";
        News temp = new News();
        temp.Text = newsToSave.Text;
        temp.Title = newsToSave.Title;

        String requestBody = gson.toJson(temp, News.class);

        //Save the title and the text first
        makeJsonObjectRequest(News.class, urlAddNews, Request.Method.POST, requestBody, true, new ICallBack<News>() {
            @Override
            public void onSuccess(News data) {
                newsToSave.Id = data.Id;

                if (newsToSave.Image != null) {
                    String urlAddNewsImage = getURLRoot() + "/news/image?newsId=" + newsToSave.Id;
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
                            newsToSave.getImageFile()) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();

                            //Add authentication header
                            addAuthenticationHeader(headers);
                            //Add control headers
                            addControlHeaders(headers);

                            return headers;
                        }
                    };

                    ApplicationController.getInstance().addToRequestQueue(imageUploadReq);


                } else {
                    onSuccess.onResponse(null);
                }
            }

            @Override
            public void onFail(String msg) {
                //Log.e("FMI_APP_EXCEPTION", msg);
                onError.onErrorResponse(new VolleyError("msg"));
            }
        });
    }

    public static void deleteNews(final Integer newsId, final String imageName, final Response.Listener onSuccess, final Response.ErrorListener onError) {

        String URL = getURLRoot() + "/news/" + newsId;

        HashMap<String, String> headers = new HashMap<>();

        //Add authentication header
        addAuthenticationHeader(headers);
        //Add control headers
        addControlHeaders(headers);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.DELETE,
                URL,
                headers,
                "",
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        //If the news has Image make a call to delete it
                        if (imageName != null) {
                            deleteImage(imageName, onSuccess, onError);
                        }
                        else{
                            onSuccess.onResponse(response);
                        }
                    }
                },
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getStudentPlan(final ICallBack<StudentPlan> onCallBack) {
        String URL = getURLRoot() + "/studentPlan";
        makeJsonObjectRequest(StudentPlan.class, URL, true, onCallBack);
    }

    public static void getCurrentElectionCampaign(final ICallBack<ElectionCampaign> onCallBack) {
        String URL = getURLRoot() + "/electivesCampaign";
        makeJsonObjectRequest(ElectionCampaign.class, URL, false, onCallBack);
    }

    public static void getElectionCampaignCourses(final ICallBack<List<ElectionCourse>> onCallBack) {
        User loggedUser = ApplicationController.getLoggedUser();

        if (loggedUser != null) {
            String URL = getURLRoot() + "/electivesCampaign/courses";
            makeJsonArrayRequest(ElectionCourse.class, URL, true, onCallBack);
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
    }

    public static void enrollCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = getURLRoot() + "/electivesCampaign/courses/enroll?courseId=" + courseId;

        HashMap<String, String> headers = new HashMap<>();

        //Add authentication header
        addAuthenticationHeader(headers);
        //Add control headers
        addControlHeaders(headers);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.POST,
                URL,
                headers,
                "",
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void cancelCourse(final Integer courseId, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = getURLRoot() + "/electivesCampaign/courses/cancel?courseId=" + courseId;

        HashMap<String, String> headers = new HashMap<>();

        //Add authentication header
        addAuthenticationHeader(headers);
        //Add control headers
        addControlHeaders(headers);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.POST,
                URL,
                headers,
                "",
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getAdministrationContacts(final ICallBack<List<AdministrationCategoryContacts>> onCallBack) {
        String URL = getURLRoot() + "/contacts/administration";
        makeJsonArrayRequest(AdministrationCategoryContacts.class, URL, false, onCallBack);
    }

    public static void getLecturersContacts(final ICallBack<List<LecturerContact>> onCallBack) {
        String URL = getURLRoot() + "/contacts/lecturers";
        makeJsonArrayRequest(LecturerContact.class, URL, false, onCallBack);
    }

    public static void getRooms(final ICallBack<List<Room>> onCallBack) {
        String URL = getURLRoot() + "/schedule/events/rooms";
        makeJsonArrayRequest(Room.class, URL, false, onCallBack);
    }

    public static void createEvent(final CalendarEvent event, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = getURLRoot() + "/schedule/events";

        HashMap<String, String> headers = new HashMap<>();
        //Add control headers
        addControlHeaders(headers);

        String requestBody = gson.toJson(event);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.POST,
                URL,
                headers,
                requestBody,
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void deleteEvent(final Integer eventId, Response.Listener onSuccess, Response.ErrorListener onError) {

        String URL = getURLRoot() + "/schedule/events/" + eventId;

        HashMap<String, String> headers = new HashMap<>();

        //Add authentication header
        addAuthenticationHeader(headers);
        //Add control headers
        addControlHeaders(headers);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.DELETE,
                URL,
                headers,
                "",
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private static void getImage(final String url, final ICallBack<Bitmap> onCallBack) {

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
                        onCallBack.onFail("Exception in getImage!");
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request);
    }

    private static void deleteImage(final String imageName, Response.Listener onSuccess, Response.ErrorListener onError) {
        String URL = getURLRoot() + "/images/" + imageName;

        HashMap<String, String> headers = new HashMap<>();

        //Add authentication header
        addAuthenticationHeader(headers);
        //Add control  headers
        addControlHeaders(headers);

        ServerStatusRequest req = new ServerStatusRequest(Request.Method.DELETE,
                URL,
                headers,
                "",
                onSuccess,
                onError);

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private static HashMap<String, String> getAuthenticationHeader() {

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

    private static void addControlHeaders(Map header) {
        //Add ContentMode header
        switch (ApplicationController.getContentPref()){
            case "DETECT" : {
                int networkType = ApplicationController.getNetworkType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    //If connected to WiFi request for full content
                    header.put(CONTENT_MODE_HEADER_KEY, CONTENT_MODE_FULL_KEY);
                } else {
                    header.put(CONTENT_MODE_HEADER_KEY, CONTENT_MODE_COMPACT_KEY);
                }
                break;
            }
            case "FULL" : {
                header.put(CONTENT_MODE_HEADER_KEY, CONTENT_MODE_FULL_KEY);
                break;
            }
            case "COMPACT" : {
                header.put(CONTENT_MODE_HEADER_KEY, CONTENT_MODE_COMPACT_KEY);
                break;
            }
            default : {
                header.put(CONTENT_MODE_HEADER_KEY, CONTENT_MODE_FULL_KEY);
                break;
            }
        }
        
        //Add locale header
        header.put(LOCALE_HEADER_KEY, ApplicationController.getLocalePref());

    }

    private static void addAuthenticationHeader(Map header) {
        Map<String, String> authHeader = getAuthenticationHeader();
        if (authHeader != null && !authHeader.isEmpty()) {
            header.putAll(authHeader);
        }
    }

    private static String getB64Auth(String login, String pass) {
        String source = login + ":" + pass;
        String ret = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return ret;
    }

    private static <T> void makeJsonObjectRequest(final Class<T> typeParameterClass, final String url, final int method, String requestBody, final boolean addAuthHeader, final ICallBack<T> onCallBack) {

        JsonRequest req = new JsonObjectRequest(method, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObj = (JSONObject) response;

                try {
                    T result = gson.fromJson(jsonObj.toString(), typeParameterClass);
                    onCallBack.onSuccess(result);
                } catch (Exception ex) {
                    //Log.e("FMI_APP_EXCEPTION", ex.toString());
                    onCallBack.onFail("Exception in makeJsonObjectRequest!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("FMI_APP_EXCEPTION", error.toString());
                onCallBack.onFail("Exception in makeJsonObjectRequest!");
                return;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (addAuthHeader) {
                    addAuthenticationHeader(headers);
                }
                addControlHeaders(headers);
                return headers;
            }
        };

        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private static <T> void makeJsonObjectRequest(final Class<T> typeParameterClass, final String url, final boolean addAuthHeader, final ICallBack<T> onCallBack) {
        makeJsonObjectRequest(typeParameterClass, url, Request.Method.GET, null, addAuthHeader, onCallBack);
    }

    private static <T> void makeJsonArrayRequest(final Class<T> typeParameterClass, final String url, final boolean addAuthHeader, final ICallBack<List<T>> onCallBack) {
        final Class<T> classType = typeParameterClass;

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
                                    //Log.e("FMI_APP_EXCEPTION", ex.toString());
                                    onCallBack.onFail("Exception in makeJsonArrayRequest!");
                                }
                            }

                            //Call the success callback
                            onCallBack.onSuccess(result);

                        } catch (JSONException e) {
                            //Log.e("FMI_APP_EXCEPTION", e.toString());
                            onCallBack.onFail("Exception in makeJsonArrayRequest!");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("FMI_APP_EXCEPTION", error.toString());
                onCallBack.onFail("Exception in makeJsonArrayRequest!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                if (addAuthHeader) {
                    addAuthenticationHeader(headers);
                }
                addControlHeaders(headers);
                return headers;
            }
        };

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);

    }


}
