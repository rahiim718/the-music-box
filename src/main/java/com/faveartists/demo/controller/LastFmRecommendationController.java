package com.faveartists.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.faveartists.demo.integration.lastfm.LastFmUnavailableException;
import com.faveartists.demo.model.FaveNewArtist;
import com.faveartists.demo.model.SimilarArtistRecommendation;
import com.faveartists.demo.repository.FaveNewArtistRepository;
import com.faveartists.demo.service.LastFmRecommendationService;

@Controller
public class LastFmRecommendationController {

    private static final int RESULT_LIMIT = 5;

    private final LastFmRecommendationService recommendationService;
    private final FaveNewArtistRepository faveNewArtistRepository;

    public LastFmRecommendationController(
            LastFmRecommendationService recommendationService,
            FaveNewArtistRepository faveNewArtistRepository) {
        this.recommendationService = recommendationService;
        this.faveNewArtistRepository = faveNewArtistRepository;
    }

    @PostMapping("/recommendations/similar")
    public String showSimilarArtists(
            @RequestParam("artist") List<String> artistSelections,
            @RequestParam(value = "genres", required = false) List<String> genres,
            Model model) {
        List<String> artistNames = artistSelections.stream()
                .map(this::extractArtistName)
                .toList();

        try {
            List<SimilarArtistRecommendation> recommendations =
                    recommendationService.getRecommendations(artistNames, RESULT_LIMIT);
            if (!recommendations.isEmpty()) {
                model.addAttribute("recommendations", recommendations);
                model.addAttribute("selectedArtists", String.join(", ", artistNames));
                return "similar-recommendations";
            }
            return showCuratedFallback(genres, model,
                    "Last.fm did not return usable matches, so these are your original curated recommendations.");
        } catch (LastFmUnavailableException exception) {
            return showCuratedFallback(genres, model,
                    "Live recommendations are temporarily unavailable, so these are your original curated recommendations.");
        }
    }

    private String showCuratedFallback(List<String> genres, Model model, String notice) {
        List<FaveNewArtist> fallbackRecommendations = genres == null
                ? List.of()
                : faveNewArtistRepository.findDistinctByGenresIn(genres);
        model.addAttribute("recommendations", fallbackRecommendations);
        model.addAttribute("recommendationNotice", notice);
        return "recommendations";
    }

    private String extractArtistName(String artistSelection) {
        int separatorPosition = artistSelection.lastIndexOf('|');
        return separatorPosition < 0
                ? artistSelection.trim()
                : artistSelection.substring(0, separatorPosition).trim();
    }
}
