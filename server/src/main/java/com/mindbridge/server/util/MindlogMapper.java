package com.mindbridge.server.util;

import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Mindlog;
import org.springframework.stereotype.Component;

@Component
public class MindlogMapper {

    // Nindlog -> MindlogDTO
    public MindlogDTO toDTO(Mindlog mindlog) {

        MindlogDTO mindlogDTO = new MindlogDTO();

        mindlogDTO.setId(mindlog.getId());
        mindlogDTO.setDate(mindlog.getDate());
        mindlogDTO.setTime(mindlog.getTime());
        mindlogDTO.setMoodColor(mindlog.getMoodColor());
        mindlogDTO.setMoods(mindlog.getMoods()); // 리스트를 직접 할당
        mindlogDTO.setTitle(mindlog.getTitle());
        mindlogDTO.setEmotionRecord(mindlog.getEmotionRecord());
        mindlogDTO.setEventRecord(mindlog.getEventRecord());
        mindlogDTO.setQuestionRecord(mindlog.getQuestionRecord());

        return mindlogDTO;
    }

    // MindlogDTO -> Mindlog
    public Mindlog toEntity(MindlogDTO mindlogDTO) {

        Mindlog mindlog = new Mindlog();

        mindlog.setId(mindlogDTO.getId());
        mindlog.setDate(mindlogDTO.getDate());
        mindlog.setTime(mindlogDTO.getTime());
        mindlog.setMoods(mindlogDTO.getMoods());
        mindlog.setMoodColor(mindlogDTO.getMoodColor());
        mindlog.setTitle(mindlogDTO.getTitle());
        mindlog.setEmotionRecord(mindlogDTO.getEmotionRecord());
        mindlog.setEventRecord(mindlogDTO.getEventRecord());
        mindlog.setQuestionRecord(mindlogDTO.getQuestionRecord());

        return mindlog;

    }
}
