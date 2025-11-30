package edu.cvtc.hooked.model;

public class SpeciesRequest {

    private Integer requestId;
    private String speciesName;
    private Integer userId;
    private String requestedAt; // store as text

    public Integer getRequestId() {
        return requestId;
    }
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getSpeciesName() {
        return speciesName;
    }
    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRequestedAt() {
        return requestedAt;
    }
    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }
}
