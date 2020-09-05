package com.akandrewkoch.mavenRunshare.controllers;


import com.akandrewkoch.mavenRunshare.models.DTO.NewRunSessionDTO;
import com.akandrewkoch.mavenRunshare.models.RunSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/runSessions")
public class RunSessionController extends MainController {
    //todo-create an edit view for runSessions
    @GetMapping(value={"", "/{sortType}"})
    public String displayRunSessionsList(@PathVariable(required=false) String sortType, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Run Sessions");
        if (sortType!=null) {
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
        model.addAttribute("runSessions", runSessionRepository.findAll());
        return "runSessions/index";
    }

    @GetMapping("/addRunSession")
    public String displayAddRunSessionsForm(Model model, HttpServletRequest request, HttpSession session){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Run Session");
        NewRunSessionDTO newRunSessionDTO = new NewRunSessionDTO();
        newRunSessionDTO.setHours(0);
        newRunSessionDTO.setMinutes(0);
        newRunSessionDTO.setSeconds(0);
        model.addAttribute(newRunSessionDTO);
        model.addAttribute("runners", runnerRepository.findAll());
        model.addAttribute("trails", trailRepository.findAll());
        return "runSessions/addRunSession";
    }

    @PostMapping("/addRunSession")
    public String processAddRunSessionForm (@ModelAttribute @Valid NewRunSessionDTO newRunSessionDTO, Errors errors, Model model, HttpServletRequest request, HttpSession session){
        setRunnerInModel(request, model);
        if (errors.hasErrors()){
            model.addAttribute("title", "Add Run Session");
            model.addAttribute("runners", runnerRepository.findAll());
            model.addAttribute("trails", trailRepository.findAll());
            return "runSessions/addRunSession";
        }

        RunSession checkedRunSession = runSessionRepository.findByName(newRunSessionDTO.getName());

        if (checkedRunSession!=null){
            errors.rejectValue("name", "runSession.alreadyExists", "That Run Session name has already been used");
            model.addAttribute("runners", runnerRepository.findAll());
            model.addAttribute("trails", trailRepository.findAll());
            model.addAttribute("title", "Add Run Session");
            return "runSessions/addRunSession";
        }

        RunSession newRunSession = new RunSession(newRunSessionDTO.getName(), newRunSessionDTO.getDate(),  getRunnerFromSession(session), newRunSessionDTO.getTrail(),  newRunSessionDTO.getLaps(), (newRunSessionDTO.getSeconds()+(newRunSessionDTO.getMinutes()*60)+(newRunSessionDTO.getHours()*3600)));
        runSessionRepository.save(newRunSession);
        model.addAttribute("title", "Run Sessions");
        model.addAttribute("runSessions", runSessionRepository.findAll());


        return "redirect:/runSessions";
    }

    @GetMapping("/runSessionDetails/{id}")
    public String displayRunSessionDetails (@PathVariable Integer id, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);

        Optional<RunSession> testRunSession = runSessionRepository.findById(id);

        if (testRunSession.isEmpty()){
            return "runSessions/index";
        }

        RunSession detailedRunSession = testRunSession.get();
        model.addAttribute("comments", commentRepository.findByRunSession_IdOrderByDateCreatedDescTimeCreatedDesc(id));
        model.addAttribute("title", "Details " + detailedRunSession.getName());
        model.addAttribute("detailedRunSession", detailedRunSession);
        return "/runSessions/runSessionDetails";
    }

    @GetMapping("/runSessionDetails")
    public String displayBlankRunSessionDetails (Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Blank Details");
        model.addAttribute("detailedRunSession", new RunSession());
        return "/runSessions/runSessionDetails";
    }
}
