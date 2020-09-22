package com.akandrewkoch.mavenRunshare.models;

import com.akandrewkoch.mavenRunshare.models.enums.Gender;
import com.akandrewkoch.mavenRunshare.models.enums.RunnerLevel;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Runner extends AbstractEntity{


//Fields
    @NotNull(message="Runner callsign is required")
    private String callsign;

    @Size(min=2, max=30, message="First name must be between 2 and 30 characters")
    private String firstName;

    @Size(min=2, max=30, message="Last Name must be between 2 and 30 characters")
    private String lastName;

    @NotNull
    private boolean callsignOnly;

    @NotNull
    private String pwHash;

    @NotNull(message="Age cannot be null")
    private int age;

    @Min(value = 0, message="Weight cannot be set lower than 0")
    private int weight;

    @NotNull(message="Gender cannot be null")
    private Gender gender;

    @NotNull(message="Runner level cannot be null")
    private RunnerLevel runnerLevel;

    @NotNull(message="Zip code cannot be null")
    private String zip;

    @OneToMany(mappedBy="creator")
    private final List<RunSession> runSessions= new ArrayList<>();

    @ManyToMany(mappedBy="runners")
    private final List<RunSession> runSessionPack = new ArrayList<>();

    private Integer numberZipCode;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @ManyToMany(mappedBy = "runners")
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "runner")
    private final List<TrailDifficultyRating> trailDifficultyRatings = new ArrayList<>();

    @ManyToMany
    private final List<Runner> friends = new ArrayList<>();

    @ManyToMany
    private final List<Runner> friendRequests = new ArrayList<>();


    //Constructors
    public Runner() {
    }

    public Runner (String callsign, String firstName, String lastName, Boolean callsignOnly, String password, int age, int weight, Gender gender, RunnerLevel runnerLevel, String zip){
        this.callsign= Jsoup.clean(callsign, Whitelist.none());
        this.firstName= Jsoup.clean(firstName, Whitelist.none());
        this.lastName= Jsoup.clean(lastName, Whitelist.none());
        this.callsignOnly=callsignOnly;
        this.pwHash= Jsoup.clean(encoder.encode(password), Whitelist.none());
        this.age=age;
        this.weight = weight;
        this.gender= gender;
        this.runnerLevel= runnerLevel;
        this.zip = Jsoup.clean(zip, Whitelist.basic());
        this.numberZipCode = Integer.parseInt(zip);
    }

//getters

    public String getCallsign() {
        return callsign;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isCallsignOnly() {
        return callsignOnly;
    }

    public String getPwHash() {
        return pwHash;
    }

    public Integer getAge() {
        return age;
    }

    public int getWeight() { return weight; }

    public Gender getGender() { return gender; }

    public RunnerLevel getRunnerLevel() { return runnerLevel; }

    public String getZip() { return zip; }

    public Integer getNumberZipCode() { return numberZipCode; }

    public List<Runner> getFriendRequests() { return friendRequests; }

    public List<Runner> getFriends() { return friends; }

    //setters

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCallsignOnly(boolean callsignOnly) {
        this.callsignOnly = callsignOnly;
    }

    public void setPassword(String password) {
        this.pwHash = encoder.encode(password);
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setRunningLevel(RunnerLevel runnerLevel) {
        this.runnerLevel = runnerLevel;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setNumberZipCode(Integer numberZipCode) { this.numberZipCode = numberZipCode; }

    public boolean isMatchingPassword(String password){
        return encoder.matches(password, pwHash);
    }

    public void addComment (Comment comment){this.comments.add(comment);}

    public void addFriendRequest (Runner runner) {this.friendRequests.add(runner);}

    public void addFriend (Runner runner) {this.friends.add(runner);}

    public void removeFriendRequest (Runner runner) {this.friendRequests.remove(runner);}

    public boolean isFriend (Runner runner) { return this.friends.contains(runner);}
}
