package ru.ddyakin.logic;

public class NodeInspector {

    private Integer number;
    private Long timestamp;

    private NodeInspector previousInspector = null;

    public NodeInspector(Integer number) {
        this.number = number;
        this.timestamp = System.currentTimeMillis();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public NodeInspector getPreviousInspector() {
        return previousInspector;
    }

    public void setPreviousInspector(NodeInspector previousInspector) {
        this.previousInspector = previousInspector;
    }
}
