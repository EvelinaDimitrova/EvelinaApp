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

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.helper.adapter.NewsListAdapter;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends DrawerBaseActivity implements CallBack<List<News>> {

    private NewsListAdapter adapter;
    final List<News> newsList = new ArrayList<>();
    Button btnLoadExtra;
    private final int CHUNK_SIZE = 3;

    public static final String RELOAD_KEY = "reload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.content_news);

        adapter = new NewsListAdapter(NewsActivity.this, newsList);
        ListView newsListView = (ListView) findViewById(R.id.news_listView);
        newsListView.setAdapter(adapter);

        // Create the Load More... button
        btnLoadExtra = new Button(this);
        btnLoadExtra.setText("Load More...");
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

        boolean reload = true;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            b.getBoolean(RELOAD_KEY, true);
        }
        loadNews(reload);

        //DataAPI.getNews(0, 0, this);
    }


    private void loadNews(boolean reload) {

        if (reload){
            newsList.clear();
        }

        Integer newsId = null;
        if(!newsList.isEmpty()) {
            News lastNews = newsList.get(newsList.size() - 1);
            newsId = lastNews.Id;
        }
        NetworkAPI.getNews(newsId, CHUNK_SIZE, this);

    }

    @Override
    public void onSuccess(final List<News> nList) {

        newsList.addAll(nList);
        adapter.notifyDataSetChanged();

        if(nList.size() < CHUNK_SIZE) {
            btnLoadExtra.setVisibility(View.GONE);
        }

        NetworkAPI.getNewsImages(newsList, new CallBack<News>() {
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
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);

        if (ApplicationController.getInstance().isLoggedIn() && ApplicationController.getLoggedUser().Role.equals("LECT")) {
            MenuItem addNews = menu.findItem(R.id.action_create_news);
            addNews.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_news:
                Intent intent = new Intent(NewsActivity.this, CreateNewsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
