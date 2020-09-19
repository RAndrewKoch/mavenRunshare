package com.akandrewkoch.mavenRunshare.controllers;


import com.akandrewkoch.mavenRunshare.models.DTO.NewTrailDTO;
import com.akandrewkoch.mavenRunshare.models.DTO.NewTrailDifficultyDTO;
import com.akandrewkoch.mavenRunshare.models.DTO.NewTrailSceneryDTO;
import com.akandrewkoch.mavenRunshare.models.Trail;
import com.akandrewkoch.mavenRunshare.models.TrailDifficultyRating;
import com.akandrewkoch.mavenRunshare.models.TrailSceneryRating;
import com.akandrewkoch.mavenRunshare.models.enums.TrailDifficulty;
import com.akandrewkoch.mavenRunshare.models.enums.TrailScenery;
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
@RequestMapping("/trails")
public class TrailController extends MainController{

    //todo-create an edit view for Trails
    @GetMapping(value={"", "/{sortType}"})
    public String displayTrailIndex (@PathVariable(required=false) String sortType, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Trail List");
        if (sortType!=null) {
            switch (sortType) {
                case "nameAsc":
                    model.addAttribute("sortType", "ascending name");
                    model.addAttribute("trails", trailRepository.findAllByOrderByNameAsc());
                    return "trails/index";
                case "nameDesc":
                    model.addAttribute("sortType", "descending name");
                    model.addAttribute("trails", trailRepository.findAllByOrderByNameDesc());
                    return "trails/index";
                case "addressAsc":
                    model.addAttribute("sortType", "ascending location");
                    model.addAttribute("trails", trailRepository.findAllByOrderByAddressAsc());
                    return "trails/index";
                case "addressDesc":
                    model.addAttribute("sortType", "descending location");
                    model.addAttribute("trails", trailRepository.findAllByOrderByAddressDesc());
                    return "trails/index";
                case "milesAsc":
                    model.addAttribute("sortType", "ascending length");
                    model.addAttribute("trails", trailRepository.findAllByOrderByMilesAsc());
                    return "trails/index";
                case "milesDesc":
                    model.addAttribute("sortType", "descending length");
                    model.addAttribute("trails", trailRepository.findAllByOrderByMilesDesc());
                    return "trails/index";
            }
        }

        Iterable<TrailDifficultyRating> grabDifficultiesList = trailDifficultyRatingRepository.findAll();
        List<TrailDifficultyRating> trailDifficulties = new ArrayList<>();
        grabDifficultiesList.forEach(trailDifficulties::add);

        Iterable<TrailSceneryRating> grabSceneryRatingsList = trailSceneryRatingRepository.findAll();
        List<TrailSceneryRating> trailSceneryRatings = new ArrayList<>();
        grabSceneryRatingsList.forEach(trailSceneryRatings::add);

        model.addAttribute("trailDifficulties", trailDifficulties);
        model.addAttribute("trailSceneryRatings", trailSceneryRatings);
        model.addAttribute("trails", trailRepository.findAll());
        return "trails/index";
    }

    @GetMapping("/addTrail")
    private String displayAddTrailView (Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Trail");
        model.addAttribute("difficulties", TrailDifficulty.values());
        model.addAttribute("sceneryValues", TrailScenery.values());
        model.addAttribute(new NewTrailDTO());
        model.addAttribute(new NewTrailDifficultyDTO());
        model.addAttribute(new NewTrailSceneryDTO());
        return "trails/addTrail";
    }

    @PostMapping("/addTrail")
    private String processAddTrailForm (@ModelAttribute @Valid NewTrailDTO newTrailDTO, Errors errors, @ModelAttribute @Valid NewTrailDifficultyDTO newTrailDifficultyDTO, @ModelAttribute @Valid NewTrailSceneryDTO newTrailSceneryDTO, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Trail");
        model.addAttribute("difficulties", TrailDifficulty.values());
        model.addAttribute("sceneryValues", TrailScenery.values());
        if (errors.hasErrors()){
            model.addAttribute("title", "Add Trail");
            return "trails/addTrail";
        }

        Trail checkedTrail = trailRepository.findByName(newTrailDTO.getName());

        if (checkedTrail!=null){
            errors.rejectValue("name", "name.alreadyExists", "A Trail of that name already exists!");
            model.addAttribute("title", "Add Trail");
            return "trails/addTrail";
        }

        Trail newTrail = new Trail(newTrailDTO.getName(), newTrailDTO.getMiles(), newTrailDTO.getAddress(), newTrailDTO.getZipCode());
        trailRepository.save(newTrail);

        HttpSession session = request.getSession();
        if (newTrailDifficultyDTO.getTrailDifficulty()!=null){
            TrailDifficultyRating newTrailDifficultyRating = new TrailDifficultyRating(newTrailDifficultyDTO.getTrailDifficulty(), getRunnerFromSession(session), newTrail);
            trailDifficultyRatingRepository.save(newTrailDifficultyRating);
        }

        if (newTrailSceneryDTO.getTrailScenery()!=null){
            TrailSceneryRating newTrailSceneryRating = new TrailSceneryRating(newTrailSceneryDTO.getTrailScenery(), getRunnerFromSession(session), newTrail);
            trailSceneryRatingRepository.save(newTrailSceneryRating);
        }
        model.addAttribute("title", "Trail List");
        model.addAttribute("trails", trailRepository.findAll());
        return "redirect:/trails";
    }



