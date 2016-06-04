package com.fmi.evelina.unimobileapp.activity.News;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.model.UserRole;
import com.fmi.evelina.unimobileapp.network.ICallBack;

public class NewsDetailsActivity extends DrawerBaseActivity implements ICallBack<News> {

    public static final String NEWS_ID_KEY = "newsId";
    public static final int NEWS_DELETED = 1;
    private static String newsImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawerContentView(R.layout.content_news_details);


        Bundle b = getIntent().getExtras();
        final int newsId = b.getInt(NEWS_ID_KEY);

        //Show the Delete button if the user is Administrator
        if (ApplicationController.isLoggedIn() && ApplicationController.getLoggedUser().Role.equals(UserRole.ADMN)) {
            Button deleteBtn = (Button) findViewById(R.id.news_details_delete_button);
            deleteBtn.setVisibility(View.VISIBLE);

            //Set the Delete button handler
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Show delete confirmation
                    new AlertDialog.Builder(NewsDetailsActivity.this)
                            .setTitle(getString(R.string.confirmation_label))
                            .setMessage(getString(R.string.confirm_delete))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(R.string.confirmation_yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ApplicationController.getDataProvider().deleteNews(newsId, newsImageName, new Response.Listener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            //On successful delete return the deleted news id
                                            Bundle conData = new Bundle();
                                            conData.putInt(NEWS_ID_KEY, newsId);

                                            Intent intent = new Intent();
                                            intent.putExtras(conData);

                                            setResult(NEWS_DELETED, intent);
                                            finish();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ApplicationController.showErrorToast();
                                        }
                                    });
                                }
                            }).setNegativeButton(R.string.confirmation_no, null).show();
                }
            });
        }

        //Load the news
        ApplicationController.getDataProvider().getNewsDetails(newsId, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back_menu, menu);
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

        title.setText(news.Title);
        date.setText(ApplicationController.fullDateTimeFormat.format(news.Date));
        text.setText(news.Text);

        if (news.ImageName != null) {
            //Store the image name in a variable available for the delete function
            newsImageName = news.ImageName;
            ApplicationController.getDataProvider().getNewsImage(news, new ICallBack<News>() {
                @Override
                public void onSuccess(News data) {
                    if (data.Image != null) {
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(data.Image);
                    }
                }

                @Override
                public void onFail(String msg) {
                    //Don't show an error, just leave the iamge blank
                    //ApplicationController.showErrorToast();
                }
            });
        }
    }

    @Override
    public void onFail(String msg) {
        ApplicationController.showErrorToast();
    }

    //Reset the title
    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.title_activity_news_details));
    }
}
