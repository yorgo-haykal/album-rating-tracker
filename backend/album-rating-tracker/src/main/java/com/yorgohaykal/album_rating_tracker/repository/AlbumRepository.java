package com.yorgohaykal.album_rating_tracker.repository;

import com.yorgohaykal.album_rating_tracker.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByUserId(Long userId);

    List<Album> findByUserIdAndGenreIgnoreCase(Long userId, String genre);

    List<Album> findByUserIdAndArtistContainingIgnoreCase(Long userId, String artist);
}
