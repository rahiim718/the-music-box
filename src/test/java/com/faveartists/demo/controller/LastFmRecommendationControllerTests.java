package com.faveartists.demo.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.faveartists.demo.integration.lastfm.LastFmUnavailableException;
import com.faveartists.demo.model.FaveNewArtist;
import com.faveartists.demo.model.SimilarArtistRecommendation;
import com.faveartists.demo.repository.FaveNewArtistRepository;
import com.faveartists.demo.service.LastFmRecommendationService;

@WebMvcTest(LastFmRecommendationController.class)
class LastFmRecommendationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LastFmRecommendationService recommendationService;

    @MockBean
    private FaveNewArtistRepository faveNewArtistRepository;

    @Test
    void rendersLiveRecommendations() throws Exception {
        SimilarArtistRecommendation recommendation = new SimilarArtistRecommendation(
                "Feid", "mbid", 0.93, "https://www.last.fm/music/Feid", "Bad Bunny");
        when(recommendationService.getRecommendations(anyList(), eq(5)))
                .thenReturn(List.of(recommendation));

        mockMvc.perform(post("/recommendations/similar")
                        .param("artist", "Bad Bunny|Latin")
                        .param("genres", "Latin"))
                .andExpect(status().isOk())
                .andExpect(view().name("similar-recommendations"))
                .andExpect(model().attribute("selectedArtists", "Bad Bunny"))
                .andExpect(model().attributeExists("recommendations"));

        verify(recommendationService).getRecommendations(List.of("Bad Bunny"), 5);
    }

    @Test
    void fallsBackToCuratedRecommendationsWhenLastFmFails() throws Exception {
        FaveNewArtist fallback = new FaveNewArtist(
                "Latin", "Curated Artist", "Biography", "video-id", "image.jpg");
        when(recommendationService.getRecommendations(anyList(), eq(5)))
                .thenThrow(new LastFmUnavailableException("Unavailable"));
        when(faveNewArtistRepository.findDistinctByGenresIn(List.of("Latin")))
                .thenReturn(List.of(fallback));

        mockMvc.perform(post("/recommendations/similar")
                        .param("artist", "Bad Bunny|Latin")
                        .param("genres", "Latin"))
                .andExpect(status().isOk())
                .andExpect(view().name("recommendations"))
                .andExpect(model().attributeExists("recommendations"))
                .andExpect(model().attributeExists("recommendationNotice"));

        verify(faveNewArtistRepository).findDistinctByGenresIn(List.of("Latin"));
    }
}
