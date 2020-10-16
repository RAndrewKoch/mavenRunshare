package com.akandrewkoch.mavenRunshare.models.enums;

public enum Hours {

    ZERO("Zero", "0", 0),
    ONE("One","1", 1),
    TWO("Two", "2", 2),
    THREE("Three","3", 3),
    FOUR("Four","4", 4),
    FIVE("Five","5", 5),
    SIX("Six","6",6),
    SEVEN("Seven","7", 7),
    EIGHT("Eight","8", 8),
    NINE("Nine","9", 9),
    TEN("Ten","10", 10),
    ELEVEN("Eleven","11", 11),
    TWELVE("Twelve","12",12),
    THIRTEEN("Thirteeen", "13", 13),
    FOURTEEN("Fourteen","14",14),
    FIFTEEN("Fifteen", "15", 15),
    SIXTEEN("Sixteen", "16", 16),
    SEVENTEEN("Seventeen", "17", 17),
    EIGHTEEN("Eighteen", "18", 18),
    NINETEEN("Nineteen", "19", 19),
    TWENTY("Twenty", "20", 20),
    TWENTYONE("Twenty-one", "21", 21),
    TWENTYTWO("Twenty-two", "22", 22),
    TWENTYTHREE("Twenty-three", "23", 23);

    private final String numberStringName;
    private final String numberStringNumber;
    private final Integer numberIntegerNumber;

    Hours(String numberStringName, String numberStringNumber, Integer numberIntegerNumber){
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
