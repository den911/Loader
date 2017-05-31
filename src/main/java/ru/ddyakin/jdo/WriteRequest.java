package ru.ddyakin.jdo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WriteRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("shards")
    private List<String> shards;

    @JsonProperty("fields")
    private List<String> fields;

    public WriteRequest() {
    }

    public WriteRequest(String name, List<String> shards, List<String> fields) {
        this.name = name;
        this.shards = shards;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getShards() {
        return shards;
    }

    public void setShards(List<String> shards) {
        this.shards = shards;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