    @GetMapping("/trailDetails/{id}")
    public String displayTrailDetails (@PathVariable Integer id, Model model, HttpServletRequest request, HttpSession session){
        setRunnerInModel(request, model);

        //testing to make sure the id is a real trail and reuturning the trail index if not
        Optional<Trail> testTrail = trailRepository.findById(id);
        if (testTrail.isEmpty()){
            return "trails/";
        }
        Trail detailedTrail = testTrail.get();

        //testing to see if the current runner has a rating on this trail
        Integer runnerId = (Integer) session.getAttribute(runnerSessionKey);
        if (runnerId != null){
            Optional<TrailDifficultyRating> testDifficulty = Optional.ofNullable(trailDifficultyRatingRepository.findByRunner_IdAndTrail_Id(runnerId, id));
            if (testDifficulty.isPresent()) {
                TrailDifficultyRating detailedTrailDifficultyRating = testDifficulty.get();
                model.addAttribute("runnerTrailRating", detailedTrailDifficultyRating.getDifficulty().getNumberLevel());
            }

            Optional<TrailSceneryRating> testScenery = Optional.ofNullable(trailSceneryRatingRepository.findByRunner_IdAndTrail_Id(runnerId, id));
            if (testScenery.isPresent()) {
                TrailSceneryRating detailedTrailSceneryRating = testScenery.get();
                model.addAttribute("runnerSceneryRating", detailedTrailSceneryRating.getScenery().getNumberLevel());
            }
        }
        Iterable<TrailDifficultyRating> grabDifficultiesList = trailDifficultyRatingRepository.findAll();
        List<TrailDifficultyRating> trailDifficulties = new ArrayList<>();
        grabDifficultiesList.forEach(trailDifficulties::add);

        Iterable<TrailSceneryRating> grabSceneryList = trailSceneryRatingRepository.findAll();
        List<TrailSceneryRating> trailSceneryRatings= new ArrayList<>();
        grabSceneryList.forEach(trailSceneryRatings::add);

        model.addAttribute("trailDifficulties", trailDifficulties);
        model.addAttribute("trailSceneryRatings", trailSceneryRatings);
        model.addAttribute("comments", commentRepository.findByTrail_IdOrderByDateCreatedDescTimeCreatedDesc(id));
        model.addAttribute("title", "Trail Details");
        model.addAttribute("detailedTrail",detailedTrail);
        return "trails/trailDetails";
    }

    @GetMapping("/trailDetails")
    public String displayTrailDetailsBlank (Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Blank");
        model.addAttribute("detailedTrail", new Trail("Blank", 0, "Blank", "Blank"));
        return "trails/trailDetails";
    }


    private int getTrailDifficultyAverage(int id){
        List<TrailDifficultyRating> listOfTrailDifficulties = trailDifficultyRatingRepository.findAllByTrail_Id(id);
        int difficultySum=0;
        int avgDifficulty;
        int count=0;
        if (listOfTrailDifficulties.isEmpty()){
            return 6;
        } else {
            for (int i = 0; i<listOfTrailDifficulties.size(); i++){
                difficultySum+= listOfTrailDifficulties.get(i).getDifficulty().getNumberLevel();
                count ++;
            }
            avgDifficulty= Math.round(difficultySum/count);
        }
        return avgDifficulty;
    }

    @GetMapping("/addDifficulty/{runnerId}/{trailId}")
    public String displayAddDifficultyView (@PathVariable int runnerId, @PathVariable int trailId, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Difficulty");
        model.addAttribute("trailDifficulties", TrailDifficulty.values());
        model.addAttribute(new NewTrailDifficultyDTO());
        model.addAttribute("trail", trailRepository.findById(trailId));
        return "trails/addDifficulty";
    }

