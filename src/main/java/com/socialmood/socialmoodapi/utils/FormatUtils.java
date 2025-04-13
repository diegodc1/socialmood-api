package com.socialmood.socialmoodapi.utils;

public class FormatUtils {
    public static String formatEmocaoName(String emocao) {
        switch (emocao.toLowerCase()) {
            case "happy":
                return "Alegria";
            case "sad":
                return "Tristeza";
            case "fear":
                return "Medo";
            case "anger":
                return "raiva";
            case "neutral":
                return "Neutro";
            default:
                return "Indefinido";
        }
    }

    public static String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
