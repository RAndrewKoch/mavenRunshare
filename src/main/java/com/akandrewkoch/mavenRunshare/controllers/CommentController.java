package com.akandrewkoch.mavenRunshare.controllers;

import com.akandrewkoch.mavenRunshare.models.Comment;
import com.akandrewkoch.mavenRunshare.models.DTO.NewCommentDTO;
import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.Trail;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;


@Controller
@RequestMapping("/comments")
public class CommentController extends MainController{

    @GetMapping
    public String displayCommentIndex(HttpServletRequest request, Model model){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Comments");
        model.addAttribute("comments", commentRepository.findFirst10ByOrderByDateCreatedDescTimeCreatedDesc());
        return "comments/index";
    }

    @GetMapping("/createComment")
    public String displayCreateComment(HttpServletRequest request, Model model){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Create Comment");
        model.addAttribute("nullTrail", null);
        model.addAttribute("nullRunSession", null);
        model.addAttribute("runners", runnerRepository.findAll());
        model.addAttribute("trails", trailRepository.findAll());
        model.addAttribute("runSessions", runSessionRepository.findAll());
        model.addAttribute(new NewCommentDTO());
        return "comments/createComment";
    }

    @GetMapping("/createComment/{entityId}")
    public String displayCreateCommentEntity(@PathVariable Integer entityId, HttpServletRequest request, Model model){
        setRunnerInModel(request, model);
        if (entityId.equals(null)){
        } else {
            Optional<Trail> testTrail = trailRepository.findById(entityId);
            if (testTrail.isPresent()){
                Trail trailToCommentOn = testTrail.get();
                model.addAttribute("trailToCommentOn", trailToCommentOn);
            }
        }
        model.addAttribute("title", "Create Comment");
        model.addAttribute("nullTrail", null);
        model.addAttribute("nullRunSession", null);
        model.addAttribute("runners", runnerRepository.findAll());
        model.addAttribute("trails", trailRepository.findAll());
        model.addAttribute("runSessions", runSessionRepository.findAll());
        model.addAttribute(new NewCommentDTO());
        return "comments/createComment";
    }

    @PostMapping("/createComment")
    public String processCreateComment(@ModelAttribute @Valid NewCommentDTO newCommentDTO, Errors errors, Model model, @RequestParam(value = "runnersList", required=false) int[] runnersList, HttpServletRequest request, HttpSession session){
        setRunnerInModel(request, model);
        if (errors.hasErrors()){
            model.addAttribute("title", "Create Comment");
            model.addAttribute("nullTrail", null);
            model.addAttribute("nullRunSession", null);
            model.addAttribute("runners", runnerRepository.findAll());
            model.addAttribute("trails", trailRepository.findAll());
            model.addAttribute("runSessions", runSessionRepository.findAll());
            return "comments/createComment";
        }

        if (runnersList != null) {
            for (int value : runnersList) {
                newCommentDTO.runners.add(runnerRepository.findById(value));
            }
        }

        newCommentDTO.runners.add(runnerRepository.findById(getRunnerFromSession(session).getId()).get());
        HttpSession commentSession = request.getSession();
        Runner commentCreator = getRunnerFromSession(commentSession);
        Comment savedComment = new Comment(newCommentDTO.getMessageTitle(), newCommentDTO.getMessage(), commentCreator, LocalDate.now(), LocalTime.now().minus(Duration.ofHours(5)), newCommentDTO.getTrail(), newCommentDTO.getRunSession(), newCommentDTO.getRunners(), newCommentDTO.getPrivateMessage());
        commentRepository.save(savedComment);
        return "redirect:/comments";
    }

    @PostMapping(value= {"/createComment/{entityId}"})
    public String processCreateCommentEntity(@ModelAttribute @Valid NewCommentDTO newCommentDTO, Errors errors, Model model, @RequestParam(value = "runnersList", required=false) int[] runnersList, @PathVariable(required=false) Integer entityId, HttpServletRequest request, HttpSession session){
        setRunnerInModel(request, model);
        if (errors.hasErrors()){
            if (!entityId.equals(null)){
                Optional<Trail> testTrail = trailRepository.findById(entityId);
                if (testTrail.isPresent()){
                    Trail trailToCommentOn = testTrail.get();
                    model.addAttribute("trailToCommentOn", trailToCommentOn);
                }
            }
            model.addAttribute("title", "Create Comment");
            model.addAttribute("nullTrail", null);
            model.addAttribute("nullRunSession", null);
            model.addAttribute("runners", runnerRepository.findAll());
            model.addAttribute("trails", trailRepository.findAll());
            model.addAttribute("runSessions", runSessionRepository.findAll());
            return "comments/createComment";
        }

        if (runnersList != null) {
            for (int value : runnersList) {
                newCommentDTO.runners.add(runnerRepository.findById(value));
            }
        }
        newCommentDTO.runners.add(runnerRepository.findById(getRunnerFromSession(session).getId()).get());
        HttpSession commentSession = request.getSession();
        Runner commentCreator = getRunnerFromSession(commentSession);
        Comment savedComment = new Comment(newCommentDTO.getMessageTitle(), newCommentDTO.getMessage(), commentCreator, LocalDate.now(), LocalTime.now().minus(Duration.ofHours(5)), newCommentDTO.getTrail(), newCommentDTO.getRunSession(), newCommentDTO.getRunners(), newCommentDTO.getPrivateMessage());
        commentRepository.save(savedComment);
        return "redirect:/comments";
    }
}
