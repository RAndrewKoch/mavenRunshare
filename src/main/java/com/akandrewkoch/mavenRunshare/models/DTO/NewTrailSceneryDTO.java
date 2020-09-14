package com.akandrewkoch.mavenRunshare.models.DTO;

import com.akandrewkoch.mavenRunshare.models.enums.TrailScenery;

public class NewTrailSceneryDTO {

    private TrailScenery trailScenery;

    public NewTrailSceneryDTO(){};

    public TrailScenery getTrailScenery() { return trailScenery; }

    public void setTrailScenery(TrailScenery trailScenery) { this.trailScenery = trailScenery; }
}
