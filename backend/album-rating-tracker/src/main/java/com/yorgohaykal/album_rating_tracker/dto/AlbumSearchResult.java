package com.yorgohaykal.album_rating_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AlbumSearchResult {

    private String musicbrainzId;
    private String title;
    private String artist;
    private Integer releaseYear;
    private String coverArtUrl;

}
