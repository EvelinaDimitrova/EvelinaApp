package com.fmi.evelina.unimobileapp.activity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.DataAPI;

import org.w3c.dom.Text;

public class NewsDetailsActivity extends DrawerBaseActivity implements CallBack<News> {

    public static final String NEWS_ID_KEY = "newsId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        int newsId = b.getInt(NEWS_ID_KEY);

        setDrawerContentView(R.layout.content_news_details);

        DataAPI.getNewsDetails(newsId,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_back:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(News news) {

        final TextView title = (TextView) findViewById(R.id.news_details_title);
        final ImageView image = (ImageView) findViewById(R.id.news_details_image);
        final TextView date = (TextView) findViewById(R.id.news_details_date);
        final TextView text = (TextView) findViewById(R.id.news_details_text);

        Log.v("ASD", news.Title);
        title.setText(news.Title);
        date.setText(news.Date.toString());
        text.setText(news.Text);

        DataAPI.getNewsImage(news, new CallBack<News>() {
            @Override
            public void onSuccess(News data) {
                if(data.Image != null){
                    image.setVisibility(View.VISIBLE);
                    image.setImageBitmap(data.Image);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void onFail(String msg) {

    }
}
