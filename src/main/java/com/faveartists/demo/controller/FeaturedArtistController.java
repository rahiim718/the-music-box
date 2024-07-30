package com.faveartists.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.faveartists.demo.model.FeaturedArtist;
import com.faveartists.demo.service.FeaturedArtistService;

@Controller
public class FeaturedArtistController {

    @Autowired
    private FeaturedArtistService featuredArtistService;


    @GetMapping("/select")
    public String selectArtist(@RequestParam String firstName, Model model) {
        List<FeaturedArtist> featuredArtists = featuredArtistService.getAllFeaturedArtists();
        model.addAttribute("featuredArtists", featuredArtists);
        model.addAttribute("firstName", firstName); // add the name to the model
        return "select";
    }

}
