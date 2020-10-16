package com.akandrewkoch.mavenRunshare.models.enums;

public enum SecondsAndMinutes {

    ZERO("Zero", "00", 0),
    ONE("One","01", 1),
    TWO("Two", "02", 2),
    THREE("Three","03", 3),
    FOUR("Four","04", 4),
    FIVE("Five","05", 5),
    SIX("Six","06",6),
    SEVEN("Seven","07", 7),
    EIGHT("Eight","08", 8),
    NINE("Nine","09", 9),
    TEN("Ten","10", 10),
    ELEVEN("Eleven","11", 11),
    TWELVE("Twelve","12",12),
    THIRTEEN("Thirteen", "13", 13),
    FOURTEEN("Fourteen","14",14),
    FIFTEEN("Fifteen", "15", 15),
    SIXTEEN("Sixteen", "16", 16),
    SEVENTEEN("Seventeen", "17", 17),
    EIGHTEEN("Eighteen", "18", 18),
    NINETEEN("Nineteen", "19", 19),
    TWENTY("Twenty", "20", 20),
    TWENTYONE("Twenty-one", "21", 21),
    TWENTYTWO("Twenty-two", "22", 22),
    TWENTYTHREE("Twenty-three", "23", 23),
    TWENTYFOUR("Twenty-four", "24", 24),
    TWENTYFIVE("Twenty-five", "25", 25),
    TWENTYSIX("Twenty-six", "26", 26),
    TWENTYSEVEN("Twenty-seven", "27", 27),
    TWENTYEIGHT("Twenty-eight", "28", 28),
    TWENTYNINE("Twenty-nine", "29", 29),
    THIRTY("Thirty", "30", 30),
    THIRTYONE("Thirty-one", "31", 31),
    THIRTYTWO("Thirty-two", "32", 32),
    THIRTYTHREE("Thirty-three", "33", 33),
    THIRTYFOUR("Thrity-four", "34", 34),
    THIRTYFIVE("Thirty-five", "35", 35),
    THRITYSIX("Thirty-six", "36", 36),
    THIRTYSEVEN("Thirty-seven", "37", 37),
    THIRTYEIGHT("Thirty-eight", "38", 38),
    THIRTYNINE("Thirty-nine", "39", 39),
    FORTY("Forty", "40", 40),
    FORTYONE("Forty-one", "41", 40),
    FORTYTWO("Forty-two", "42", 42),
    FORTYTHREE("Forty-three", "43", 43),
    FORTYFOUR("Forty-four", "44", 44),
    FORTYFIVE("Forty-five", "45", 45),
    FORTYSIX("Forty-Six", "46", 46),
    FORTYSEVEN("Forty-Seven", "47", 47),
    FORTYEIGHT("Forty-Eight", "48", 48),
    FORTYNINE("Forty-Nine", "49", 49),
    FIFTY("Fifty", "50", 50),
    FIFTYONE("Fifty-one", "51", 51),
    FIFTYTWO("Fifty-two", "52", 52),
    FIFTYTHREE("Fifty-three", "53", 53),
    FIFTYFOUR("Fifty-four", "54", 54),
    FIFTYFIVE("Fifty-five", "55", 55),
    FIFTYSIX("Fifty-six", "56", 56),
    FIFTYSEVEN("Fifty-seven", "57", 57),
    FIFTYEIGHT("Fifty-eight", "58", 59),
    FIFTYNINE("Fifty-nine", "59", 59);

    private final String numberStringName;
    private final String numberStringNumber;
    private final Integer numberIntegerNumber;

    SecondsAndMinutes(String numberStringName, String numberStringNumber, Integer numberIntegerNumber){
        this.numberStringName = numberStringName;
        this.numberStringNumber = numberStringNumber;
        this.numberIntegerNumber  = numberIntegerNumber;
    }

    public String getNumberStringName() {
        return numberStringName;
    }

    public String getNumberStringNumber() {
        return numberStringNumber;
    }

    public Integer getNumberIntegerNumber() {
        return numberIntegerNumber;
    }
}
