package com.hike.models;

public enum Sezon {
    PRIMAVARA, VARA, TOAMNA, IARNA, TOATE_SEZOANELE, PRIMAVARA_VARA, TOAMNA_IARNA;

    public static boolean contains(String test) {
        for (Sezon s : Sezon.values()) {
            if (s.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
