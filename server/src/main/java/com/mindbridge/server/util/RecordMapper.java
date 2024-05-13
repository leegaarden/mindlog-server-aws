package com.mindbridge.server.util;

import com.mindbridge.server.dto.RecordDTO;
import com.mindbridge.server.model.Record;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {

    // Record -> RecordDTO
    public RecordDTO toDTO(Record record) {

        RecordDTO recordDTO = new RecordDTO();

        recordDTO.setId(record.getId());
        recordDTO.setFilePath(record.getFilePath());
        recordDTO.setSummary(record.getSummary());

        return recordDTO;
    }

    // RecordDTO -> Record
    public Record toEntity(RecordDTO recordDTO) {

        Record record = new Record();

        record.setId(recordDTO.getId());
        record.setFilePath(recordDTO.getFilePath());
        record.setSummary((recordDTO.getSummary()));

        return record;
    }
}
