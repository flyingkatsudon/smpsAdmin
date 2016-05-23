package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Cacheable
@Table(uniqueConstraints = @UniqueConstraint(name = "UK__ITEM__01", columnNames = {"examCd", "itemNo"}))
public class Item implements Serializable {
    @Id @GeneratedValue private long _id;

    private String itemNo;
    private String itemNm;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "deviCd", nullable = false) private Devi devi;

    @Column(columnDefinition = "int default 0") private int orderby;

}