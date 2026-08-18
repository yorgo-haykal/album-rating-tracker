package com.yorgohaykal.album_rating_tracker.controller;

import com.yorgohaykal.album_rating_tracker.dto.AlbumSearchResult;
import com.yorgohaykal.album_rating_tracker.service.MusicBrainzService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/musicbrainz")
@AllArgsConstructor
public class MusicBrainzController {

    private final MusicBrainzService musicBrainzService;

    @GetMapping("/search")
    public List<AlbumSearchResult> search(@RequestParam(required = false) String albumTitle, @RequestParam(required = false) String artist) {
        return musicBrainzService.search(albumTitle, artist);
    }
}
