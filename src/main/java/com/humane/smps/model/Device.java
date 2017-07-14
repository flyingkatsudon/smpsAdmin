package com.humane.smps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"packageName", "uuid", "versionName"})})
public class Device {
    @Id @GeneratedValue private Long deviceId;
    private String packageName;
    private String uuid;
    private String phoneNo;
    private String deviceNo;
    private String versionName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastDttm;
}
