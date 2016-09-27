package com.humane.smps.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"admissionCd", "userId"})})
@Getter
@Setter
@ToString
public class UserAdmission {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    @ManyToOne @JoinColumn(name = "admissionCd", nullable = false) private Admission admission;
    @ManyToOne @JoinColumn(name = "userId", nullable = false) private User user;
}
