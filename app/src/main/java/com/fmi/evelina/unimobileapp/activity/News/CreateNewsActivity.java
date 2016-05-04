package com.fmi.evelina.unimobileapp.activity.News;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.DrawerBaseActivity;
import com.fmi.evelina.unimobileapp.model.News;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.io.FileDescriptor;
import java.io.IOException;

public class CreateNewsActivity extends DrawerBaseActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static News newsToSave = new News();

    private Button buttonLoadImage;
    private Button buttonSaveNews;
    private EditText title;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDrawerContentView(R.layout.content_create_news);

        title = (EditText) findViewById(R.id.create_news_title);
        text = (EditText) findViewById(R.id.create_news_text);

        buttonLoadImage = (Button) findViewById(R.id.create_news_add_image);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        buttonSaveNews = (Button) findViewById(R.id.create_news_save_button);
        buttonSaveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newsToSave.Title = title.getText().toString();
                newsToSave.Text = text.getText().toString();

                NetworkAPI.addNews(newsToSave,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Intent intent = new Intent(CreateNewsActivity.this, NewsActivity.class);
                                Bundle b = new Bundle();
                                b.putBoolean(NewsActivity.RELOAD_KEY, true);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
            }
        });


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            newsToSave.Image = bmp;

            ImageView imageView = (ImageView) findViewById(R.id.create_news_image);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(newsToSave.Image);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
