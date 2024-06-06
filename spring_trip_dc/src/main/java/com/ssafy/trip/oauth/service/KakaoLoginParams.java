package com.ssafy.trip.oauth.service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ssafy.trip.member.model.dto.OAuthProvider;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams{

	private String authorizationCode;
	
	
	@Override
	public OAuthProvider oAuthProvider() {
		
		return OAuthProvider.KAKAO;
	}

	@Override
	public MultiValueMap<String, String> makeBody() {
		// TODO Auto-generated method stub
		MultiValueMap<String, String> body=new LinkedMultiValueMap<>();
		body.add("code", authorizationCode);
		
		return body;
	}

}
