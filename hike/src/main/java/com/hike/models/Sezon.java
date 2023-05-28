package com.hike.models;

public enum Sezon {
    PRIMAVARA("Primăvară"),
    VARA("Vară"),
    TOAMNA("Toamnă"),
    IARNA("Iarnă"),
    TOATE_SEZOANELE("Toate sezoanele"),
    PRIMAVARA_VARA("Primăvară-Vară"),
    TOAMNA_IARNA("Toamnă-Iarnă"),
    INTERZIS_IARNA("Interzis iarna");

    public static boolean contains(String test) {
        for (Sezon s : Sezon.values()) {
            if (s.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    private String displayName;

    Sezon(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }

    // Optionally and/or additionally, toString.
    @Override public String toString() { return displayName; }
}
