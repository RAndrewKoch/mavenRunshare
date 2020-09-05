package com.akandrewkoch.mavenRunshare.models.enums;

public enum RunnerLevel {

    BEGINNER("Beginner", "I can run less than a mile"),
    NOVICE("Novice", "I can run a mile"),
    HOBBY("Hobby", "I can run a 5k"),
    ATHLETIC("Athletic", "I run 5k's regularly"),
    STRONG("Strong", "I run 10K's regularly"),
    VERYSTRONG("Very Strong","I can run half marathons (~13 miles)"),
    MARATHON("Marathon", "I can run a marathon(~26 miles)"),
    SUPERSTAR("Superstar", "I run marathons regularly");

    private final String displayName;
    private final String description;

    RunnerLevel(String displayName, String description){
        this.displayName=displayName;
        this.description=description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
