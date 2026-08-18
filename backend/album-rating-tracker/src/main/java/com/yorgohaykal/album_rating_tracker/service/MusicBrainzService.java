package com.yorgohaykal.album_rating_tracker.service;

import com.yorgohaykal.album_rating_tracker.dto.AlbumSearchResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MusicBrainzService {

    private static final String MUSICBRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/release-group";
    private static final String COVER_ART_BASE_URL = "https://coverartarchive.org/release-group";
    private static final String USER_AGENT = "AlbumRatingTracker/1.0 (student portfolio project)";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<AlbumSearchResult> search(String albumTitle, String artist) {
        StringBuilder queryBuilder = new StringBuilder();

        if (albumTitle != null && !albumTitle.isBlank()) {
            queryBuilder.append("releasegroup:\"").append(albumTitle.trim()).append("\"");
        }

        if (artist != null && !artist.isBlank()) {
            if (!queryBuilder.isEmpty()) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("artist:\"").append(artist.trim()).append("\"");
        }

        String luceneQuery = queryBuilder.toString();

        URI uri = UriComponentsBuilder.fromUriString(MUSICBRAINZ_BASE_URL)
                .queryParam("query", luceneQuery)
                .queryParam("fmt", "json")
                .queryParam("limit", 10)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", USER_AGENT);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);

        return parseResults(response.getBody());
    }

    private List<AlbumSearchResult> parseResults(Map<String, Object> body) {
        List<AlbumSearchResult> results = new ArrayList<>();

        if (body == null || !body.containsKey("release-groups")) {
            return results;
        }

        List<Map<String, Object>> releaseGroups = (List<Map<String, Object>>) body.get("release-groups");

        for (Map<String, Object> rg : releaseGroups) {
            String mbid = (String) rg.get("id");
            String title = (String) rg.get("title");

            String artist = extractArtistName(rg);
            Integer releaseYear = extractReleaseYear(rg);
            String coverArtUrl = COVER_ART_BASE_URL + "/" + mbid + "/front-250";

            results.add(new AlbumSearchResult(mbid, title, artist, releaseYear, coverArtUrl));
        }

        return results;
    }

    private String extractArtistName(Map<String, Object> releaseGroup) {
        List<Map<String, Object>> artistCredit = (List<Map<String, Object>>) releaseGroup.get("artist-credit");
        if (artistCredit == null || artistCredit.isEmpty()) {
            return "Unknown Artist";
        }
        return (String) artistCredit.getFirst().get("name");
    }

    private Integer extractReleaseYear(Map<String, Object> releaseGroup) {
        String firstReleaseDate = (String) releaseGroup.get("first-release-date");
        if (firstReleaseDate == null || firstReleaseDate.isBlank()) {
            return null;
        }
        try {
            // MusicBrainz dates come as "YYYY", "YYYY-MM", or "YYYY-MM-DD"
            return Integer.parseInt(firstReleaseDate.substring(0, 4));
        } catch (Exception e) {
            return null;
        }
    }
}
