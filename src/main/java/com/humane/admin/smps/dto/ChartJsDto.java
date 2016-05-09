package com.humane.admin.smps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ChartJsDto {

    private final List<String> labels;
    private final List<Dataset> datasets;

    public ChartJsDto() {
        labels = new ArrayList<>();
        datasets = new ArrayList<>();
    }

    public void addLabel(String majorNm) {
        labels.add(majorNm);
    }

    public void addDataset(Dataset dataset) {
        datasets.add(dataset);
    }

    @ToString
    @Getter
    public static class Dataset {
        private List<Object> data;
        private String label;

        public Dataset() {
            data = new ArrayList<>();
        }

        public Dataset(String label){
            this();
            this.label = label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void addData(Object o) {
            data.add(o);
        }
    }
}
