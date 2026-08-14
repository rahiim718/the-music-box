package com.faveartists.demo.integration.lastfm;

public class LastFmUnavailableException extends RuntimeException {

    public LastFmUnavailableException(String message) {
        super(message);
    }
}
