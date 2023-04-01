package com.hike.models;

public enum Dificultate {
    USOR, USOR_MEDIU, MEDIU, MEDIU_DIFICIL, DIFICIL;

    public static boolean contains(String test) {
        for (Dificultate d : Dificultate.values()) {
            if (d.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
