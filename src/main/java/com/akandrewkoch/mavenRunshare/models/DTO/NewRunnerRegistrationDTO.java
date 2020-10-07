package com.akandrewkoch.mavenRunshare.models.DTO;



import com.akandrewkoch.mavenRunshare.models.enums.Gender;
import com.akandrewkoch.mavenRunshare.models.enums.RunnerLevel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class NewRunnerRegistrationDTO extends RunnerLoginDTO {

    @NotNull
    @NotBlank(message="First name is required.  If you do not wish your name to be displayed, please check the 'Callsign only' box below")
    @Size(min=2, max=30, message="First name must be 2-30 characters")
    private String firstName;

    @NotNull
    @NotBlank(message="Last name is required.  If you do not wish your name to be displayed, please check the 'Callsign only' box below")
    @Size(min=2, max=30, message="Last name must be 2-30 characters")
    private String lastName;

    @NotNull
    private boolean callsignOnly;

    @NotNull
    @NotBlank(message="Verification password is required")
    @Size(min=8, max=20,message="All Runner passwords are between 8 and 20 characters")
    private String verifyPassword;

    @NotNull(message="Age required for tracking")
    private int age;

    @NotNull(message="Weight cannot be null")
    private int weight;

    @NotNull(message="Gender cannot be null")
    private Gender gender;

    @NotNull(message="Running level cannot be null")
    private RunnerLevel runnerLevel;

    @NotBlank(message="Zip Code cannot be blank")
    @NotNull(message="Zip Code cannot be null")
    @Size(min=5, max=5, message="Must use 5-digit zip code")
    private String zip;

    @Email(message="Email provided must be valid")
    private String email;

    public NewRunnerRegistrationDTO(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isCallsignOnly() {
        return callsignOnly;
    }

    public void setCallsignOnly(boolean callsignOnly) {
        this.callsignOnly = callsignOnly;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public RunnerLevel getRunnerLevel() {
        return runnerLevel;
    }

    public void setRunnerLevel(RunnerLevel runningLevel) {
        this.runnerLevel = runningLevel;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {return email; }

    public void setEmail(String email) {this.email = email; }
}
