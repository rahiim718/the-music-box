package com.faveartists.demo.service;

import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faveartists.demo.model.WelcomePage;
import com.faveartists.demo.repository.WelcomePageRepository;

@Service
public class WelcomePageService {
    private  final WelcomePageRepository pageRepo;

    public WelcomePageService(WelcomePageRepository repo) {
        this.pageRepo = repo;
    }

    public List<WelcomePage> findWelcomePage(String firstName, String lastName){
        return pageRepo.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<WelcomePage> findByEmailAddressAndUserPassword(String emailAddress, String userPassword) {
        return pageRepo.findByEmailAddressAndUserPassword(emailAddress, userPassword);
    }
    

    

    // public void addFirstName(WelcomePage wp){ //This method passes in the Welcome page
    //     pageRepo.save(wp);   //Saves the welcome page to the repo
    // } 

    public void addPage(WelcomePage wp){ //Changed the name of the method
        pageRepo.save(wp);
    }


}
