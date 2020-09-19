package com.akandrewkoch.mavenRunshare.models;

import com.akandrewkoch.mavenRunshare.models.enums.TrailScenery;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class TrailSceneryRating extends AbstractEntity {

    private TrailScenery scenery;

    @ManyToOne
    private Runner runner;

    @ManyToOne
    private Trail trail;

    public TrailSceneryRating(TrailScenery scenery, Runner runner, Trail trail){
        this.scenery=scenery;
        this.runner=runner;
        this.trail=trail;
    }

    public TrailSceneryRating (){}

    public TrailScenery getScenery() { return scenery; }

    public void setScenery(TrailScenery scenery) { this.scenery = scenery; }

    public Runner getRunner() { return runner; }

    public void setRunner(Runner runner) { this.runner = runner; }

    public Trail getTrail() { return trail; }

    public void setTrail(Trail trail) { this.trail = trail; }
}
