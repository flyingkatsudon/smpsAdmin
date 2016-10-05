package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examCd", "hallCd", "hallDate"}))
@Data
public class ExamHallDate {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "examCd", nullable = false) private Exam exam;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Temporal(TemporalType.DATE)
    private Date hallDate;

    @Column String virtNoStart;
    @Column String virtNoEnd;
}
