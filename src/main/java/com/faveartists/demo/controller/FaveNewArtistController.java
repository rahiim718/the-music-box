package com.faveartists.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.faveartists.demo.model.FaveNewArtist;
import com.faveartists.demo.repository.FaveNewArtistRepository;

import java.util.List;

@Controller
public class FaveNewArtistController {

    @Autowired
    private FaveNewArtistRepository faveNewArtistRepository;

    // Handle the GET request for a single genre, for backward compatibility or other uses
    @GetMapping("/recommendations/{genre}")
    public String showRecommendations(@PathVariable String genre, Model model) {
        List<FaveNewArtist> filteredArtists = faveNewArtistRepository.findDistinctByGenresIn(List.of(genre));
        model.addAttribute("recommendations", filteredArtists);
        return "recommendations"; 
    }

    // Handle the POST request for multiple genres
    @PostMapping("/recommendations")
    public String showRecommendationsForGenres(@RequestParam List<String> genres, Model model) {
        List<FaveNewArtist> filteredArtists = faveNewArtistRepository.findDistinctByGenresIn(genres);
        model.addAttribute("recommendations", filteredArtists);
        return "recommendations"; 
    }
}
