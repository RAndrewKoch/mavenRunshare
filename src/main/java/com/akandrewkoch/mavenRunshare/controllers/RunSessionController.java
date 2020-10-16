package com.akandrewkoch.mavenRunshare.controllers;


import com.akandrewkoch.mavenRunshare.models.Comment;
import com.akandrewkoch.mavenRunshare.models.DTO.NewRunSessionDTO;
import com.akandrewkoch.mavenRunshare.models.JavaEmail;
import com.akandrewkoch.mavenRunshare.models.RunSession;
import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.enums.Hours;
import com.akandrewkoch.mavenRunshare.models.enums.SecondsAndMinutes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/runSessions")
public class RunSessionController extends MainController {
    //todo-create an edit view for runSessions
    @GetMapping(value = {"", "index", "/{sortType}"})
    public String displayRunSessionsList(@PathVariable(required = false) String sortType, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "Run Sessions");
        if (sortType != null) {
            switch (sortType) {
                case "nameAsc":
                    model.addAttribute("runSessions", runSessionRepository.findAllByOrderByNameAsc());
                    model.addAttribute("sortType", "ascending name");
                    return "runSessions/index";
                case "nameDesc":
                    model.addAttribute("runSessions", runSessionRepository.findAllByOrderByNameDesc());
                    model.addAttribute("sortType", "descending name");
                    return "runSessions/index";
                case "timeAsc":
                    model.addAttribute("runSessions", runSessionRepository.findAllByOrderByTimeAsc());
                    model.addAttribute("sortType", "ascending time");
                    return "runSessions/index";
                case "timeDesc":
                    model.addAttribute("runSessions", runSessionRepository.findAllByOrderByTimeDesc());
                    model.addAttribute("sortType", "descending time");
                    return "runSessions/index";
                case "dateAsc":
                    model.addAttribute("runSessions", runSessionRepository.findAllByOrderByDateAsc());
                    model.addAttribute("sortType", "ascending date");
                    return "runSessions/index";
                case "dateDesc":
                    model.addAttribute("runSessions", runSessionRepository.findAllByOrderByDateDesc());
                    model.addAttribute("sortType", "descending date");
                    return "runSessions/index";
            }
        }
        model.addAttribute("runSessions", runSessionRepository.findAllByOrderByDateDescTimeDesc());
        return "runSessions/index";
    }

    @GetMapping("/addRunSession")
    public String displayAddRunSessionsForm(Model model, HttpServletRequest request, HttpSession session) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Run Session");
        NewRunSessionDTO newRunSessionDTO = new NewRunSessionDTO();
        newRunSessionDTO.setHours(0);
        newRunSessionDTO.setMinutes(0);
        newRunSessionDTO.setSeconds(0);
        model.addAttribute(newRunSessionDTO);
        model.addAttribute("secondsAndMinutes", SecondsAndMinutes.values());
        model.addAttribute("hours", Hours.values());
        model.addAttribute("runners", runnerRepository.findAll());
        model.addAttribute("trails", trailRepository.findAll());
        return "runSessions/addRunSession";
    }

    @PostMapping("/addRunSession")
    public String processAddRunSessionForm(@ModelAttribute @Valid NewRunSessionDTO newRunSessionDTO, Errors errors, Model model, HttpServletRequest request, HttpSession session) {
        setRunnerInModel(request, model);
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Run Session");
            model.addAttribute("secondsAndMinutes", SecondsAndMinutes.values());
            model.addAttribute("hours", Hours.values());
            model.addAttribute("runners", runnerRepository.findAll());
            model.addAttribute("trails", trailRepository.findAll());
            return "runSessions/addRunSession";
        }

        //---This code makes it so only one run session can have a given name.  It's not really needed, but left here in case I change my mind
//        RunSession checkedRunSession = runSessionRepository.findByName(newRunSessionDTO.getName());
//
//        if (checkedRunSession != null) {
//            errors.rejectValue("name", "runSession.alreadyExists", "That Run Session name has already been used");
//            model.addAttribute("runners", runnerRepository.findAll());
//            model.addAttribute("trails", trailRepository.findAll());
//            model.addAttribute("title", "Add Run Session");
//            return "runSessions/addRunSession";
//        }

        RunSession newRunSession = new RunSession(newRunSessionDTO.getName(), newRunSessionDTO.getRunners(), newRunSessionDTO.getDate(), getRunnerFromSession(session), newRunSessionDTO.getTrail(), newRunSessionDTO.getLaps(), (newRunSessionDTO.getSeconds() + (newRunSessionDTO.getMinutes() * 60) + (newRunSessionDTO.getHours() * 3600)));
        runSessionRepository.save(newRunSession);
