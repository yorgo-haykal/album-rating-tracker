package com.yorgohaykal.album_rating_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {

    private Long userId;
    private String username;
    private String email;

}
