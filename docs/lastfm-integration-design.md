# Last.fm Integration Design

## Goal

Add live similar-artist discovery without replacing The Music Box's original curated, genre-based experience. The `master` branch remains the before version; this feature branch is the after version.

## User experience

The existing artist-selection page has two actions:

1. **Original Genre Recommendations** posts to `/recommendations` and uses the local H2 data exactly as before.
2. **Live Last.fm Recommendations** posts to `/recommendations/similar`, requests similar artists from Last.fm, and displays the five strongest unique matches.

If Last.fm is unavailable, misconfigured, or returns no usable matches, the live route renders the original curated recommendations with a friendly notice.

## Components

| Component | Responsibility |
| --- | --- |
| `LastFmClient` | Defines the external API boundary. |
| `LastFmHttpClient` | Builds the Last.fm request, applies timeouts, maps JSON, and caches successful responses. |
| `LastFmRecommendationService` | Combines results for up to three selected artists, removes selected artists and duplicates, ranks by similarity, and returns five results. |
| `LastFmRecommendationController` | Handles the live form submission and switches to the curated fallback when necessary. |
| `SimilarArtistRecommendation` | View-facing model kept separate from the existing JPA entity. |

## Request flow

1. The browser posts the selected artist names and genres.
2. The controller extracts artist names from the existing `name|genre` form values.
3. The service requests ten similar artists per selected artist.
4. Last.fm responses are cached for 12 hours using Caffeine.
5. The service removes the original selections, merges duplicate names, keeps the stronger duplicate score, sorts descending, and returns five.
6. The controller renders `similar-recommendations.html` or uses the local repository fallback.

## Security and reliability

- `LASTFM_API_KEY` is read only on the Spring server and is never committed or sent to browser JavaScript.
- Connect and read timeouts prevent a slow API from holding the page indefinitely.
- Exceptions deliberately omit the request URI because it contains the API key.
- The local curated database remains the fallback.
- The external response is stored in a separate view model instead of being trusted as persistent application data.

## Configuration

```text
LASTFM_API_KEY=your-key-here
```

The remaining defaults are in `application.properties`. No API key is required to start the app; without one, the live route safely uses the original recommendations.

## Future phases

1. Add MusicBrainz metadata using the returned MBID.
2. Add curated image and biography overrides.
3. Add verified YouTube video lookup.
4. Move from the in-memory H2 database to persistent storage if API results or overrides need to survive restarts.
