package com.mindbridge.server.model;

import com.mindbridge.server.converter.StringListConverter;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Entity
public class Mindlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 날짜
    @Column(columnDefinition = "TEXT NOT NULL")
    private Date date;

    // 작성 시간
    @Column(columnDefinition = "TEXT NOT NULL")
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

    // default 생성자
    public Mindlog() {
    }

    public Mindlog(Long id, Date date, LocalTime time, List<String> moods, int moodColor, String title, String emotionRecord, String eventRecord, String questionRecord) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.moods = moods;
        this.moodColor = moodColor;
        this.title = title;
        this.emotionRecord = emotionRecord;
        this.eventRecord = eventRecord;
        this.questionRecord = questionRecord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<String> getMoods() {
        return moods;
    }

    public void setMoods(List<String> moods) {
        this.moods = moods;
    }

    public int getMoodColor() {
        return moodColor;
    }

    public void setMoodColor(int moodColor) {
        this.moodColor = moodColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmotionRecord() {
        return emotionRecord;
    }

    public void setEmotionRecord(String emotionRecord) {
        this.emotionRecord = emotionRecord;
    }

    public String getEventRecord() {
        return eventRecord;
    }

    public void setEventRecord(String eventRecord) {
        this.eventRecord = eventRecord;
    }

    public String getQuestionRecord() {
        return questionRecord;
    }

    public void setQuestionRecord(String questionRecord) {
        this.questionRecord = questionRecord;
    }
}
