package com.akandrewkoch.mavenRunshare.models.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewPasswordDTO {

    @NotNull
    @NotBlank(message="Password is required")
    @Size(min=8, max=20,message="All Runner passwords are between 8 and 20 characters")
    private String password;

    @NotNull
    @NotBlank(message="Verification password is required")
    @Size(min=8, max=20,message="All Runner passwords are between 8 and 20 characters")
    private String verifyPassword;

    public NewPasswordDTO(){}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
