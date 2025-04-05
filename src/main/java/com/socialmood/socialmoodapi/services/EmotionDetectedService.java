package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.dto.EmotionDTO;
import com.socialmood.socialmoodapi.entitys.EmotionDetected;
import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.enums.Emotion;
import com.socialmood.socialmoodapi.repositorys.IEmotionDetected;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class EmotionDetectedService {
    @Autowired
    private IEmotionDetected emotionDetectedRepo;

    public Boolean saveEmotions(Session session, User user, List<EmotionDTO> emotions) {
        try {
            if (!emotions.isEmpty()) {
                for (EmotionDTO emotionDto : emotions) {
                    EmotionDetected emotionDetected = new EmotionDetected();
                    emotionDetected.setUser(user);
                    emotionDetected.setSessao(session);
                    emotionDetected.setEmocao(Emotion.getEmotion(emotionDto.emotion()));
                    emotionDetected.setDataDeteccao(Date.from(emotionDto.time()));
                    emotionDetectedRepo.save(emotionDetected);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Não foi possível salvar as emoções");
        }
        return false;
    }

}
