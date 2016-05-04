package com.fmi.evelina.unimobileapp.helper;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeDeserializer implements JsonDeserializer<Time>, JsonSerializer<Time> {
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

    @Override
    public Time deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try {

            String s = jsonElement.getAsString();
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.US);
            sdf.parse(s);
            long ms = sdf.parse(s).getTime();
            Time t = new Time(ms);
            return t;
        } catch (ParseException e) {
        }
        throw new JsonParseException("Unparseable time: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + TIME_FORMAT);
    }

    @Override
    public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
        String dateFormatAsString = timeFormat.format(src);
        return new JsonPrimitive(dateFormatAsString);
    }
}
