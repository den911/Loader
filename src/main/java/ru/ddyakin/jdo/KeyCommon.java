package ru.ddyakin.jdo;

public class KeyCommon {
    private Integer id;
    private Long timestamp;

    public KeyCommon() {
    }

    public KeyCommon(Integer id, Long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
