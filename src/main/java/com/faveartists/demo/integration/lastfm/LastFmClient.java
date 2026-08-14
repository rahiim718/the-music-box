package com.faveartists.demo.integration.lastfm;

import java.util.List;

import com.faveartists.demo.model.SimilarArtistRecommendation;

public interface LastFmClient {

    List<SimilarArtistRecommendation> getSimilarArtists(String artistName, int limit);
}
