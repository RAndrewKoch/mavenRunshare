package com.akandrewkoch.mavenRunshare.models.staticMethods;

import java.time.LocalTime;

public class TimeConversion{

    public static String displayTimeAsString (Integer time){
        Integer hours = time/3600;
        Integer minutes = (time-(hours*3600))/60;
        Integer seconds = (time%60);
        String minutesZero = "";
        String secondsZero = "";
        if (minutes<10){
            minutesZero="0";
        }
        if (seconds<10){
            secondsZero="0";
        }
        return hours+":"+minutesZero+minutes+":"+secondsZero+seconds;
    }

    public static String displayLocalTimeAsString(LocalTime localTime){
        String stringTime = localTime.toString();
        String[] splitTime = stringTime.split(":");
        Integer hours= Integer.parseInt(splitTime[0]);
        String minutes= splitTime[1];
        String ampm = "AM";
        if (hours>=13){
            hours = hours-12;
            ampm = "PM";
        }

        return hours+":"+minutes+" "+ampm;
    }
}
