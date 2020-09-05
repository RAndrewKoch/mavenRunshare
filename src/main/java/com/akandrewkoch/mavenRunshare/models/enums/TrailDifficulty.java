package com.akandrewkoch.mavenRunshare.models.enums;

public enum TrailDifficulty {

    BEGINNER("Beginner", "Flat, short, padded, shaded, indoors, or otherwise easy", 1),
    NOVICE("Novice", "Slight hills, up to a mile, solid running surface, etc.", 2),
    AVERAGE("Average", "Some significant hills, half exposed, somewhat broken or rough surface", 3),
    CHALLENGING("Challenging", "Very few flat locations, almost no shade, gravel or uneven surfaces", 4),
    EXPERT("Expert", "All hills, no shade at all, dirt or unmarked trail", 5);

    private final String displayName;
    private final String description;
    private final Integer numberLevel;

    TrailDifficulty(String displayName, String description, Integer numberLevel){
        this.displayName=displayName;
        this.description=description;
        this.numberLevel=numberLevel;
    }

    public String getDisplayName() { return displayName; }

    public String getDescription() { return description; }

    public Integer getNumberLevel() { return numberLevel; }
}
