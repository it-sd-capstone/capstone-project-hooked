package edu.cvtc.hooked.model;

import java.util.Objects;

public class Location {

    private Integer locationId;      // null until saved
    private String locationName;
    private String state;           // 2 letter code, for example "WI"
    private Integer createdByUserId; // nullable, same idea as Bait

    public Location() {
        // default ctor
    }

    // Full constructor (used when loading from DB)
    public Location(Integer locationId,
                    String locationName,
                    String state,
                    Integer createdByUserId) {
        this.locationId = locationId;
        setLocationName(locationName);
        setState(state);
        this.createdByUserId = createdByUserId;
    }

    // Convenience constructor for new locations (id assigned by DB)
    public Location(String locationName,
                    String state) {
        this(null, locationName, state, null);
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = formatTitle(locationName);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = formatState(state);
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    private String formatTitle(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();
    }

    private String formatState(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.trim().toUpperCase();
    }

    // Treat (locationName, state) as the logical key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location that = (Location) o;
        return Objects.equals(locationName, that.locationName)
                && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationName, state);
    }

    @Override
    public String toString() {
        return "Location{locationId=" + locationId
                + ", locationName='" + locationName + '\''
                + ", state='" + state + '\''
                + ", createdByUserId=" + createdByUserId
                + '}';
    }
}
