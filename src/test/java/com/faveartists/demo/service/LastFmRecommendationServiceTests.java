package com.faveartists.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.faveartists.demo.integration.lastfm.LastFmClient;
import com.faveartists.demo.integration.lastfm.LastFmUnavailableException;
import com.faveartists.demo.model.SimilarArtistRecommendation;

class LastFmRecommendationServiceTests {

    @Test
    void combinesRanksAndDeduplicatesResults() {
        LastFmClient client = (artistName, limit) -> switch (artistName) {
            case "Bad Bunny" -> List.of(
                    recommendation("Feid", 0.80, artistName),
                    recommendation("Bad Bunny", 1.00, artistName),
                    recommendation("Rauw Alejandro", 0.70, artistName));
            case "Ozuna" -> List.of(
                    recommendation("feid", 0.95, artistName),
                    recommendation("Myke Towers", 0.85, artistName));
            default -> List.of();
        };
        LastFmRecommendationService service = new LastFmRecommendationService(client);

        List<SimilarArtistRecommendation> results =
                service.getRecommendations(List.of("Bad Bunny", "Ozuna"), 5);

        assertThat(results).extracting(SimilarArtistRecommendation::getName)
                .containsExactly("feid", "Myke Towers", "Rauw Alejandro");
        assertThat(results.get(0).getSimilarityScore()).isEqualTo(0.95);
        assertThat(results).noneMatch(artist -> artist.getName().equals("Bad Bunny"));
    }

    @Test
    void limitsTheNumberOfResults() {
        LastFmClient client = (artistName, limit) -> List.of(
                recommendation("Artist A", 0.90, artistName),
                recommendation("Artist B", 0.80, artistName),
                recommendation("Artist C", 0.70, artistName));
        LastFmRecommendationService service = new LastFmRecommendationService(client);

        List<SimilarArtistRecommendation> results =
                service.getRecommendations(List.of("Source Artist"), 2);

        assertThat(results).hasSize(2);
        assertThat(results).extracting(SimilarArtistRecommendation::getName)
                .containsExactly("Artist A", "Artist B");
    }

    @Test
    void throwsWhenEveryLastFmRequestFails() {
        LastFmClient client = (artistName, limit) -> {
            throw new LastFmUnavailableException("Unavailable");
        };
        LastFmRecommendationService service = new LastFmRecommendationService(client);

        assertThatThrownBy(() -> service.getRecommendations(List.of("Bad Bunny"), 5))
                .isInstanceOf(LastFmUnavailableException.class);
    }

    @Test
    void keepsSuccessfulResultsWhenOnlyOneRequestFails() {
        LastFmClient client = (artistName, limit) -> {
            if (artistName.equals("Unavailable Artist")) {
                throw new LastFmUnavailableException("Unavailable");
            }
            return List.of(recommendation("Feid", 0.88, artistName));
        };
        LastFmRecommendationService service = new LastFmRecommendationService(client);

        List<SimilarArtistRecommendation> results = service.getRecommendations(
                List.of("Unavailable Artist", "Bad Bunny"), 5);

        assertThat(results).extracting(SimilarArtistRecommendation::getName)
                .containsExactly("Feid");
    }

    private SimilarArtistRecommendation recommendation(String name, double score, String basedOnArtist) {
        return new SimilarArtistRecommendation(
                name,
                "musicbrainz-id",
                score,
                "https://www.last.fm/music/" + name.replace(' ', '+'),
                basedOnArtist);
    }
}
