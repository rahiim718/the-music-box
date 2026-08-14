package com.faveartists.demo.integration.lastfm;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.faveartists.demo.model.SimilarArtistRecommendation;

@Component
public class LastFmHttpClient implements LastFmClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public LastFmHttpClient(RestTemplateBuilder restTemplateBuilder,
            @Value("${lastfm.api-key:}") String apiKey,
            @Value("${lastfm.base-url:https://ws.audioscrobbler.com/2.0/}") String baseUrl,
            @Value("${lastfm.connect-timeout:3s}") Duration connectTimeout,
            @Value("${lastfm.read-timeout:5s}") Duration readTimeout) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    @Override
    @Cacheable(cacheNames = "lastFmSimilarArtists", key = "#p0.toLowerCase() + ':' + #p1")
    public List<SimilarArtistRecommendation> getSimilarArtists(String artistName, int limit) {
        if (!StringUtils.hasText(apiKey)) {
            throw new LastFmUnavailableException("The Last.fm API key is not configured.");
        }

        URI requestUri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("method", "artist.getsimilar")
                .queryParam("artist", artistName)
                .queryParam("api_key", apiKey)
                .queryParam("format", "json")
                .queryParam("autocorrect", 1)
                .queryParam("limit", limit)
                .build()
                .encode()
                .toUri();

        try {
            LastFmResponse response = restTemplate.getForObject(requestUri, LastFmResponse.class);
            if (response == null || response.similarArtists() == null
                    || response.similarArtists().artists() == null) {
                throw new LastFmUnavailableException("Last.fm returned an incomplete response.");
            }

            return response.similarArtists().artists().stream()
                    .filter(Objects::nonNull)
                    .filter(artist -> StringUtils.hasText(artist.name()))
                    .map(artist -> toRecommendation(artist, artistName))
                    .filter(Objects::nonNull)
                    .toList();
        } catch (RestClientException exception) {
            // Do not include the request URI in the exception because it contains the API key.
            throw new LastFmUnavailableException("Last.fm could not be reached.");
        }
    }

    private SimilarArtistRecommendation toRecommendation(LastFmArtist artist, String basedOnArtist) {
        if (!StringUtils.hasText(artist.match())) {
            return null;
        }
        try {
            double match = Double.parseDouble(artist.match());
            return new SimilarArtistRecommendation(
                    artist.name(),
                    artist.musicBrainzId(),
                    match,
                    artist.url(),
                    basedOnArtist);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LastFmResponse(
            @JsonProperty("similarartists") LastFmSimilarArtists similarArtists) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LastFmSimilarArtists(
            @JsonProperty("artist") List<LastFmArtist> artists) {

        private LastFmSimilarArtists {
            artists = artists == null ? Collections.emptyList() : artists;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record LastFmArtist(
            String name,
            @JsonProperty("mbid") String musicBrainzId,
            String match,
            String url) {
    }
}
