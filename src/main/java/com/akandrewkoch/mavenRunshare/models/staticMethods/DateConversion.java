package com.akandrewkoch.mavenRunshare.models.staticMethods;

public class DateConversion {

    public static String convertYYYYMMDDToDisplayString(String date){
        String[] splitDate = date.split("-");
        String month = "";
        String dayModifier ="";
        String day = "";
        switch (splitDate[1]){
            case "01":
                month="January";
                break;
            case "02":
                month="February";
                break;
            case "03":
                month="March";
                break;
            case "04":
                month="April";
                break;
            case "05":
                month="May";
                break;
            case "06":
                month="June";
                break;
            case "07":
                month="July";
                break;
            case "08":
                month="August";
                break;
            case "09":
                month="September";
                break;
            case "10":
                month="October";
                break;
            case "11":
                month ="November";
                break;
            case "12":
                month = "December";
                break;
        }
        switch (splitDate[2]){
            case "01": case "21":
                dayModifier ="st";
                break;
            case "02": case "22":
                dayModifier ="nd";
                break;
            case "03": case "23":
                dayModifier ="rd";
                break;
            default:
                dayModifier="th";
        }

        switch (splitDate[2]){
            case "01":
                day= "1";
                break;
            case "02":
                day = "2";
                break;
            case "03":
                day= "3";
                break;
            case "04":
                day = "4";
                break;
            case "05":
                day = "5";
                break;
            case "06":
                day = "6";
                break;
            case "07":
                day = "7";
                break;
            case "08":
                day = "8";
                break;
            case "09":
                day ="9";
                break;
            default:
                day = splitDate[2];
        }

        return month+" "+day+dayModifier+", "+splitDate[0];
    }
}
