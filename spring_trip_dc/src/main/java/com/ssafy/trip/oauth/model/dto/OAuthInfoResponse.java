package com.ssafy.trip.oauth.model.dto;

import com.ssafy.trip.member.model.dto.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}