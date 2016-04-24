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
import com.fmi.evelina.unimobileapp.model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.RecurringCalendarEvent;
import com.fmi.evelina.unimobileapp.model.student_plan_model.StudentPlan;
import com.fmi.evelina.unimobileapp.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The network API
 */
public class DataAPI {

    private static final String URLRoot = ApplicationController.getServerURL();
    private static final Gson gson = ApplicationController.getGson();

    //TODO remove this mock method
    public static void signInTest(final String userId, final String password, final CallBack<User> onCallBack) {

        onCallBack.onSuccess(new User(userId, password));
        return;
    }

    public static void signIn(final String userId, final String password, final CallBack<User> onCallBack) {

        String URL = URLRoot + "/auth/signIn";

        final HashMap<String, String> authHeader = getAuthenticationHeader(userId, password);

        ServerStatusRequest req = new ServerStatusRequest(
                URL,
                authHeader,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        final User user = new User(userId, password);
                        onCallBack.onSuccess(user);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onCallBack.onFail("Unable to log in!");
                    }
                });


        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getStudentSchedule(final CallBack<List<RecurringCalendarEvent>> onCallBack) {

        User loggedUser = ApplicationController.getLoggedUser();

        if (loggedUser != null) {

            String URL = URLRoot + "/events/" + loggedUser.getUserId();
            final HashMap<String, String> authHeader = getAuthenticationHeader();

            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            final List<RecurringCalendarEvent> result = new ArrayList<RecurringCalendarEvent>();
                            try {
                                // Parsing json array response
                                // loop through each json object
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObj = (JSONObject) response.get(i);

                                    try {
                                        RecurringCalendarEvent calEv = gson.fromJson(jsonObj.toString(), RecurringCalendarEvent.class);
                                        result.add(calEv);
                                    } catch (Exception ex) {
                                        Log.e("EVE_TRACE_ERROR", ex.toString());
                                    }
                                }

                                //Call the success callback
                                onCallBack.onSuccess(result);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onCallBack.onFail("Unable to retrieve data");
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
        } else {
            onCallBack.onFail("Unable to retrieve data");
        }
    }

    public static void getEvents(final CallBack<List<CalendarEvent>> onCallBack) {

        String URL = URLRoot + "/events";

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("EVE_TRACE", "-01");

                        final List<CalendarEvent> result = new ArrayList<CalendarEvent>();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObj = (JSONObject) response.get(i);

                                try {
                                    CalendarEvent calEv = gson.fromJson(jsonObj.toString(), CalendarEvent.class);
                                    result.add(calEv);
                                } catch (Exception ex) {
                                    Log.e("EVE_TRACE_ERROR", ex.toString());
                                }
                            }

                            //Call the success callback
                            onCallBack.onSuccess(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onCallBack.onFail("Unable to retrieve data");
            }
        });

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);

    }

    public static void getNews(final Integer newsId, final int chunkSize, final CallBack<List<News>> onCallBack) {

        String URL = URLRoot + "/news?newsId="+newsId+"&chunkSize="+chunkSize;

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<News> result = new ArrayList<News>();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObj = (JSONObject) response.get(i);

                                try {
                                    News news = gson.fromJson(jsonObj.toString(), News.class);
                                    result.add(news);
                                } catch (Exception ex) {
                                    Log.e("EVE_TRACE_ERROR", ex.toString());
                                }
                            }

                            //Call the success callback
                            onCallBack.onSuccess(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onCallBack.onFail("Unable to retrieve data");
            }
        });

        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
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
            getNewsImage(news, onCallBack);
        }
    }

    public static void getNewsDetails(final int newsId, final CallBack<News> onCallBack) {
        String URL = URLRoot + "/news/" + newsId;

        JsonRequest req = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObj = (JSONObject) response;

                try {
                    News news = gson.fromJson(jsonObj.toString(), News.class);

                    onCallBack.onSuccess(news);
                } catch (Exception ex) {
                    Log.e("EVE_TRACE_ERROR", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EVE_TRACE_ERROR2", error.getMessage());
                return;
            }
        });

        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getCurrentElectionCampaign(final CallBack<ElectionCampaign> onCallBack) {
        String URL = URLRoot + "/electionCampaign";

        //TODO remove this
        ElectionCampaign campaign = new ElectionCampaign();
        campaign.OpenDate = new Date(2016,1,1);
        campaign.CloseDate = new Date(2016,2,2);
        campaign.EndDate = new Date(2016,3,3);
        onCallBack.onSuccess(campaign);
        return;

//TODO add authentication header
//        JsonRequest req = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONObject jsonObj = (JSONObject) response;
//
//                try {
//                    ElectionCampaign campaign = gson.fromJson(jsonObj.toString(), ElectionCampaign.class);
//                    onCallBack.onSuccess(campaign);
//                } catch (Exception ex) {
//                    Log.e("EVE_TRACE_ERROR", ex.toString());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                return;
//            }
//        });
//
//        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public static void getStudentPlan(final CallBack<StudentPlan> onCallBack){
        String URL = URLRoot + "/studentPlan";
        final HashMap<String, String> authHeader = getAuthenticationHeader();

        JsonRequest req = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObj = (JSONObject) response;

                try {
                    StudentPlan studentPlan = gson.fromJson(jsonObj.toString(), StudentPlan.class);
                    onCallBack.onSuccess(studentPlan);
                } catch (Exception ex) {
                    Log.e("EVE_TRACE_ERROR", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                return;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (authHeader != null && !authHeader.isEmpty()) {
                    return authHeader;
                }
                return super.getHeaders();
            }
        };;

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
        return getAuthenticationHeader("student1", "s1");

//        User loggedUser = ApplicationController.getLoggedUser();
//        if(loggedUser != null) {
//            return getAuthenticationHeader(loggedUser.getUserId(), loggedUser.getPassword());
//        }
//        else {
//            return null;
//        }
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
}
