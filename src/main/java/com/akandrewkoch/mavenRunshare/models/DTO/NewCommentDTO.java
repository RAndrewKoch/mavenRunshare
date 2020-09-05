package com.akandrewkoch.mavenRunshare.models.DTO;



import com.akandrewkoch.mavenRunshare.models.RunSession;
import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.Trail;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class NewCommentDTO {

    @NotBlank(message="Message title required")
    @Size(min=3, max=50, message="Sorry, message titles must be kept between 3 and 50 characters")
    private String messageTitle;

    @NotBlank(message="Message required")
    @Size(max=10000, message="Sorry, messages must be kept under 10000 characters")
    private String message;

    private Trail trail;

    private RunSession runSession;

    public List<Runner> runners = new ArrayList<>();

    private Boolean privateMessage;

    public NewCommentDTO(){}

    public NewCommentDTO(String messageTitle, String message, Trail trail, RunSession runSession, Boolean privateMessage){
        this.messageTitle=messageTitle;
        this.message=message;
        this.trail=trail;
        this.runSession=runSession;
        this.privateMessage=privateMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageTitle() { return messageTitle; }

    public Trail getTrail() { return trail; }

    public RunSession getRunSession() { return runSession; }

    public List<Runner> getRunners() { return runners; }

    public Boolean getPrivateMessage() { return privateMessage; }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageTitle(String messageTitle) { this.messageTitle = messageTitle; }

    public void setTrail(Trail trail) { this.trail = trail; }

    public void setRunSession(RunSession runSession) { this.runSession = runSession; }

    public void setPrivateMessage(Boolean privateMessage) { this.privateMessage = privateMessage; }
}
