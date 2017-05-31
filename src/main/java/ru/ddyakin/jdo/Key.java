package ru.ddyakin.jdo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Key {

    @JsonProperty("keyType")
    private String key;
    @JsonProperty("unique")
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
