package com.mindbridge.server.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private List<MindlogDTO> mindlogDTOs = new ArrayList<>();

}
