package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Exam implements Serializable{
    @Id private String examCd; // 시험코드

    @ManyToOne @JoinColumn(name = "fkExamCd") private Exam fkExam;
    private String examNm; // 시험명
    private String typeNm;

    @ManyToOne @JoinColumn(name = "admissionCd", nullable = false) private Admission admission; // 전형

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Temporal(TemporalType.DATE)
    private Date examDate; // 시험일자

    @Column(columnDefinition = "int default 100") private int totScore; // 총점

    @Column(columnDefinition = "int default 0") private int scorerCnt; // 채점자 수. 검증 및 통계에 쓰임.
    @Column(columnDefinition = "int default 1") private int virtNoStart; // 가번호 시작점. 헤더값과는 상관없음.
    @Column(columnDefinition = "int default 100") private int virtNoEnd; // 가번호 종료점.
    private int virtNoDigits; // 가번호 표시 자릿수
    private String virtNoType; // 가번호 타입. 수험번호? 답안지번호? 순차번호?
    // TODO: virtNoAssignType 임시 사용
    private String virtNoAssignType; // 가번호 할당 방식.
    @Column(columnDefinition = "bit default 1") private boolean isAbsence; // 결시버튼 사용여부
    @Column(columnDefinition = "bit default 1") private boolean isHorizontal; // 채점방식. 가로, 세로
    @Column(columnDefinition = "bit default 0") private boolean isMove; // 지정이동? 순차이동? => uplus 기준 문제별 채점, 학생별 채점
    @Column(columnDefinition = "bit default 1") private boolean isClosedView;  // 타 사용자가 이전 채점자의 마감데이터를 볼지 여부 결정.
    @Column(columnDefinition = "bit default 1") private boolean isMgrAuto; // 가번호 자동 or 수동

    @Column(columnDefinition = "int default 0") private int examineeLen; // 수험번호 자릿수

    private String keypadType;

    private String period; // 단계

    // 채점앱용 프린트
    private String printTitle1;
    private String printTitle2;
    private String printContent1;
    private String printContent2;
    private String printSign;

    @Column(columnDefinition = "int default 0") private int itemCnt;

    private String barcodeType;

    @Column(columnDefinition = "decimal(10, 1) default 0") BigDecimal adjust;
}