//        RunSession runSessionToNotify = runSessionRepository.findById(newRunSession.getId()).get();
        Comment runSessionNotification = new Comment("New Run: " + newRunSession.getName(), newRunSession.runSessionDisplayString(), newRunSession.getCreator(), LocalDate.now(), LocalTime.now(), trailRepository.findById(newRunSession.getTrail().getId()).get(), newRunSession, new ArrayList<Runner>(), true);
        List<Runner> runnersToAddToComment = runSessionRepository.findById(newRunSession.getId()).get().getRunners();
        for (Runner runner : runnersToAddToComment) {
            runSessionNotification.addRunner(runner);
        }
        runSessionNotification.addRunner(newRunSession.getCreator());
        commentRepository.save(runSessionNotification);
        for (Runner runner : runnersToAddToComment) {
            JavaEmail javaEmail = new JavaEmail();
            String runnerMessage = "<body>"+
                    "<h1 style=\"text-align:center;\">Hey, " + runner.getCallsign() + ", " + runSessionNotification.getMessageCreator().getCallsign() + " logged a new Run Session with you!</h1>"+
                    "<h2 style=\"text-align:center;\">Check it out here:</h2>"+
                    "<div style=\"text-align:center;\">"+
                    "<h3>"+
                    "<a style=\"text-align:center;\" href=\""+System.getenv("ENVIRONMENT_URL")+"/runSessions/runSessionDetails/"+newRunSession.getId()+"\">View "+newRunSession.getName()+"</a>"+
                    "</h3>"+
                    "</div>"+
                    "</body>";
            if (System.getenv("ENVIRONMENT_URL").equals("http://localhost:8080")){
                runnerMessage+="sent via development mode";
            }

            if (runner.getEmail()!=null) {
                javaEmail.sendEmail(runner.getEmail(), System.getenv("SENDING_EMAIL_ADDRESS"), "New Run Session Logged", runnerMessage );
            }
        }
        if (runSessionNotification.getMessageCreator().getEmail()!=null) {
            JavaEmail javaEmail = new JavaEmail();
            String creatorMessage = "<body>"+
                    "<h1 style=\"text-align:center;\">Hey, " + runSessionNotification.getMessageCreator().getCallsign() + ", your " + newRunSession.getName() + " session has been logged on RunShare!"+
                    "<h2 style=\"text-align:center;\">Check it out here:</h2>"+
                    "<div style=\"text-align:center;\">"+
                    "<h3>"+
                    "<a style=\"text-align:center;\" href=\""+System.getenv("ENVIRONMENT_URL")+"/runSessions/runSessionDetails/"+newRunSession.getId()+"\">View "+newRunSession.getName()+"</a>"+
                    "</h3>"+
                    "</div>"+
                    "</body>";
            if (System.getenv("ENVIRONMENT_URL").equals("http://localhost:8080")){
                creatorMessage+="sent via development mode";
            }

            javaEmail.sendEmail(runSessionNotification.getMessageCreator().getEmail(), System.getenv("SENDING_EMAIL_ADDRESS"), "Great Run Session, " + runSessionNotification.getMessageCreator().getCallsign() + "!", creatorMessage);
        }
        model.addAttribute("title", "Run Sessions");
        model.addAttribute("runSessions", runSessionRepository.findAll());


        return "redirect:/runSessions";
    }

    @GetMapping("/runSessionDetails/{id}")
    public String displayRunSessionDetails(@PathVariable Integer id, Model model, @RequestParam(required=false) Integer deleteComment, HttpServletRequest request) {
        setRunnerInModel(request, model);

        Optional<RunSession> testRunSession = runSessionRepository.findById(id);

        if (testRunSession.isEmpty()) {
            return "runSessions/index";
        }

        if (deleteComment!=null){
            Comment commentToDelete = commentRepository.findById(deleteComment).get();
            commentToDelete.deleteComment();
            model.addAttribute("commentDeleted", commentToDelete.getMessageTitle());
            commentRepository.save(commentToDelete);
        }

        RunSession detailedRunSession = testRunSession.get();
        model.addAttribute("comments", commentRepository.findByRunSession_IdOrderByDateCreatedDescTimeCreatedDesc(id));
        model.addAttribute("title", "Details " + detailedRunSession.getName());
        model.addAttribute("detailedRunSession", detailedRunSession);
        List<Runner> otherRunners = runnerRepository.findAllByRunSessionPackId(id);
        String runnerString = "";
        if (!otherRunners.isEmpty()) {
            if (otherRunners.size() == 1) {
                runnerString = otherRunners.get(0).getCallsign();
            } else {
                runnerString += otherRunners.get(0).getCallsign();
                if (otherRunners.size() > 1) {
                    for (int i = 1; i < otherRunners.size() - 1; i++) {
                        runnerString += ", " + otherRunners.get(i).getCallsign();
                    }
                }
                runnerString += " and " + otherRunners.get(otherRunners.size() - 1).getCallsign();
            }
        }
        if (!runnerString.isBlank()) {
            model.addAttribute("runners", runnerString);
        }
        return "runSessions/runSessionDetails";
    }

    @GetMapping("/runSessionDetails")
    public String displayBlankRunSessionDetails(Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "Blank Details");
        model.addAttribute("detailedRunSession", new RunSession());
        return "runSessions/runSessionDetails";
    }

    @GetMapping("/editRunSession/{runSessionId}")
    public String displayEditRunSession(@PathVariable Integer runSessionId, Model model, HttpServletRequest request, HttpSession session) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "edit " + runSessionRepository.findById(runSessionId).get().getName() + " run session");
        RunSession runSessionToEdit = runSessionRepository.findById(runSessionId).get();
        NewRunSessionDTO newRunSessionDTO = new NewRunSessionDTO();
        newRunSessionDTO.setName(runSessionToEdit.getName());
        newRunSessionDTO.setRunners(runSessionToEdit.getRunners());
        newRunSessionDTO.setDate(runSessionToEdit.getDate());
        newRunSessionDTO.setTrail(runSessionToEdit.getTrail());
        newRunSessionDTO.setLaps(runSessionToEdit.getLaps());
        Integer hours = (int) (Math.floor(runSessionToEdit.getTimeInSeconds() / 3600));
        newRunSessionDTO.setHours(hours);
        Integer minutes = (int) Math.floor((runSessionToEdit.getTimeInSeconds() - (hours * 3600)) / 60);
        newRunSessionDTO.setMinutes(minutes);
        Integer seconds = runSessionToEdit.getTimeInSeconds() - (hours * 3600) - (minutes * 60);
        newRunSessionDTO.setSeconds(seconds);
        model.addAttribute(newRunSessionDTO);
        if (!newRunSessionDTO.runners.isEmpty()) {
            model.addAttribute("previousRunners", newRunSessionDTO.runners);
        }
        List<Runner> notChosenRunners = (List<Runner>) runnerRepository.findAll();
        notChosenRunners.removeAll(newRunSessionDTO.runners);
        if (notChosenRunners.contains(getRunnerFromSession(session))) {
            notChosenRunners.remove(getRunnerFromSession(session));
        }
        if (!notChosenRunners.isEmpty()) {
            model.addAttribute("notChosenRunners", notChosenRunners);
        }
        model.addAttribute("trails", trailRepository.findAll());
        return "runSessions/editRunSession";
    }

    @PostMapping("/editRunSession/{runSessionId}")
    public String processEditRunSessionForm(@ModelAttribute @Valid NewRunSessionDTO newRunSessionDTO, Errors errors, Model model, @PathVariable Integer runSessionId, HttpSession session, HttpServletRequest request) {
        setRunnerInModel(request, model);
        if (errors.hasErrors()) {
            model.addAttribute("title", "edit " + runSessionRepository.findById(runSessionId).get().getName() + " run session");
            if (!newRunSessionDTO.runners.isEmpty()) {
                model.addAttribute("previousRunners", newRunSessionDTO.runners);
            }
            List<Runner> notChosenRunners = (List<Runner>) runnerRepository.findAll();
            notChosenRunners.removeAll(newRunSessionDTO.runners);
            if (notChosenRunners.contains(getRunnerFromSession(session))) {
                notChosenRunners.remove(getRunnerFromSession(session));
            }
            if (!notChosenRunners.isEmpty()) {
                model.addAttribute("notChosenRunners", notChosenRunners);
            }
            model.addAttribute("trails", trailRepository.findAll());
            return "runSessions/editRunSession";
        }
        RunSession editedRunSession = runSessionRepository.findById(runSessionId).get();
        editedRunSession.setName(newRunSessionDTO.getName());
        editedRunSession.setDate(newRunSessionDTO.getDate());
        editedRunSession.setRunners(newRunSessionDTO.getRunners());
        editedRunSession.setTrail(newRunSessionDTO.getTrail());
        editedRunSession.setLaps(newRunSessionDTO.getLaps());
        editedRunSession.setDistanceFromDTO();
        editedRunSession.setTime(newRunSessionDTO.getSeconds() + (newRunSessionDTO.getMinutes() * 60) + (newRunSessionDTO.getHours() * 3600));
        editedRunSession.calculatePace();
        runSessionRepository.save(editedRunSession);
        List<Comment> runSessionComments = commentRepository.findByRunSession_Id(runSessionId);
        for (Comment comment : runSessionComments) {
            comment.blankRunners();
            List<Runner> runnersToAddToComment = runSessionRepository.findById(runSessionId).get().getRunners();
            for (Runner runner : runnersToAddToComment) {
                comment.addRunner(runner);
            }
            comment.addRunner(runSessionRepository.findById(runSessionId).get().getCreator());
            commentRepository.save(comment);
        }


        return "redirect:/runSessions/runSessionDetails/" + runSessionId;
    }
}
