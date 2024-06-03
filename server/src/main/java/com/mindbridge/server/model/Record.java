package com.mindbridge.server.model;

import com.mindbridge.server.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 파일 위치
    @Column(columnDefinition = "TEXT NOT NULL")
    private String filePath;

    // 요약 내용 추가
    private String summary;

    // appointment 연관 관계 매핑
    @OneToOne(mappedBy = "record", cascade = CascadeType.ALL)
    private Appointment appointment;

    // default 생성자
    public Record() {
    }

    public Record(Long id, String filePath, String summary) {
        this.id = id;
        this.filePath = filePath;
        this.summary = summary;
    }

}
