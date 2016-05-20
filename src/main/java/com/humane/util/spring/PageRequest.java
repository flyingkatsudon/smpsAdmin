package com.humane.util.spring;

import lombok.Data;

@Data
public class PageRequest {
    private Integer page;
    private Integer size;
    private String sort;
}
