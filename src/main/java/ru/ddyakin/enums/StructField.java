package ru.ddyakin.enums;

public enum StructField {
    ID(0), NAME(1), STATUS(2), RECORDS(3);
    StructField(int id) {
        this.id = id;
    }
    public final int id;
}
