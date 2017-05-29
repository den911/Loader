package ru.ddyakin.jdo;

public class Structure {
    public Integer id;
    public String name;
    public String status;
    public Long recordsNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRecordsNumber() {
        return recordsNumber;
    }

    public void setRecordsNumber(Long recordsNumber) {
        this.recordsNumber = recordsNumber;
    }
}
