package com.mindbridge.server.model;

import com.mindbridge.server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 진료 아이디
    private Long id;

    // 진료 일정
    @Column(nullable = false)
    private Date date;

    // 시작 시간
    @Column(nullable = false)
    private LocalTime startTime;

    // 종료 시간
    @Column(nullable = false)
    private LocalTime endTime;

    // 주치의
    @Column(nullable = true)
    private String doctorName;

    // 병원
    @Column(nullable = true)
    private String hospital;

    // 메모
    @Column(columnDefinition = "TEXT")
    private String memo;

    // record와 연관 관계 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @OneToMany(mappedBy = "appointment" /*cascade = CascadeType.ALL, orphanRemoval = true */)
    private List<Mindlog> mindlogs = new ArrayList<>();

    // default 생성자
    public Appointment() {
    }

    public Appointment(Long id, Date date, LocalTime startTime, LocalTime endTime, String doctorName, String hospital, String memo) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorName = doctorName;
        this.hospital = hospital;
        this.memo = memo;
    }

}
