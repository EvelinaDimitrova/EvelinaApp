package com.fmi.evelina.unimobileapp.helper;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private static final String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try {
            Log.v("Eve_trace", "before = " + jsonElement.getAsString());
            return new SimpleDateFormat(DATE_FORMAT_LONG).parse(jsonElement.getAsString());
        } catch (ParseException e) {
        }

        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + DATE_FORMAT_LONG);
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        String dateFormatAsString = dateFormat.format(src);
        return new JsonPrimitive(dateFormatAsString);
    }


}