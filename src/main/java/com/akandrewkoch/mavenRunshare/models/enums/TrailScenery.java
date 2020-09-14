package com.akandrewkoch.mavenRunshare.models.enums;

public enum TrailScenery {

    TERRIBLE("Terrible", "Trash littered, polluted, or otherwise uncomfortable to use", 1),
    BELOWAVERAGE("Below Average", "Poorly maintained, no sight lines, crowded", 2),
    AVERAGE("Average", "Maintained adequately, somewhat green, or with decent scenery", 3),
    ABOVEAVERAGE("Above Average", "Well maintained, good looking greenery, or some interesting sights", 4),
    GORGEOUS("Gorgeous", "Meticulously maintained, manicured greenery, or majestic views", 5);

    private final String displayName;
    private final String description;
    private final int numberLevel;

    TrailScenery (String displayName, String description, int numberLevel){
        this.displayName=displayName;
        this.description=description;
        this.numberLevel=numberLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberLevel() {
        return numberLevel;
    }
}
