package com.humane.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Iterator;
import java.util.Map;

public class ObjectConvert {

    public static Map<String, Object> asMap(Object object) {
        return asMap(object, false);
    }

    public static Map<String, Object> asMap(Object object, boolean useEmpty) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        Map<String, Object> map = objectMapper.convertValue(object, new TypeReference<Map>() {
        });

        if (!useEmpty) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                Object value = entry.getValue();
                if (value == null || value.equals("")) iterator.remove();
            }
        }
        return map;
    }
}
