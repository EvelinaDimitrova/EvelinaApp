package com.fmi.evelina.unimobileapp.helper;

import android.graphics.Color;
import android.util.Log;

import com.fmi.evelina.unimobileapp.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventColorMap {

    public static int getEventColor(String eventType){

        switch (eventType){
            case "LECT" : return Color.parseColor("#FF0000");
            case "EXCE" : return Color.parseColor("#3F51B5");
            case "PRACT" : return Color.parseColor("#0000FF");
            case "ELECT" : return Color.parseColor("#3F51B5");
            case "EVENT" : return Color.parseColor("#00FF00");
            default: return Color.parseColor("#2789e4");
        }
    }
}
