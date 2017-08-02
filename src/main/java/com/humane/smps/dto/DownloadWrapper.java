package com.humane.smps.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadWrapper {
    private String url;
    private List<Wrapper> list;

    @Data
    public static class Wrapper {
        private String examCd;
        private String hallCd;
    }
}
