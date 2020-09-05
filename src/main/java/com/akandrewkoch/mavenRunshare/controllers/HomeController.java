package com.akandrewkoch.mavenRunshare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HomeController extends MainController {
    private String displayIndexPage(Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Home");
        return "index.html";
    }
}
