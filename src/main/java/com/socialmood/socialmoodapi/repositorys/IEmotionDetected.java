package com.socialmood.socialmoodapi.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmotionDetected extends JpaRepository<com.socialmood.socialmoodapi.entitys.EmotionDetected, Long> {

}
