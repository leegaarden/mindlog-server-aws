package com.mindbridge.server.model;

import com.mindbridge.server.common.BaseEntity;
import com.mindbridge.server.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//import java.time.LocalTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Mindlog extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 날짜
    @Column(nullable = false)
    private Date date;

    // 작성 시간
    @Column(nullable = false)
    private LocalTime time;

    // 감정 리스트
    @Column(nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> moods = new ArrayList<>();

    // 기분 색
    @Column(nullable = false)
    private int moodColor;

    // 제목
    @Column(columnDefinition = "TEXT NOT NULL")
    private String title;

    // 감정 기록 내용
    @Column(columnDefinition = "TEXT NOT NULL")
    private String emotionRecord;

    // 이벤트 기록 내용
    @Column(columnDefinition = "TEXT NOT NULL")
    private String eventRecord;

    // 질문 기록 내용
    @Column(columnDefinition = "TEXT NOT NULL")
    private String questionRecord;

    // 진료 일정 아이디 연동
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = true)
    private Appointment appointment;

     // 기록 총정리
    @Column(columnDefinition = "TEXT", nullable = true)
    private String allRecord;

    // 통계용 기록 정리 -> emotion + event
    @Column(columnDefinition = "TEXT", nullable = true)
    private String emotionEvent;

    // 감정 기록 요약
    @Column(columnDefinition = "TEXT")
    private String emotionSummary;

    // 이벤트 기록 요약
    @Column(columnDefinition = "TEXT")
    private String eventSummary;

    // 질문 기록 요약
    @Column(columnDefinition = "TEXT")
    private String questionSummary ;

    // 통계용 기록 요약
    @Column(columnDefinition = "TEXT")
    private String emotionEventSummary;


    // default 생성자
    public Mindlog() {
    }

}
