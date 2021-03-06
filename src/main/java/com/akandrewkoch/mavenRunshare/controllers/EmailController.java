package com.akandrewkoch.mavenRunshare.controllers;

import com.akandrewkoch.mavenRunshare.models.DTO.NewPasswordDTO;
import com.akandrewkoch.mavenRunshare.models.DTO.NewRunnerRegistrationDTO;
import com.akandrewkoch.mavenRunshare.models.JavaEmail;
import com.akandrewkoch.mavenRunshare.models.Runner;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;



@Controller
@RequestMapping("/email")
public class EmailController extends MainController {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public static void setUserInSession(HttpSession session, Runner runner) {
        session.setAttribute(runnerSessionKey, runner.getId());
    }

    @GetMapping("/passwordResetRequest/{id}")
    public String displayPasswordResetRequestPage(HttpServletRequest request, HttpSession session, @PathVariable Integer id, Model model) {
        setRunnerInModel(request, model);
        model.addAttribute("title", "request password reset");
        model.addAttribute("resettingRunner", runnerRepository.findById(id).get());
        return "email/passwordResetRequest";
    }

    @PostMapping("/passwordResetRequest/{id}")
    public String processPasswordResetRequestPage(@PathVariable Integer id) {

        Runner resettingRunner = runnerRepository.findById(id).get();
        JavaEmail javaEmail = new JavaEmail();
        String passwordTempRequest = encoder.encode(resettingRunner.getPwHash());
        String subjectLine = "Hey " + resettingRunner.getCallsign() + ", heard you lost your password!";
        String content = "<body style=\"text-align:center;\">"+
                "<h1>" + resettingRunner.getCallsign() + ", we were notified you were requesting to reset your password for RunShare.</h1>" +
                "<div>Sorry to hear about that, let's get you back up and running.</div>" +
                "<div>Click the link below to reset your password, and let's get you back on the trail!</div>" +
                "<div style=\"text-align:center;\">"+
                "<a href=\"" + System.getenv("ENVIRONMENT_URL") + "/email/passwordReset?id=" + resettingRunner.getId() + "&pwhash=" + passwordTempRequest + "\">Reset Password</a>"+
                "</div>"+
                "<br/>"+
                "<small>If you did not request a password reset, you may ignore this email</small> "+
                "</body>";

        if (System.getenv("ENVIRONMENT_URL").equals("http://localhost:8080")){
            content+="sent via development mode";
        }
        javaEmail.sendEmail(resettingRunner.getEmail(), System.getenv("SENDING_EMAIL_ADDRESS"), subjectLine, content);
        resettingRunner.setPasswordTempRequest(passwordTempRequest);
        runnerRepository.save(resettingRunner);
        return "redirect:/";
    }

    @GetMapping("/passwordReset")
    public String displayPasswordResetForm(@RequestParam Integer id, @RequestParam String pwhash, Model model) {
        Runner resettingRunner = runnerRepository.findById(id).get();
        model.addAttribute("title", "password reset");

        if (pwhash.equals(resettingRunner.getPasswordTempRequest())) {
            model.addAttribute("validRequest", true);
            model.addAttribute("resettingRunner", resettingRunner);
            NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
            model.addAttribute(newPasswordDTO);
        }

        resettingRunner.setPasswordTempRequest(null);
        runnerRepository.save(resettingRunner);

        return "email/passwordReset";
    }

    @PostMapping("/passwordReset")
    public String processPasswordResetForm (@ModelAttribute @Valid NewPasswordDTO newPasswordDTO, Errors errors, Model model, @RequestParam Integer id, HttpServletRequest request){
        model.addAttribute("title", "password reset");
        Runner resettingRunner = runnerRepository.findById(id).get();
        model.addAttribute("resettingRunner", resettingRunner);
        if (errors.hasErrors()){
            model.addAttribute("validRequest", true);
            model.addAttribute(newPasswordDTO);
            return "email/passwordReset";
        }

        String password = newPasswordDTO.getPassword();
        String verifyPassword = newPasswordDTO.getVerifyPassword();

        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "password.doesNotMatch", "The passwords do not match");
            model.addAttribute("validRequest", true);
            model.addAttribute("newPasswordDTO", newPasswordDTO);
            return "email/passwordReset";
        }

        if (resettingRunner.isMatchingPassword(password) || resettingRunner.isMatchingPreviousPWHash(password)){
            errors.rejectValue("password", "password.isCurrentPassword", "Please do not use current or previous password");
            model.addAttribute("validRequest", true);
            model.addAttribute("newPasswordDTO", newPasswordDTO);
            return "email/passwordReset";
        }

        resettingRunner.setPreviousPWHash(resettingRunner.getPwHash());
        resettingRunner.setPassword(newPasswordDTO.getPassword());
        runnerRepository.save(resettingRunner);
        JavaEmail javaEmail = new JavaEmail();
        String passwordResetMessage = "<body style=\"text-align:center\">"+
                "<h1>Password reset!</h1>"+
                "<h2>Congratulations "+resettingRunner.getCallsign()+", your password has been reset!</h2>"+
                "<p>If you did not reset your password, please go to your account and reset your password immediately, and contact a RunShare administrator<p>"+
                "<div style=\"text-align:center\">"+
                "<a href=\""+System.getenv("ENVIRONMENT_URL")+"\">Go to RunShare</a>"+
                "</div>"+
                "</body>";
        if (System.getenv("ENVIRONMENT_URL").equals("http://localhost:8080")){
            passwordResetMessage+="sent via development mode";
        }
        javaEmail.sendEmail(resettingRunner.getEmail(), System.getenv("SENDING_EMAIL_ADDRESS"), "Password reset for "+resettingRunner.getCallsign(), passwordResetMessage);
        setUserInSession(request.getSession(), resettingRunner);
        return "redirect:/runners";
    }

    @GetMapping("/contactAdmin")
    public String displayContactAdminForm (Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "contact Admins");
        return "email/contactAdmin";
    }

    @PostMapping("/contactAdmin")
    public String processContactAdminForm (@RequestParam String subject, @RequestParam String content, Model model, HttpSession session){
        Runner contactingRunner = getRunnerFromSession(session);
        subject = Jsoup.clean(subject, Whitelist.none());
        content = Jsoup.clean(content, Whitelist.none());
        content = "<body>"+
                "<h1>"+content+"</h1>"+
                "<h2>From: "+contactingRunner.getCallsign()+"</h2>"+
                "<h3>At: "+contactingRunner.getEmail()+"</h3>"+
                "</body>";
        JavaEmail javaEmail = new JavaEmail();
        if (System.getenv("ENVIRONMENT_URL").equals("http://localhost:8080")){
            content += "sent via development mode";
        }
        javaEmail.sendEmail(System.getenv("SENDING_EMAIL_ADDRESS"), contactingRunner.getEmail(), subject, content);
        return "redirect:/runners/runnerDetails/"+contactingRunner.getId()+"?emailSent="+subject;

    }
}
