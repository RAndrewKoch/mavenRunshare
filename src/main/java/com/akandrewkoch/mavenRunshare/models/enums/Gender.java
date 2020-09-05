package com.akandrewkoch.mavenRunshare.models.enums;

public enum Gender {

    PREFERNOT("Prefer not to say"),
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String displayName;

    Gender(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
