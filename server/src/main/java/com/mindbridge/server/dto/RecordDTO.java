package com.mindbridge.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
public class RecordDTO {

    private Long id;
    private String filePath;
    private String summary;
}
