package com.akandrewkoch.mavenRunshare.models.DTO;

public class NewFriendRequestDTO {

    private int runner;

    private int friend;

    public NewFriendRequestDTO(){};

    public NewFriendRequestDTO(int runner, int friend){
        this.runner = runner;
        this.friend = friend;
    }

    public int getRunner() {
        return runner;
    }

    public void setRunner(int runner) {
        this.runner = runner;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }
}
