package ru.ddyakin.jdo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Struct {

    @JsonProperty("name")
    private String spaceName;

    @JsonProperty("type")
    private String typeKey;

    @JsonProperty("keys")
    private ArrayList<Key> keys;

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public ArrayList<Key> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<Key> keys) {
        this.keys = keys;
    }

}
