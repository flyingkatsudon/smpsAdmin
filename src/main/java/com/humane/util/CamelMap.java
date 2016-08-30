package com.humane.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelMap<K, V> extends ListOrderedMap {

    private static final long serialVersionUID = 1L;

    @Override
    public Object put(Object key, Object value) {
        String _key = key.toString();
        String firstChar = _key.substring(0, 1);
        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(firstChar);
        CaseFormat caseFormat = CaseFormat.UPPER_UNDERSCORE;

        if (matcher.matches()) caseFormat = CaseFormat.LOWER_UNDERSCORE;

        return super.put(caseFormat.to(CaseFormat.LOWER_CAMEL, _key), value);
    }
}