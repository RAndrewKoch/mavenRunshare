package com.akandrewkoch.mavenRunshare.controllers;

import com.akandrewkoch.mavenRunshare.models.Comment;
import com.akandrewkoch.mavenRunshare.models.DTO.NewCommentDTO;
import com.akandrewkoch.mavenRunshare.models.RunSession;
import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.Trail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/comments")
public class CommentController extends MainController{

    @GetMapping(value={"", "index"})
    public String displayCommentIndex(@RequestParam(required=false) Integer deleteComment,
                                      @RequestParam(required=false) Integer pageNumber, HttpServletRequest request,
                                      Model model){
        if (pageNumber ==null){
            pageNumber = 1;
        }

        if (deleteComment != null){
            Comment commentToDelete = commentRepository.findById(deleteComment).get();
            commentToDelete.deleteComment();
            commentRepository.save(commentToDelete);
        }

        setRunnerInModel(request, model);
        model.addAttribute("title", "Comments");
        Pageable paging = PageRequest.of(pageNumber-1, 5);
        Page<Comment> pageComment = commentRepository.findByDeletedCommentAndPrivateMessageOrderByDateCreatedDescTimeCreatedDesc(false, false, paging);;
        List<Comment> pageOfComments = pageComment.getContent();
        if (!pageOfComments.isEmpty()) {
            model.addAttribute("comments", pageOfComments);
        }
        model.addAttribute("pageNumber", pageNumber);
        if (pageNumber<pageComment.getTotalPages()) {
            model.addAttribute("nextPage", pageNumber + 1);
        }

        if (pageNumber>0) {
            model.addAttribute("previousPage", pageNumber - 1);
        }

        model.addAttribute("totalPages", pageComment.getTotalPages());
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
    public String displayCreateCommentEntity(@PathVariable Integer entityId, HttpServletRequest request, Model model, HttpSession session){
        setRunnerInModel(request, model);
        model.addAttribute(new NewCommentDTO());
        List<Runner> runnersToAdd =new ArrayList<>();
        ArrayList<Runner> runnersToDisplay = (ArrayList<Runner>) runnerRepository.findAll();
        runnersToDisplay.remove(runnersToDisplay.indexOf(getRunnerFromSession(session)));
        Optional<Trail> testTrail = trailRepository.findById(entityId);
        if (testTrail.isPresent()){
            Trail trailToCommentOn = testTrail.get();
            model.addAttribute("trailToCommentOn", trailToCommentOn);
        }
        Optional<RunSession> testRunSession = runSessionRepository.findById(entityId);
        if (testRunSession.isPresent()){
            RunSession runSessionToCommentOn = testRunSession.get();
            model.addAttribute("runSessionToCommentOn", runSessionToCommentOn);
            model.addAttribute("trailToCommentOn", runSessionToCommentOn.getTrail());
            runnersToDisplay.removeAll(runSessionToCommentOn.getRunners());
            runnersToDisplay.remove(runSessionToCommentOn.getCreator());
            if (!runSessionToCommentOn.getRunners().isEmpty()){
                runnersToAdd.addAll(runSessionToCommentOn.getRunners());
            }
            runnersToAdd.add(runSessionToCommentOn.getCreator());
            if (runnersToAdd.contains(getRunnerFromSession(session))){
                runnersToAdd.remove(getRunnerFromSession(session));
            }
        }

        Optional<Runner> testRunner = runnerRepository.findById(entityId);
        if (testRunner.isPresent()){
            Runner runnerToCommentOn = testRunner.get();
            model.addAttribute("runnerToCommentOn", runnerToCommentOn);
        }
        model.addAttribute("runnersToAdd", runnersToAdd);
        model.addAttribute("title", "Create Comment");
        model.addAttribute("nullTrail", null);
        model.addAttribute("nullRunSession", null);
        model.addAttribute("runners", runnersToDisplay);
        model.addAttribute("trails", trailRepository.findAll());
        model.addAttribute("runSessions", runSessionRepository.findAll());

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

    @PostMapping("/createComment/{entityId}")
    public String processCreateCommentEntity(@ModelAttribute @Valid NewCommentDTO newCommentDTO, Errors errors, Model model, @RequestParam(value = "runnersList", required=false) int[] runnersList, @PathVariable(required=false) Integer entityId, HttpServletRequest request, HttpSession session){
        setRunnerInModel(request, model);
        if (errors.hasErrors()){
            List<Runner> runnersToAdd =new ArrayList<>();
            ArrayList<Runner> runnersToDisplay = (ArrayList<Runner>) runnerRepository.findAll();
            runnersToDisplay.remove(runnersToDisplay.indexOf(getRunnerFromSession(session)));
            if (!entityId.equals(null)){
                Optional<Trail> testTrail = trailRepository.findById(entityId);
                if (testTrail.isPresent()){
                    Trail trailToCommentOn = testTrail.get();
                    model.addAttribute("trailToCommentOn", trailToCommentOn);
                }
                Optional<RunSession> testRunSession = runSessionRepository.findById(entityId);
                if (testRunSession.isPresent()) {
                    RunSession runSessionToCommentOn = testRunSession.get();
                    model.addAttribute("runSessionToCommentOn", runSessionToCommentOn);
                    model.addAttribute("trailToCommentOn", runSessionToCommentOn.getTrail());
                    runnersToDisplay.removeAll(runSessionToCommentOn.getRunners());
                    runnersToDisplay.remove(runSessionToCommentOn.getCreator());
                    if (!runSessionToCommentOn.getRunners().isEmpty()) {
                        runnersToAdd.addAll(runSessionToCommentOn.getRunners());
                    }
                    runnersToAdd.add(runSessionToCommentOn.getCreator());
                    if (runnersToAdd.contains(getRunnerFromSession(session))) {
                        runnersToAdd.remove(getRunnerFromSession(session));
                    }
                }
                Optional<Runner> testRunner = runnerRepository.findById(entityId);
                if (testRunner.isPresent()){
                    Runner runnerToCommentOn = testRunner.get();
                    model.addAttribute("runnerToCommentOn", runnerToCommentOn);
                }

            }
            model.addAttribute("runnersToAdd", runnersToAdd);
            model.addAttribute("title", "Create Comment");
            model.addAttribute("nullTrail", null);
            model.addAttribute("nullRunSession", null);
            model.addAttribute("runners", runnersToDisplay);
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
        Optional<Runner> testRunner = runnerRepository.findById(entityId);
        if (testRunner.isPresent()){
            savedComment.setPrivateMessage(true);
        }

        commentRepository.save(savedComment);
        return "redirect:/comments";
    }
}
