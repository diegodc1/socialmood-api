package com.socialmood.socialmoodapi.enums;

public enum Emotion {
    HAPPY("happy"),
    SAD("sad"),
    ANGRY("angry"),
    NEUTRAL("neutral"),
    SURPRISE("surprise"),
    FEAR("fear"),
    DISGUST("disust");

    Emotion(String emocao) {
        this.emocao = emocao;
    }

    private String emocao;


    public static Emotion getEmotion(String emotion) {
        for (Emotion e : Emotion.values()) {
            if (e.emocao.equalsIgnoreCase(emotion)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Emoção inválida: " + emotion);
    }
}
