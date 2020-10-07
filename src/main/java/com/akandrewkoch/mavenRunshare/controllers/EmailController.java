package com.akandrewkoch.mavenRunshare.controllers;

import com.akandrewkoch.mavenRunshare.models.JavaEmail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;


@Controller
@RequestMapping("/email")
public class EmailController extends MainController{

    @GetMapping("")
    public String displayPasswordResetPage (HttpServletRequest request, HttpSession session, Model model){
        setRunnerInModel(request, model);

        return "email/index";
    }

    @PostMapping("")
    public String processPasswordResetPage (){

        JavaEmail javaEmail= new JavaEmail();
        javaEmail.sendEmail("akandrewkoch@gmail.com", "rakochcoding@gmail.com", "test", "test");
        return "redirect:/";
    }
}
