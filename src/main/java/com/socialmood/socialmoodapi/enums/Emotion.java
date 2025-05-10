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

    public static Emotion fromPortuguese(String emotionPt) {
        if (emotionPt == null) {
            throw new IllegalArgumentException("Emoção não pode ser nula");
        }

        switch (emotionPt.trim().toLowerCase()) {
            case "alegria": return HAPPY;
            case "tristeza": return SAD;
            case "raiva": return ANGRY;
            case "neutro": return NEUTRAL;
            case "supresa": return SURPRISE;
            case "medo": return FEAR;
            default:
                throw new IllegalArgumentException("Emoção inválida: " + emotionPt);
        }
    }

}
