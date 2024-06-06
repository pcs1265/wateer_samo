package com.ssafy.trip.oauth.service;

import com.ssafy.trip.member.model.dto.OAuthProvider;
import com.ssafy.trip.oauth.model.dto.OAuthInfoResponse;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}