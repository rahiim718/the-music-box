package com.faveartists.demo.controller;

import com.faveartists.demo.service.FeaturedArtistService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.faveartists.demo.model.FeaturedArtist;
import com.faveartists.demo.model.WelcomePage;
import com.faveartists.demo.service.WelcomePageService;


@Controller
public class WelcomePageController {

    @Autowired
    private FeaturedArtistService featuredArtistService;
    
    @Autowired
    private WelcomePageService welcomeService;

    public WelcomePageController(WelcomePageService repoService){
        this.welcomeService = repoService;
    }


    @GetMapping("/")
    public String defaultWelcome(Model page){
        return "landingpage2"; 
    }

    @GetMapping("/landingpage")
    public String welcompage(Model page){
        return "landingpage2"; 
    }

    @PostMapping("/form")
    public String form (Model model){
        return "landingpageform";
    }

    @PostMapping("/signform")
    public String signform (Model model){
        return "signinform";
    }

    @PostMapping("/landingpageform")
    public String addWelcome(
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam String emailAddress,
        @RequestParam String userPassword,
        Model model)
        {
            if(firstName.isBlank() || lastName.isBlank()  || emailAddress.isBlank() || userPassword.isBlank()){ //Generates an error page if user doesn't enter the required fields
                model.addAttribute("invalidInfo", "Please enter information in all required fields.");
                return "erroraddlanding"; //need to code the error page
            }

            WelcomePage page = new WelcomePage(); //Creates an object of the Welcome page so that information fields are created for the database
            page.setFirstName(firstName); //Setting the information entered by user into the designated fields
            page.setLastName(lastName);
            page.setEmailAddress(emailAddress);
            page.setUserPassword(userPassword);
            welcomeService.addPage(page); //Stores information in H2 database . This knows the first and last name.
            model.addAttribute("firstName", page); //This gives the attribute in quotes of what will be displayed to the screen
            model.addAttribute("featuredArtists", featuredArtistService.getAllFeaturedArtists());
            return "select"; //Takes user to the intro page which prompts to the Featured artist page. Pge link is receiving information from this method
        }
        
    @PostMapping("/signinform")
    public String signIn(
        @RequestParam String emailAddress, 
        @RequestParam String userPassword, 
        Model model) {
        // Call the service method to check if the user exists
        List<WelcomePage> users = welcomeService.findByEmailAddressAndUserPassword(emailAddress, userPassword);

        if (!users.isEmpty()) {
            // User with the provided credentials was found
            // Assuming there's only one matching user, you can get their first name
            String firstName = users.get(0).getFirstName();

            // Add the first name to the model to pass it to the next page
            model.addAttribute("firstName", firstName);
            model.addAttribute("featuredArtists", featuredArtistService.getAllFeaturedArtists());

            // Redirect to the next page (e.g., greeting page)
            return "signinselect"; // Adjust the page name as needed
        } else {
            // No matching user found, handle sign-in failure (e.g., display an error message)
            // model.addAttribute("error", "Invalid email address or password.");
            model.addAttribute("invalidInfo", "Invalid email address or password.");
            return "errorsignpage"; // errorpage
        }
    }

        
        @GetMapping("/about")
        public String aboutPage() {
            return "about"; 
        }

        @GetMapping("/bio")
        public String bioPage() {
            return "bio";
        }

        @GetMapping("/signin")
        public String signinPage() {
            return "signinform"; 
        }

        @GetMapping("/signup")
        public String signupPage() {
            return "landingpageform";
        }

        @GetMapping("/guest")
        public String enterPage(Model model) {
        List<FeaturedArtist> featuredArtists = featuredArtistService.getAllFeaturedArtists();
        model.addAttribute("featuredArtists", featuredArtists);
            return "guestselect";
        }
    
    }

