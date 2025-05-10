package com.socialmood.socialmoodapi.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum ERedeSocial {
    TIKTOK(1, "TikTok"),
    REDDIT(2, "Reddit"),
    FACEBOOK(3, "Facebook"),
    XTWITTER(4, "X"),
    INSTAGRAM(5, "Instagram"),
    THREADS(6, "Threads");

    ERedeSocial(Integer codigo, String name) {
        this.codigo = codigo;
        this.name = name;
    }

    private Integer codigo;
    private String name;


    public static ERedeSocial getSocialName(Long socialCod) {
        for (ERedeSocial e : ERedeSocial.values()) {
            if (socialCod != null && socialCod.equals(Long.valueOf(e.codigo))) {
                return e;
            }
        }
        throw new IllegalArgumentException("Rede Social inválida: " + socialCod);
    }

    public static ERedeSocial getSocialIdByName(String socialName) {
        for (ERedeSocial e : ERedeSocial.values()) {
            if (socialName != null && socialName.equalsIgnoreCase(e.name)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Rede Social inválida: " + socialName);
    }
}
