package com.ssafy.trip.oauth.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ssafy.trip.member.model.dto.OAuthProvider;
import com.ssafy.trip.oauth.model.dto.OAuthInfoResponse;

@Component
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
    	
        OAuthApiClient client = clients.get(params.oAuthProvider());
        
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}