    @GetMapping("/editDifficulty/{runnerId}/{trailId}")
    public String displayEditDifficultyView (@PathVariable int trailId, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute(new NewTrailDifficultyDTO());
        model.addAttribute("title", "Edit Difficulty");
        model.addAttribute("trailDifficulties", TrailDifficulty.values());
        model.addAttribute("trail", trailRepository.findById(trailId));
        return "trails/addDifficulty";
    }

    @PostMapping("/addDifficulty/{runnerId}/{trailId}")
    public String processAddDifficultyForm (@ModelAttribute @Valid NewTrailDifficultyDTO newTrailDifficultyDTO, @PathVariable int runnerId, @PathVariable int trailId, Errors errors, Model model, HttpServletRequest request ){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Difficulty");
        if (errors.hasErrors()){
            model.addAttribute("trailDifficulties", TrailDifficulty.values());
            return "trails/addDifficulty/"+runnerId+"/"+trailId;
        }
        TrailDifficultyRating newTrail = new TrailDifficultyRating(newTrailDifficultyDTO.getTrailDifficulty(), runnerRepository.findById(runnerId), trailRepository.findById(trailId));
        trailDifficultyRatingRepository.save(newTrail);
        return "redirect:/trails";
    }

    @PostMapping("/editDifficulty/{runnerId}/{trailId}")
    public String processEditDifficultyForm (@ModelAttribute @Valid NewTrailDifficultyDTO newTrailDifficultyDTO, @PathVariable int runnerId, @PathVariable int trailId, Errors errors, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Edit Difficulty");
        if (errors.hasErrors()){
            model.addAttribute("trailDifficulties", TrailDifficulty.values());
            return "trails/editDifficulty/"+runnerId+"/"+trailId;
        }
        TrailDifficultyRating editedTrail = trailDifficultyRatingRepository.findByRunner_IdAndTrail_Id(runnerId, trailId);
        editedTrail.setDifficulty(newTrailDifficultyDTO.getTrailDifficulty());
        trailDifficultyRatingRepository.save(editedTrail);
        return "redirect:/trails";
    }


//    scenery block
@GetMapping("/addScenery/{runnerId}/{trailId}")
public String displayAddSceneryView (@PathVariable int runnerId, @PathVariable int trailId, Model model, HttpServletRequest request){
    setRunnerInModel(request, model);
    model.addAttribute("title", "Add Scenery");
    model.addAttribute("trailSceneryValues", TrailScenery.values());
    model.addAttribute(new NewTrailSceneryDTO());
    model.addAttribute("trail", trailRepository.findById(trailId));
    return "trails/addScenery";
}

    @GetMapping("/editScenery/{runnerId}/{trailId}")
    public String displayEditSceneryView (@PathVariable int trailId, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute(new NewTrailSceneryDTO());
        model.addAttribute("title", "Edit Scenery");
        model.addAttribute("trailSceneryValues", TrailScenery.values());
        model.addAttribute("trail", trailRepository.findById(trailId));
        return "trails/addScenery";
    }

    @PostMapping("/addScenery/{runnerId}/{trailId}")
    public String processAddSceneryForm (@ModelAttribute @Valid NewTrailSceneryDTO newTrailSceneryDTO, @PathVariable int runnerId, @PathVariable int trailId, Errors errors, Model model, HttpServletRequest request ){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Add Scenery");
        if (errors.hasErrors()){
            model.addAttribute("trailSceneryValues", TrailScenery.values());
            return "trails/addScenery/"+runnerId+"/"+trailId;
        }
        TrailSceneryRating newTrail = new TrailSceneryRating(newTrailSceneryDTO.getTrailScenery(), runnerRepository.findById(runnerId), trailRepository.findById(trailId));
        trailSceneryRatingRepository.save(newTrail);
        return "redirect:/trails";
    }

    @PostMapping("/editScenery/{runnerId}/{trailId}")
    public String processEditSceneryForm (@ModelAttribute @Valid NewTrailSceneryDTO newTrailSceneryDTO, @PathVariable int runnerId, @PathVariable int trailId, Errors errors, Model model, HttpServletRequest request){
        setRunnerInModel(request, model);
        model.addAttribute("title", "Edit Scenery");
        if (errors.hasErrors()){
            model.addAttribute("trailSceneryValues", TrailScenery.values());
            return "trails/editScenery/"+runnerId+"/"+trailId;
        }
        TrailSceneryRating editedTrail = trailSceneryRatingRepository.findByRunner_IdAndTrail_Id(runnerId, trailId);
        editedTrail.setScenery(newTrailSceneryDTO.getTrailScenery());
        trailSceneryRatingRepository.save(editedTrail);
        return "redirect:/trails";
    }

}
