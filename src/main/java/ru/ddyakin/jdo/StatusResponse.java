package ru.ddyakin.jdo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ddyakin on 31.05.17.
 */
public class StatusResponse {

    @JsonProperty("status")
    private String status;

    public StatusResponse() {
    }

    public StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
