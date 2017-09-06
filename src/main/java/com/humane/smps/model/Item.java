package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
@Table(uniqueConstraints = @UniqueConstraint(name = "UK__ITEM__01", columnNames = {"examCd", "itemNo"}))
public class Item implements Serializable {
    @Id @GeneratedValue private Long _id;

    @Column(nullable = false) private String itemNo;
    private String itemNm;
    private Long minScore;
    private Long maxScore;
    private String keypadType;

    private String deviCd; // 필요없음, 나중에 지워야함

    private String scoreMap;
    private Long minWarning;
    private Long maxWarning;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;

    @Column(columnDefinition = "int default 0") private long orderby;

}