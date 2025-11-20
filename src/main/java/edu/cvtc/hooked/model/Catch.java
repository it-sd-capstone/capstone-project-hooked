package edu.cvtc.hooked.model;

import java.util.Objects;

public class Catch {
    private Integer catchId;       // null until saved
    private Integer userId;
    private String speciesName;    // ID referring to species
    private String locationName;   // ID referring to location
    private String  baitType;       // ID referring to bait
    private String dateCaught;      // store as ISO string "YYYY-MM-DD"
    private String notes;
    private double length;
    private double weight;

    public Catch() {}

    // Full constructor
    public Catch(Integer catchId,
                 Integer userId,
                 String speciesName,
                 String locationName,
                 String baitType,
                 String dateCaught,
                 String notes,
                 double length,
                 double weight) {
        this.catchId = catchId;
        this.userId = userId;
        this.speciesName = speciesName;
        this.locationName = locationName;
        this.baitType = baitType;
        this.dateCaught = dateCaught;
        this.notes = notes;
        this.length = length;
        this.weight = weight;
    }

    // Constructor for new catches (ID assigned by DB)
    public Catch(Integer userId,
                 String speciesName,
                 String locationName,
                 String baitType,
                 String dateCaught,
                 String notes,
                 double length,
                 double weight) {
        this(null, userId, speciesName, locationName, baitType, dateCaught, notes, length, weight);
    }

    // Getters and setters
    public Integer getCatchId() { return catchId; }
    public void setCatchId(Integer catchId) { this.catchId = catchId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getSpeciesName() { return speciesName; }
    public void setSpeciesName(String speciesName) { this.speciesName = speciesName; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getBaitType() { return baitType; }
    public void setBaitType(String baitType) { this.baitType = baitType; }

    public String getDateCaught() { return dateCaught; }
    public void setDateCaught(String dateCaught) { this.dateCaught = dateCaught; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Catch)) return false;
        Catch c = (Catch) o;
        return Objects.equals(catchId, c.catchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catchId);
    }

    @Override
    public String toString() {
        return "Catch{" +
                "catchId=" + catchId +
                ", userId=" + userId +
                ", speciesName=" + speciesName +
                ", locationName=" + locationName +
                ", baitType=" + baitType +
                ", dateCaught='" + dateCaught + '\'' +
                ", notes='" + notes + '\'' +
                ", length=" + length +
                ", weight=" + weight +
                '}';
    }
}
