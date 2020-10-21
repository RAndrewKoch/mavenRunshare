package com.akandrewkoch.mavenRunshare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("")
public class HomeController extends MainController {

    @GetMapping
    private String displayIndexPage(Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Home");

        return "index.html";
    }
}
