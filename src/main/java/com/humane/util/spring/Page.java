package com.humane.util.spring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

import java.util.List;

/**
 * spring data jpa page wrapper
 * @param <T>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Page<T> {
    public List<T> content;
    public boolean last;
    public int totalElements;
    public int totalPages;
    public int size;
    public int number;
    public int numberOfElements;
    boolean first;
    List<Sort> sort;

    @ToString
    public class Sort {
        boolean ascending;
        String direction;
        boolean ignoreCase;
        String nullHandling;
        String property;
    }
}
