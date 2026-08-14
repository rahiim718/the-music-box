package com.faveartists.demo.model;

public class SimilarArtistRecommendation {

    private final String name;
    private final String musicBrainzId;
    private final double similarityScore;
    private final String lastFmUrl;
    private final String basedOnArtist;

    public SimilarArtistRecommendation(String name, String musicBrainzId, double similarityScore,
            String lastFmUrl, String basedOnArtist) {
        this.name = name;
        this.musicBrainzId = musicBrainzId;
        this.similarityScore = similarityScore;
        this.lastFmUrl = lastFmUrl;
        this.basedOnArtist = basedOnArtist;
    }

    public String getName() {
        return name;
    }

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public long getSimilarityPercent() {
        return Math.round(similarityScore * 100);
    }

    public String getLastFmUrl() {
        return lastFmUrl;
    }

    public String getBasedOnArtist() {
        return basedOnArtist;
    }
}
