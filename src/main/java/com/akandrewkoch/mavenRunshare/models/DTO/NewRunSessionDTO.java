package com.akandrewkoch.mavenRunshare.models.DTO;

import com.akandrewkoch.mavenRunshare.models.AbstractEntity;
import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.Trail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class NewRunSessionDTO extends AbstractEntity {

    @NotBlank(message="Run Session needs to be named")
    private String name;

    public List<Runner> runners = new ArrayList<>();

    @NotBlank(message="Must supply a date")
    private String date;

    @NotNull
    private Trail trail;

    @NotNull
    private double laps;

    private Integer seconds;
    private Integer minutes;
    private Integer hours;

    public NewRunSessionDTO (){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Runner> getRunners() { return runners; }

    public void setRunners(List<Runner> runners) { this.runners = runners; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Trail getTrail() { return trail; }

    public void setTrail(Trail trail) { this.trail = trail; }

    public double getLaps() { return laps; }

    public void setLaps(double laps) {
        this.laps = laps;
    }

    public Integer getSeconds() { return seconds; }

    public void setSeconds(Integer seconds) { this.seconds = seconds; }

    public Integer getMinutes() { return minutes; }

    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public Integer getHours() { return hours; }

    public void setHours(Integer hours) { this.hours = hours; }
}
