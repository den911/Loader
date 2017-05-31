package ru.ddyakin.jdo;

public class Key {

    private String key;
    private Boolean unique;

    public Key() {
    }

    public Key(String key, Boolean unique) {
        this.key = key;
        this.unique = unique;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
