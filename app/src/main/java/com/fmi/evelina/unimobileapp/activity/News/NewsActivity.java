package com.fmi.evelina.unimobileapp.activity.News;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.Calendar.CreateEventActivity;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.helper.adapter.NewsListAdapter;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.UserRole;
import com.fmi.evelina.unimobileapp.network.CallBack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class NewsActivity extends DrawerBaseActivity implements CallBack<List<News>> {


    private NewsListAdapter adapter;
    final List<News> newsList = new ArrayList<>();
    Button btnLoadExtra;
    private final int CHUNK_SIZE = 3;

    //keys for opening the activities for results
    private static final int NEWS_DETAILS_REQUEST = 1;
    private static final int CREATE_NEWS_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.content_news);

        adapter = new NewsListAdapter(NewsActivity.this, newsList);
        ListView newsListView = (ListView) findViewById(R.id.news_listView);
        newsListView.setAdapter(adapter);

        // Create the Load More... button
        btnLoadExtra = new Button(this);
        btnLoadExtra.setText(R.string.news_load);
        // Adding Load More button to lisview at bottom
        newsListView.addFooterView(btnLoadExtra);

        btnLoadExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadNews(false);
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int newsId = newsList.get(position).Id;

                showNewsDetailsView(newsId);
            }
        });

        loadNews(true);
    }


    private void loadNews(boolean reload) {

        if (reload) {
            newsList.clear();
        }

        Integer newsId = null;
        if (!newsList.isEmpty()) {
            News lastNews = newsList.get(newsList.size() - 1);
            newsId = lastNews.Id;
        }
        ApplicationController.getDataProvider().getNews(newsId, CHUNK_SIZE, this);

    }

    @Override
    public void onSuccess(final List<News> nList) {

        newsList.addAll(nList);
        adapter.notifyDataSetChanged();

        if (nList.size() < CHUNK_SIZE) {
            btnLoadExtra.setVisibility(View.GONE);
        }

        ApplicationController.getDataProvider().getNewsImages(newsList, new CallBack<News>() {
            @Override
            public void onSuccess(News data) {

                for (News n : newsList) {
                    if (n.Id == data.Id) {
                        n.Image = data.Image;
                        break;
                    }
                }

                //adapter.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String msg) {
                Log.e("EVE_TRACE_ERROR", msg);

            }
        });

    }

    @Override
    public void onFail(String msg) {
        Log.e("EVE_TRACE_ERROR", msg);
    }

    private void showNewsDetailsView(int newsId) {
        Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
        Bundle b = new Bundle();
        b.putInt(NewsDetailsActivity.NEWS_ID_KEY, newsId);
        intent.putExtras(b);
        startActivityForResult(intent, NEWS_DETAILS_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);

        if (ApplicationController.getInstance().isLoggedIn()) {
            UserRole role = ApplicationController.getLoggedUser().Role;
            if (role.equals(UserRole.LECT) || role.equals(UserRole.ADMN)) {
                MenuItem addNews = menu.findItem(R.id.action_create_news);
                addNews.setVisible(true);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_news:
                Intent intent = new Intent(NewsActivity.this, CreateNewsActivity.class);
                startActivityForResult(intent, CREATE_NEWS_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //We expect to get here after opening the Create Event activity for result. This is the callback handler
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEWS_DETAILS_REQUEST) {
            if (resultCode == NewsDetailsActivity.NEWS_DELETED) {
                //Show an information toast
                Toast.makeText(NewsActivity.this, R.string.news_deleted, Toast.LENGTH_LONG).show();

                //Get the deleted news ID
                Bundle b = data.getExtras();
                int newsID = b.getInt(NewsDetailsActivity.NEWS_ID_KEY);

                //Remove the news from the list
                Iterator<News> i = newsList.iterator();
                while (i.hasNext()) {
                    News news = i.next();
                    if (news.Id == newsID) {
                        i.remove();
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
        if (requestCode == CREATE_NEWS_REQUEST) {
            if (resultCode == CreateNewsActivity.NEWS_CREATED) {
                //Show an information toast
                Toast.makeText(NewsActivity.this, R.string.news_created, Toast.LENGTH_LONG).show();
                loadNews(true);
            }
        }
    }

    //Reset the title
    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.title_activity_news));
    }
}
