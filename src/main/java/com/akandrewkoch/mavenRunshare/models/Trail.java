package com.akandrewkoch.mavenRunshare.models;


import com.akandrewkoch.mavenRunshare.models.staticMethods.DistanceConversion;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Trail extends AbstractEntity {

    @NotBlank(message="Trail name required")
    private String name;

    private double miles;

    private double kilometers;

    private String address;

    @Size(max=5, min=5, message="Must use 5-digit zip code")
    private String zipCode;

    private Integer numberZipCode;

    @OneToMany(mappedBy="trail")
    private final List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy="trail")
    private final List<TrailDifficultyRating> trailDifficultyRatings = new ArrayList<>();

    public Trail (String name, double miles, String address, String zipCode ){
        this.name= Jsoup.clean(name, Whitelist.none());
        this.miles=miles;
        this.kilometers= DistanceConversion.milesToKilometers(miles);
        this.address= Jsoup.clean(address, Whitelist.none());
        this.zipCode= Jsoup.clean(zipCode, Whitelist.none());
        this.numberZipCode= Integer.parseInt(zipCode);
    }

    public Trail (){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public Integer getNumberZipCode() { return numberZipCode; }

    public void setNumberZipCode(Integer numberZipCode) { this.numberZipCode = numberZipCode; }

    public int returnTrailDifficultyAvg (List<TrailDifficultyRating> trailDifficultyList){
        double sum = 0;
        double count = 0;
        for (int i = 0; i<trailDifficultyList.size(); i++){
            if (trailDifficultyList.get(i).getTrail().getId().equals(this.getId())) {
                sum += trailDifficultyList.get(i).getDifficulty().getNumberLevel();
                count ++;
            }
        }
        if (count==0){
            return 6;
        }
        return (int) Math.round(sum/count);
    }

    public int returnNumberOfTrailDifficultyRatings (List<TrailDifficultyRating> trailDifficultyRatingList){
        int count =0;
        for (int i=0; i<trailDifficultyRatingList.size(); i++){
            if (trailDifficultyRatingList.get(i).getTrail().getId().equals(this.getId())) {
                count ++;
            }
        }
        return count;
    }

    public int returnTrailSceneryAvg (List<TrailSceneryRating> trailSceneryRatingList){
        double sum = 0;
        double count = 0;
        for (int i = 0; i<trailSceneryRatingList.size(); i++){
            if (trailSceneryRatingList.get(i).getTrail().getId().equals(this.getId())) {
                sum += trailSceneryRatingList.get(i).getScenery().getNumberLevel();
                count ++;
            }
        }
        if (count==0){
            return 6;
        }
        return (int) Math.round(sum/count);
    }

    public int returnNumberOfTrailSceneryRatings (List<TrailSceneryRating> trailSceneryRatingList){
        int count =0;
        for (int i=0; i<trailSceneryRatingList.size(); i++){
            if (trailSceneryRatingList.get(i).getTrail().getId().equals(this.getId())) {
                count ++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o)) return false;
        com.akandrewkoch.mavenRunshare.models.Trail trail = (com.akandrewkoch.mavenRunshare.models.Trail) o;
        return name.equals(trail.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
