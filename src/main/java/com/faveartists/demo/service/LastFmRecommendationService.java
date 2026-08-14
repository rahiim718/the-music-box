package com.faveartists.demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.faveartists.demo.integration.lastfm.LastFmClient;
import com.faveartists.demo.integration.lastfm.LastFmUnavailableException;
import com.faveartists.demo.model.SimilarArtistRecommendation;

@Service
public class LastFmRecommendationService {

    private static final int RESULTS_REQUESTED_PER_ARTIST = 10;

    private final LastFmClient lastFmClient;

    public LastFmRecommendationService(LastFmClient lastFmClient) {
        this.lastFmClient = lastFmClient;
    }

    public List<SimilarArtistRecommendation> getRecommendations(List<String> selectedArtists, int resultLimit) {
        List<String> usableSelections = selectedArtists == null ? List.of() : selectedArtists.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .toList();

        if (usableSelections.isEmpty() || resultLimit < 1) {
            return List.of();
        }

        Set<String> selectedNames = usableSelections.stream()
                .map(this::normalizeName)
                .collect(Collectors.toSet());
        Map<String, SimilarArtistRecommendation> uniqueRecommendations = new LinkedHashMap<>();
        List<LastFmUnavailableException> failures = new ArrayList<>();
        int successfulRequests = 0;

        for (String selectedArtist : usableSelections) {
            try {
                List<SimilarArtistRecommendation> candidates =
                        lastFmClient.getSimilarArtists(selectedArtist, RESULTS_REQUESTED_PER_ARTIST);
                successfulRequests++;

                for (SimilarArtistRecommendation candidate : candidates) {
                    String normalizedCandidate = normalizeName(candidate.getName());
                    if (selectedNames.contains(normalizedCandidate)) {
                        continue;
                    }

                    uniqueRecommendations.merge(
                            normalizedCandidate,
                            candidate,
                            this::keepStrongerMatch);
                }
            } catch (LastFmUnavailableException exception) {
                failures.add(exception);
            }
        }

        if (successfulRequests == 0 && !failures.isEmpty()) {
            throw failures.get(0);
        }

        return uniqueRecommendations.values().stream()
                .sorted(Comparator.comparingDouble(SimilarArtistRecommendation::getSimilarityScore).reversed())
                .limit(resultLimit)
                .toList();
    }

    private SimilarArtistRecommendation keepStrongerMatch(
            SimilarArtistRecommendation current,
            SimilarArtistRecommendation replacement) {
        return replacement.getSimilarityScore() > current.getSimilarityScore() ? replacement : current;
    }

    private String normalizeName(String artistName) {
        return artistName.trim().toLowerCase(Locale.ROOT);
    }
}
