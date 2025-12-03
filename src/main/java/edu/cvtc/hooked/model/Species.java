package edu.cvtc.hooked.model;

public class Species {
    private Integer speciesId;
    private String speciesName;
    private double maxLength;
    private double maxWeight;
    private Integer createdByUserId;  // nullable

    public Species() {}

    public Species(Integer speciesId, String speciesName,
                   double maxLength, double maxWeight,
                   Integer createdByUserId) {
        this.speciesId = speciesId;
        this.speciesName = speciesName;
        this.maxLength = maxLength;
        this.maxWeight = maxWeight;
        this.createdByUserId = createdByUserId;
    }

    public Species(String speciesName, double maxLength, double maxWeight, Integer createdByUserId) {
        this(null, speciesName, maxLength, maxWeight, createdByUserId);
    }

    public Integer getSpeciesId() { return speciesId; }
    public void setSpeciesId(Integer speciesId) { this.speciesId = speciesId; }

    public String getSpeciesName() { return speciesName; }
    public void setSpeciesName(String speciesName) { this.speciesName = speciesName; }

    public double getMaxLength() { return maxLength; }
    public void setMaxLength(double maxLength) { this.maxLength = maxLength; }

    public double getMaxWeight() { return maxWeight; }
    public void setMaxWeight(double maxWeight) { this.maxWeight = maxWeight; }

    public Integer getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Integer createdByUserId) { this.createdByUserId = createdByUserId; }
}
