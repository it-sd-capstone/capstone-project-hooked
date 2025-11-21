package edu.cvtc.hooked.model;

import java.util.Objects;

public class Location {

    private Integer locationId;      // null until saved
    private String locationName;
    private String state;           // 2 letter code, for example "WI"

    public Location() {
        // default ctor
    }

    // Full constructor (used when loading from DB)
    public Location(Integer locationId,
                    String locationName,
                    String state) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.state = state;
    }

    // Convenience constructor for new locations (id assigned by DB)
    public Location(String locationName,
                    String state) {
        this(null, locationName, state);
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
        this.locationName = locationName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
                + ", state='" + state + '\'' + '}';
    }
}





