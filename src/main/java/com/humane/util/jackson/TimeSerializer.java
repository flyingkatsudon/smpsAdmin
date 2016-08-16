package com.humane.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSerializer extends JsonSerializer<Date> {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (date == null) jsonGenerator.writeNull();
        else jsonGenerator.writeString(FORMATTER.format(date.getTime()));
    }
}
