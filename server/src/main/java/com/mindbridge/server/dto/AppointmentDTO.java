package com.mindbridge.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.sql.Date;

@Getter
@Setter
@ToString
public class AppointmentDTO {

    private Long id;
    private Date date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String doctorName;
    private String hospital;
    private String memo;
    private Long recordId;
}
