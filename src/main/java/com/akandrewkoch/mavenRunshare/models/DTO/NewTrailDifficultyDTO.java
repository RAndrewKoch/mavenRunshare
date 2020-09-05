package com.akandrewkoch.mavenRunshare.models.DTO;


import com.akandrewkoch.mavenRunshare.models.enums.TrailDifficulty;

public class NewTrailDifficultyDTO {

    private TrailDifficulty trailDifficulty;

    public NewTrailDifficultyDTO(){};

    public TrailDifficulty getTrailDifficulty() { return trailDifficulty; }

    public void setTrailDifficulty(TrailDifficulty trailDifficulty) { this.trailDifficulty = trailDifficulty; }
}
