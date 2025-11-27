package edu.cvtc.hooked.model;

import java.util.Objects;

public class Species {

    private Integer speciesId;
    private String speciesName;
    private Double minLength;
    private Double maxLength;
    private Double minWeight;
    private Double maxWeight;

    public Species() {
        // default ctor
    }

    // full ctor (used when loading from DB)
    public Species(Integer speciesId,
                   String speciesName,
                   Double minLength,
                   Double maxLength,
                   Double minWeight,
                   Double maxWeight) {
        this.speciesId = speciesId;
        this.speciesName = speciesName;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    // convenience ctor for new species (id assigned by DB)
    public Species(String speciesName,
                   Double minLength,
                   Double maxLength,
                   Double minWeight,
                   Double maxWeight) {
        this(null, speciesName, minLength, maxLength, minWeight, maxWeight);
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

    public Double getMinLength() {
        return minLength;
    }

    public void setMinLength(Double minLength) {
        this.minLength = minLength;
    }

    public Double getMaxLength() {return maxLength;}

    public void setMaxLength(Double maxLength) {
        this.maxLength = maxLength;}

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMaxWeight() {return maxWeight;}

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
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

