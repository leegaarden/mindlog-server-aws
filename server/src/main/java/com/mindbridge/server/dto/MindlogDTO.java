package com.mindbridge.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class MindlogDTO {

    private Long id;
    private Date date;
    private LocalTime time;
    private List<String> moods = new ArrayList<>(); // 감정을 List 형식으로 변경
    private int moodColor;
    private String title;
    private String emotionRecord;
    private String eventRecord;
    private String questionRecord;
}
