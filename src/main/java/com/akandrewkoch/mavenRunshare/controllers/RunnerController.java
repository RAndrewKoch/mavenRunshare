package com.akandrewkoch.mavenRunshare.controllers;


import com.akandrewkoch.mavenRunshare.models.Comment;
import com.akandrewkoch.mavenRunshare.models.DTO.NewFriendRequestDTO;
import com.akandrewkoch.mavenRunshare.models.DTO.NewRunnerRegistrationDTO;
import com.akandrewkoch.mavenRunshare.models.DTO.RunnerLoginDTO;
import com.akandrewkoch.mavenRunshare.models.JavaEmail;
import com.akandrewkoch.mavenRunshare.models.RunSession;
import com.akandrewkoch.mavenRunshare.models.Runner;
import com.akandrewkoch.mavenRunshare.models.enums.Gender;
import com.akandrewkoch.mavenRunshare.models.enums.RunnerLevel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/runners")
public class RunnerController extends MainController {

    public static void setUserInSession(HttpSession session, Runner runner) {
        session.setAttribute(runnerSessionKey, runner.getId());
    }

    @GetMapping(value = {"", "/index", "/{sortType}"})
    public String displayRunnersIndex(@PathVariable(required = false) String sortType, @RequestParam(required = false) String friendsList, @RequestParam(required = false) String runnerLevel, HttpServletRequest request, HttpSession session, Model model) {
        setRunnerInModel(request, model);
        if (friendsList != null) {
            if (friendsList.equals("true")) {
                model.addAttribute("friendsList", runnerRepository.findById(getRunnerFromSession(session).getId()).get().getFriends());
            }
        }

        if (runnerLevel != null){
            model.addAttribute("runnerLevel", runnerLevel);
        }

        List<Runner> runners= (List<Runner>) runnerRepository.findAll();
        List<Runner> runnersWithFriendRequests = new ArrayList<>();
        if (getRunnerFromSession(session)!=null){
            for(Runner runner : runners){
                if (runner.getFriendRequests().contains(getRunnerFromSession(session))){
                    runnersWithFriendRequests.add(runner);
                }
            }
            model.addAttribute("runnersWithFriendRequests", runnersWithFriendRequests);
            model.addAttribute("runnersWhoHaveSentCurrentRunnerFriendRequest", getRunnerFromSession(session).getFriendRequests());
        }
        model.addAttribute("runners", runners);

        model.addAttribute("title", "Runners");
        if (sortType != null) {
            switch (sortType) {
                case "callsignAsc":
                    model.addAttribute("sortType", "ascending Callsign");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByCallsignAsc());
                    return "runners/index";
                case "callsignDesc":
                    model.addAttribute("sortType", "descending Callsign");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByCallsignDesc());
                    return "runners/index";
                case "runnerLevelAsc":
                    model.addAttribute("sortType", "ascending runner level");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByRunnerLevelAsc());
                    return "runners/index";
                case "runnerLevelDesc":
                    model.addAttribute("sortType", "descending runner level");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByRunnerLevelDesc());
                    return "runners/index";
                case "firstNameAsc":
                    model.addAttribute("sortType", "ascending first name");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByFirstNameAsc());
                    return "runners/index";
                case "firstNameDesc":
                    model.addAttribute("sortType", "descending first name");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByFirstNameDesc());
                    return "runners/index";
                case "lastNameAsc":
                    model.addAttribute("sortType", "ascending last name");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByLastNameAsc());
                    return "runners/index";
                case "lastNameDesc":
                    model.addAttribute("sortType", "descending last name");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByLastNameDesc());
                    return "runners/index";
                case "ageAsc":
                    model.addAttribute("sortType", "ascending age");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByAgeAsc());
                    return "runners/index";
                case "ageDesc":
                    model.addAttribute("sortType", "descending age");
                    model.addAttribute("runners", runnerRepository.findAllByOrderByAgeDesc());
                    return "runners/index";
            }
        }

        return "runners/index";
    }

    @GetMapping("/addRunner")
    public String displayAddRunnerForm(HttpServletRequest request, Model model) {
        setRunnerInModel(request, model);
        model.addAttribute("runnerLevels", RunnerLevel.values());
        model.addAttribute("genders", Gender.values());
        NewRunnerRegistrationDTO newRunnerRegistrationDTO = new NewRunnerRegistrationDTO();
        newRunnerRegistrationDTO.setAge(16);
        model.addAttribute(newRunnerRegistrationDTO);
        model.addAttribute("title", "Add Runner");
        return "runners/addRunner";
    }

    @PostMapping("/addRunner")
    public String processAddRunnerForm(@ModelAttribute @Valid NewRunnerRegistrationDTO newRunnerRegistrationDTO, Errors errors, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        if (errors.hasErrors()) {
            model.addAttribute("runnerLevels", RunnerLevel.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("title", "Add Runner");
            model.addAttribute("newRunnerRegistrationDTO", newRunnerRegistrationDTO);
            return "runners/addRunner";
        }

        Runner checkedRunner = runnerRepository.findByCallsign(newRunnerRegistrationDTO.getCallsign());

        if (checkedRunner != null) {
            errors.rejectValue("callsign", "callsign.alreadyExists", "A Runner with this callsign already exists!");
            model.addAttribute("runnerLevels", RunnerLevel.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("title", "Add Runner");
            return "runners/addRunner";
        }

        String password = newRunnerRegistrationDTO.getPassword();
        String verifyPassword = newRunnerRegistrationDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "password.doesNotMatch", "The passwords do not match");
            model.addAttribute("runnerLevels", RunnerLevel.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("title", "Add Runner");
            return "runners/addRunner";
        }
        Runner newRunner = new Runner(newRunnerRegistrationDTO.getCallsign(), newRunnerRegistrationDTO.getFirstName(), newRunnerRegistrationDTO.getLastName(), newRunnerRegistrationDTO.isCallsignOnly(), newRunnerRegistrationDTO.getPassword(), newRunnerRegistrationDTO.getAge(), newRunnerRegistrationDTO.getWeight(), newRunnerRegistrationDTO.getGender(), newRunnerRegistrationDTO.getRunnerLevel(), newRunnerRegistrationDTO.getZip(), newRunnerRegistrationDTO.getEmail());
        runnerRepository.save(newRunner);
        JavaEmail javaEmail = new JavaEmail();
        String welcomeMessage = "<head>"+
                "<style type=\"text/css\">"+
                "</style>"+
                "<body style=\"text-align:center\">"+
                "<h1>Welcome to RunShare, "+newRunner.getCallsign()+"!</h1><br/>"+
                "<h2>Login to your new account here!<h2><br/>"+
                "<a style=\"border-style:solid; background-color:#2A2773; border-radius:10px; padding:5px; color:white;\" href=\""+System.getenv("ENVIRONMENT_URL")+"/runners/login/"+newRunner.getId()+"\">Join us on the trail!</a><br/>"+
                "</body>";
        javaEmail.sendEmail( newRunner.getEmail(), System.getenv("SENDING_EMAIL_ADDRESS"), "Welcome, "+newRunner.getCallsign()+"!", welcomeMessage);
        setUserInSession(request.getSession(), newRunner);


        return "redirect:/runners";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("runners", runnerRepository.findAll());
        model.addAttribute(new RunnerLoginDTO());
        model.addAttribute("title", "Login");
        return "runners/login";
    }

    @GetMapping("/login/{id}")
    public String displayLoginFormWithId(@PathVariable Integer id, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("selectedRunner", runnerRepository.findById(id).get());
        RunnerLoginDTO listedRunner = new RunnerLoginDTO();
        listedRunner.setCallsign(runnerRepository.findById(id).get().getCallsign());
        model.addAttribute("runnerLoginDTO", listedRunner);
        model.addAttribute("title", "Login " + listedRunner.getCallsign());
        return "runners/login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid RunnerLoginDTO runnerLoginDTO, Errors errors, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "Login");
        if (errors.hasErrors()) {
            model.addAttribute("runners", runnerRepository.findAll());
            return "runners/login";
        }

        Runner loginRunner = runnerRepository.findByCallsign(runnerLoginDTO.getCallsign());

        if (loginRunner == null) {
            errors.rejectValue("callsign", "callsign.notFound", "That Callsign has not been registered!");
            return "runners/login";
        }

        if (!loginRunner.isMatchingPassword(runnerLoginDTO.getPassword())) {
            errors.rejectValue("password", "password.incorrectPassword", "Password is not correct for this Callsign");
            model.addAttribute("runners", runnerRepository.findAll());
            return "runners/login";
        }

        setUserInSession(request.getSession(), loginRunner);
        setRunnerInModel(request, model);
        model.addAttribute("runners", runnerRepository.findAll());
        return "redirect:/runners/runnerDetails/" + loginRunner.getId();
    }

    @PostMapping("/login/{id}")
    public String processLoginForm(@PathVariable Integer id, @ModelAttribute @Valid RunnerLoginDTO runnerLoginDTO, Errors errors, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("selectedRunner", runnerRepository.findById(id).get());
        model.addAttribute("title", "Login");
        if (errors.hasErrors()) {
            return "runners/login";
        }

        Runner loginRunner = runnerRepository.findByCallsign(runnerLoginDTO.getCallsign());

        if (loginRunner == null) {
            errors.rejectValue("callsign", "callsign.notFound", "That Callsign has not been registered!");
            return "runners/login";
        }

        if (!loginRunner.isMatchingPassword(runnerLoginDTO.getPassword())) {
            errors.rejectValue("password", "password.incorrectPassword", "Password is not correct for this Callsign");
            return "runners/login";
        }

        setUserInSession(request.getSession(), loginRunner);
        model.addAttribute("runners", runnerRepository.findAll());
        setRunnerInModel(request, model);
        return "redirect:/runners/runnerDetails/" + loginRunner.getId();
    }

    @GetMapping("/logout")
    private String logoutRunner(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("title", "Home");
        session.removeAttribute(runnerSessionKey);
        request.getSession().invalidate();
        return "index";
    }

    @GetMapping("/runnerDetails/{id}")
    private String displayRunnerDetailsView(@PathVariable Integer id, Model model, HttpServletRequest request, HttpSession session) {
        setRunnerInModel(request, model);
        //        if no runner is logged in, detail pages cannot be viewed
        if (getRunnerFromSession(session) == null) {
            model.addAttribute("detailedRunner", runnerRepository.findById(id).get());

            return "runners/runnerDetailsNoAccess";
        }

        //        find if the person requesting the details page is the detailed runner, or a friend of the detailed runner
        if (getRunnerFromSession(session).getId().equals(id) || getRunnerFromSession(session).getFriends().contains(runnerRepository.findById(id).get())) {
            Optional<Runner> testRunner = runnerRepository.findById(id);

            if (getRunnerFromSession(session).getId().equals(id)) {
                model.addAttribute("currentRunnerIsDetailedRunner", true);
            }

            if (testRunner.isEmpty()) {
                return "runners/index";
            }

            Runner detailedRunner = testRunner.get();
            model.addAttribute("title", "Details " + detailedRunner.getCallsign());
            model.addAttribute("detailedRunner", detailedRunner);

            List<Comment> comments = commentRepository.findByRunners_IdOrderByDateCreatedDescTimeCreatedDesc(id);
            if (!comments.isEmpty()) {
                model.addAttribute("comments", comments);
            }

            List<RunSession> runSessions = runSessionRepository.findAllByCreator_Id(id);
            if (!runSessions.isEmpty()) {
                model.addAttribute("runSessions", runSessions);
            }

            List<RunSession> otherSessions = runSessionRepository.findAllByRunners(runnerRepository.findById(id).get());
            if (!otherSessions.isEmpty()) {
                model.addAttribute("otherSessions", otherSessions);
            }

            if (!detailedRunner.getFriendRequests().isEmpty()) {
                model.addAttribute("friendRequests", detailedRunner.getFriendRequests());
            }

            if (!detailedRunner.getFriends().isEmpty()) {
                model.addAttribute("friends", detailedRunner.getFriends());
            }


            return "runners/runnerDetails";
        }

        NewFriendRequestDTO newFriendRequestDTO = new NewFriendRequestDTO();
        model.addAttribute(newFriendRequestDTO);
        model.addAttribute("detailedRunner", runnerRepository.findById(id).get());

        //        find out if logged in runner already has a friend Request for the detailed runner, and if so, display that instead of a request friend button
        if (runnerRepository.findById(id).get().getFriendRequests().contains(runnerRepository.findById(getRunnerFromSession(session).getId()).get())) {
            model.addAttribute("currentRunnerFriendRequestIssued", true);
        }

        //        find out if detailed runner has sent you a friend request already, but you have not accepted.  If so, display button to go to your details page
        if (runnerRepository.findById(getRunnerFromSession(session).getId()).get().getFriendRequests().contains(runnerRepository.findById(id).get())) {
            model.addAttribute("detailedRunnerIssuedFriendRequest", true);
        }
        return "runners/runnerDetailsNoAccess";
    }

    @GetMapping("/runnerDetails")
    private String displayRunnerDetailsBlank(Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "Blank Details");
        model.addAttribute("detailedRunner", new Runner());
        return "runners/runnerDetails";
    }

    @PostMapping("/runnerDetails/{id}")
    private String processFriendRequestDTO(@PathVariable int id, Model model, HttpServletRequest request, HttpSession session) {
        setRunnerInModel(request, model);
        Runner requestedFriend = runnerRepository.findById(id);
        requestedFriend.addFriendRequest(getRunnerFromSession(session));
        runnerRepository.save(requestedFriend);
        return "redirect:/runners/index";
    }

    @GetMapping("/editRunner/{id}")
    private String displayEditRunnerView(@PathVariable Integer id, Model model, HttpServletRequest request, HttpSession session) {
        setRunnerInModel(request, model);
        Optional<Runner> runnerTest = runnerRepository.findById(id);
        if (runnerTest.isEmpty()) {
            return "runners/index";
        }

        Runner runnerToEdit = runnerTest.get();
        NewRunnerRegistrationDTO newRunnerRegistrationDTO = new NewRunnerRegistrationDTO();
        newRunnerRegistrationDTO.setCallsign(runnerToEdit.getCallsign());
        newRunnerRegistrationDTO.setFirstName(runnerToEdit.getFirstName());
        newRunnerRegistrationDTO.setLastName(runnerToEdit.getLastName());
        newRunnerRegistrationDTO.setCallsignOnly(runnerToEdit.isCallsignOnly());
        newRunnerRegistrationDTO.setPassword("111111111");
        newRunnerRegistrationDTO.setVerifyPassword("111111111");
        newRunnerRegistrationDTO.setAge(runnerToEdit.getAge());
        newRunnerRegistrationDTO.setWeight(runnerToEdit.getWeight());
        newRunnerRegistrationDTO.setGender(runnerToEdit.getGender());
        newRunnerRegistrationDTO.setRunnerLevel(runnerToEdit.getRunnerLevel());
        newRunnerRegistrationDTO.setZip(runnerToEdit.getZip());
        newRunnerRegistrationDTO.setEmail(runnerToEdit.getEmail());
        model.addAttribute(newRunnerRegistrationDTO);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("runnerLevels", RunnerLevel.values());
        model.addAttribute("title", "Editing Runner " + newRunnerRegistrationDTO.getCallsign());
        return "runners/editRunner";

    }

    @PostMapping("/editRunner/{id}")
    private String processEditRunnerForm(@ModelAttribute @Valid NewRunnerRegistrationDTO newRunnerRegistrationDTO, Errors errors, Model model, HttpServletRequest request, @PathVariable Integer id) {
        setRunnerInModel(request, model);
        if (errors.hasErrors()) {
            model.addAttribute("newRunnerRegistrationDTO", newRunnerRegistrationDTO);
            model.addAttribute("runnerLevels", RunnerLevel.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("title", "Editing Runner" + newRunnerRegistrationDTO.getCallsign());
            return "runners/editRunner";
        }
        Runner updatedRunner = runnerRepository.findById(id).get();
        updatedRunner.setCallsign(newRunnerRegistrationDTO.getCallsign());
        updatedRunner.setFirstName(newRunnerRegistrationDTO.getFirstName());
        updatedRunner.setLastName(newRunnerRegistrationDTO.getLastName());
        updatedRunner.setCallsignOnly(newRunnerRegistrationDTO.isCallsignOnly());
        updatedRunner.setAge(newRunnerRegistrationDTO.getAge());
        updatedRunner.setWeight(newRunnerRegistrationDTO.getWeight());
        updatedRunner.setGender(newRunnerRegistrationDTO.getGender());
        updatedRunner.setRunningLevel(newRunnerRegistrationDTO.getRunnerLevel());
        updatedRunner.setZip(newRunnerRegistrationDTO.getZip());
        updatedRunner.setNumberZipCode(Integer.parseInt(newRunnerRegistrationDTO.getZip()));
        updatedRunner.setEmail(newRunnerRegistrationDTO.getEmail());
        runnerRepository.save(updatedRunner);
        return "redirect:/runners/runnerDetails/" + updatedRunner.getId();
    }

    @GetMapping("/friendRequest/{requester}/{requested}")
    private String displayFriendRequestView(@PathVariable Integer requester, @PathVariable Integer requested, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "Accept Friend");
        Runner friendRequested = runnerRepository.findById(requested).get();
        Runner runnerRequesting = runnerRepository.findById(requester).get();
        model.addAttribute("friendRequested", friendRequested);
        model.addAttribute("runnerRequesting", runnerRequesting);
        return "runners/friendRequest";
    }

    @PostMapping("/friendRequest/{requester}/{requested}")
    private String processFriendRequestForm(@PathVariable Integer requester, @PathVariable Integer requested, @RequestParam String acceptOrDecline, Model model, HttpServletRequest request) {
        setRunnerInModel(request, model);
        if (acceptOrDecline.equals("accept")) {
            model.addAttribute("title", "AcceptFriend");
            Runner runnerAccepting = runnerRepository.findById(requested).get();
            Runner runnerRequesting = runnerRepository.findById(requester).get();
            runnerAccepting.addFriend(runnerRequesting);
            runnerRequesting.addFriend(runnerAccepting);
            runnerAccepting.removeFriendRequest(runnerRequesting);
            runnerRepository.save(runnerAccepting);
            runnerRepository.save(runnerRequesting);
            return "redirect:/runners/runnerDetails/" + runnerAccepting.getId();
        } else if (acceptOrDecline.equals("decline")) {
            Runner runnerDeclining = runnerRepository.findById(requested).get();
            Runner runnerRequestiong = runnerRepository.findById(requester).get();
            runnerDeclining.removeFriendRequest(runnerRequestiong);
            runnerRepository.save(runnerDeclining);
            return "redirect:/runners/runnerDetails/" + runnerDeclining.getId();
        } else {
            Runner runnerDeferring = runnerRepository.findById(requested).get();
            return "redirect:/runners/runnerDetails/" + runnerDeferring.getId();
        }
    }

    @GetMapping("/forgottenPassword")
    public String displayForgottenPasswordForm(Model model){
        List<Runner> runners = (List<Runner>) runnerRepository.findAll();
        model.addAttribute("title", "forgotten password");
        model.addAttribute("runners", runners);
        return "runners/forgottenPassword";
    }

    @PostMapping("/forgottenPassword")
    public String processForgottenPasswordForm (@RequestParam Integer id){

        return "redirect:/email/passwordResetRequest/"+id;
    }
}
