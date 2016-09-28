package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"examCd", "hallCd"})})
@Data
public class ExamHall {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
}