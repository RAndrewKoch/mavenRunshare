package com.akandrewkoch.mavenRunshare.models.DTO;

import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.staticMethods.DistanceConversion;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewTrailDTO {

    @NotBlank(message="Trail name required")
    private String name;

    @Min(value=0, message="Trail cannot be negative miles long")
    private double miles;

    private double kilometers;

    private String address;

    @Size(max=5, min=5, message="Must use 5-digit zip code")
    private String zipCode;

    public NewTrailDTO (String name, double miles, String address, String zipCode){
        this.name=name;
        this.miles=miles;
        this.kilometers= DistanceConversion.milesToKilometers(this.miles);
        this.address=address;
        this.zipCode=zipCode;
    }

    public NewTrailDTO(){}

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

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers() {
        this.kilometers = DistanceConversion.milesToKilometers(this.miles);
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

}
