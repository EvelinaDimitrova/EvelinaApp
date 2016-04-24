package com.fmi.evelina.unimobileapp.helper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateDeserializer implements JsonDeserializer<Date> {
    private static final String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try {
            return new SimpleDateFormat(DATE_FORMAT_SHORT, Locale.US).parse(jsonElement.getAsString());
        } catch (ParseException e) {
        }

        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + DATE_FORMAT_SHORT);
    }
}