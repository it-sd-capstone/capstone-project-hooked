package edu.cvtc.hooked.model;

import java.util.Objects;

public class Catch {
    private Integer catchId;     // null until saved
    private Integer userId;
    private Integer speciesId;
    private Integer locationId;
    private Integer baitId;
    private String dateCaught;   // store as ISO string "YYYY-MM-DD"
    private String notes;

    public Catch() {}

    public Catch(Integer catchId,
                 Integer userId,
                 Integer speciesId,
                 Integer locationId,
                 Integer baitId,
                 String dateCaught,
                 String notes) {
        this.catchId = catchId;
        this.userId = userId;
        this.speciesId = speciesId;
        this.locationId = locationId;
        this.baitId = baitId;
        this.dateCaught = dateCaught;
        this.notes = notes;
    }

    // convenience ctor for new catches (id assigned by DB)
    public Catch(Integer userId,
                 Integer speciesId,
                 Integer locationId,
                 Integer baitId,
                 String dateCaught,
                 String notes) {
        this(null, userId, speciesId, locationId, baitId, dateCaught, notes);
    }

    // getters/setters
    public Integer getCatchId() { return catchId; }
    public void setCatchId(Integer catchId) { this.catchId = catchId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getSpeciesId() { return speciesId; }
    public void setSpeciesId(Integer speciesId) { this.speciesId = speciesId; }

    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }

    public Integer getBaitId() { return baitId; }
    public void setBaitId(Integer baitId) { this.baitId = baitId; }

    public String getDateCaught() { return dateCaught; }
    public void setDateCaught(String dateCaught) { this.dateCaught = dateCaught; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Catch)) return false;
        Catch c = (Catch) o;

        // CatchID is the stable key once saved
        return Objects.equals(catchId, c.catchId);
    }

    @Override public int hashCode() {
        return Objects.hash(catchId);
    }

    @Override public String toString() {
        return "Catch{catchId=" + catchId +
                ", userId=" + userId +
                ", speciesId=" + speciesId +
                ", locationId=" + locationId +
                ", baitId=" + baitId +
                ", dateCaught='" + dateCaught + "'}";
    }
}