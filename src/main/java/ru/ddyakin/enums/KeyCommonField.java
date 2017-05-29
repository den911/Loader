package ru.ddyakin.enums;

public enum KeyCommonField {
    ID(0), TIMESTAMP(1);

    KeyCommonField(int id) {
        this.id = id;
    }
    public final int id;
}
