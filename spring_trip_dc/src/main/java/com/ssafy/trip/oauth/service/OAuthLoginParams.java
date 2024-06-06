package com.ssafy.trip.oauth.service;

import org.springframework.util.MultiValueMap;

import com.ssafy.trip.member.model.dto.OAuthProvider;

public interface OAuthLoginParams {
	OAuthProvider oAuthProvider();
	MultiValueMap<String, String> makeBody();
	
}
