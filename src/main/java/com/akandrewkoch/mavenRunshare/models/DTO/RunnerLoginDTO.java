package com.akandrewkoch.mavenRunshare.models.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RunnerLoginDTO {

    @NotNull
    @NotBlank(message="A Callsign is required.")
    @Size(min=2, max=30, message="All Runner callsigns are between 2 and 30 characters")
    private String callsign;

    @NotNull
    @NotBlank(message="Password is required")
    @Size(min=8, max=20, message="All Runner passwords are between 8 and 20 characters")
    private String password;

    public RunnerLoginDTO(){};


    public String getCallsign() { return callsign; }
    public void setCallsign(String callsign) { this.callsign = callsign; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password;}
}
