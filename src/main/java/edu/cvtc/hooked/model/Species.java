package edu.cvtc.hooked.model;

import java.util.Objects;

public class Species {

    private Integer speciesId;
    private String speciesName;
    private Double length;
    private Double weight;

    public Species() {
        // default ctor
    }

    // full ctor (used when loading from DB)
    public Species(Integer speciesId,
                   String speciesName,
                   Double length,
                   Double weight) {
        this.speciesId = speciesId;
        this.speciesName = speciesName;
        this.length = length;
        this.weight = weight;
    }

    // convenience ctor for new species (id assigned by DB)
    public Species(String speciesName,
                   Double length,
                   Double weight) {
        this(null, speciesName, length, weight);
    }

    public Integer getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    // treat speciesName as the unique key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Species)) return false;
        Species s = (Species) o;
        return Objects.equals(speciesName, s.speciesName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speciesName);
    }

    @Override
    public String toString() {
        return "Species{speciesId=" + speciesId
                + ", speciesName='" + speciesName + "'}";
    }
}

