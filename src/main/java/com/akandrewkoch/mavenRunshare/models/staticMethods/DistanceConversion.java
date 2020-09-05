package com.akandrewkoch.mavenRunshare.models.staticMethods;

public class DistanceConversion {

    public static double milesToKilometers (double miles){
        return (Math.floor((miles*1.609344)*10)/10);
    }

    public static double kilometersToMiles (double kilometers){
        return (Math.floor((kilometers*.6214)*10)/10);
    }
}
