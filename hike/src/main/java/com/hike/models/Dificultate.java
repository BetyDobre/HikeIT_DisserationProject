package com.hike.models;

public enum Dificultate {
    USOR("USOR"),
    USOR_MEDIU("USOR-MEDIU"),
    MEDIU("MEDIU"),
    MEDIU_DIFICIL("MEDIU-DIFICIL"),
    DIFICIL("DIFICIL");

    private String displayName;

    Dificultate(){};

    Dificultate(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }

    public static boolean contains(String test) {
        for (Dificultate d : Dificultate.values()) {
            if (d.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
