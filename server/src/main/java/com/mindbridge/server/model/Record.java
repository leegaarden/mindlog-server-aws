package com.mindbridge.server.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 파일 위치
    private String filePath;

    // 요약 내용 추가
    private String summary;

    // appointment 연관 관계 매핑
    @OneToOne(mappedBy = "record", fetch = FetchType.LAZY)
    private Appointment appointment;

    // default 생성자
    public Record() {
    }

    public Record(Long id, String filePath, String summary) {
        this.id = id;
        this.filePath = filePath;
        this.summary = summary;
    }

    // getter setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary (String summary) {
        this.summary = summary;
    }
}
