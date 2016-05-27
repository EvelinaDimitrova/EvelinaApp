package com.fmi.evelina.unimobileapp.helper.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.News;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class NewsListAdapter extends ArrayAdapter<News> {
    private final Activity context;
    private final List<News> newsList;

    public NewsListAdapter(Activity context, List<News> newsList) {
        super(context, R.layout.news_list_item, newsList);

        this.context = context;
        this.newsList = newsList;
    }

    public View getView(int position, View view, ViewGroup parent) {


        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL);
        News currentNews = newsList.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.news_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.news_list_item_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.news_list_item_icon);
        TextView txtDate = (TextView) rowView.findViewById(R.id.news_list_item_date);

        txtTitle.setText(currentNews.Title);
        txtDate.setText(ApplicationController.fullDateTimeFormat.format(currentNews.Date));

        if (currentNews.Image != null) {
            imageView.setImageBitmap(currentNews.Image);
        } else {
            imageView.setImageResource(R.drawable.ic_menu_gallery);
        }
        //extratxt.setText("Description "+ itemname[position]);
        return rowView;

    }

    ;
}
