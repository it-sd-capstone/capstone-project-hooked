package edu.cvtc.hooked.model;

import java.util.Map;

public class SpeciesRestrictions {

    private double maxWeight;
    private double maxLength;

    public SpeciesRestrictions(double maxWeight, double maxLength) {
        this.maxWeight = maxWeight;
        this.maxLength = maxLength;
    }

    public double getMaxWeight() {return maxWeight;}
    public void setMaxWeight(double maxWeight) {this.maxWeight = maxWeight;}
    public double getMaxLength() {return maxLength;}
    public void setMaxLength(double maxLength) {this.maxLength = maxLength;}

    // sources:
    // https://dnr.wisconsin.gov/topic/Fishing/recordfish/hookline
     // https://www.dnr.state.mn.us/fishing/certified-weight.html


    public static final Map<String, SpeciesRestrictions> ALL = Map.ofEntries(
            Map.entry("bighead carp", new SpeciesRestrictions(68.0, 50.0)),
            Map.entry("bigmouth buffalo", new SpeciesRestrictions(77.0, 50.0)),
            Map.entry("black bullhead", new SpeciesRestrictions(6.0, 22.0)),
            Map.entry("black crappie", new SpeciesRestrictions(5.0, 20.0)),
            Map.entry("bluegill", new SpeciesRestrictions(3.0, 12.0)),
            Map.entry("bowfin", new SpeciesRestrictions(14.0, 32.0)),
            Map.entry("brooke trout", new SpeciesRestrictions(11.0, 25.0)),
            Map.entry("brown bullhead", new SpeciesRestrictions(5.0, 18.0)),
            Map.entry("brown trout", new SpeciesRestrictions(42.0, 41.0)),
            Map.entry("burbot", new SpeciesRestrictions(19.0, 38.0)),
            Map.entry("channel catfish", new SpeciesRestrictions(44.0, 50.0)),
            Map.entry("common carp", new SpeciesRestrictions(58.0, 50.0)),
            Map.entry("flathead catfish", new SpeciesRestrictions(75.0, 53.0)),
            Map.entry("freshwater drum", new SpeciesRestrictions(36.0, 38.0)),
            Map.entry("golden redhorse", new SpeciesRestrictions(6.0, 24.0)),
            Map.entry("greater redhorse", new SpeciesRestrictions(11.0, 29.0)),
            Map.entry("lake sturgeon", new SpeciesRestrictions(171.0, 80.0)),
            Map.entry("lake trout", new SpeciesRestrictions(50.0, 45.0)),
            Map.entry("largemouth bass", new SpeciesRestrictions(12.0, 24.0)),
            Map.entry("longnose gar", new SpeciesRestrictions(22.0, 53.0)),
            Map.entry("muskellunge", new SpeciesRestrictions(70.0, 64.0)),
            Map.entry("northern pike", new SpeciesRestrictions(38.0, 46.0)),
            Map.entry("pumpkinseed", new SpeciesRestrictions(2.0, 12.0)),
            Map.entry("rock bass", new SpeciesRestrictions(3.0, 14.0)),
            Map.entry("sauger", new SpeciesRestrictions(7.0, 24.0)),
            Map.entry("shorthead redhorse", new SpeciesRestrictions(5.0, 24.0)),
            Map.entry("shortnose gar", new SpeciesRestrictions(5.0, 32.0)),
            Map.entry("shovelnose sturgeon", new SpeciesRestrictions(8.0, 40.0)),
            Map.entry("silver redhorse", new SpeciesRestrictions(12.0, 30.0)),
            Map.entry("walleye", new SpeciesRestrictions(19.0, 30.0)),
            Map.entry("longnose sucker", new SpeciesRestrictions(5.0, 22.0)),
            Map.entry("northern hog sucker", new SpeciesRestrictions(2.0, 17.0)),
            Map.entry("sucker spotted", new SpeciesRestrictions(5.0, 22.0))
    );

}

