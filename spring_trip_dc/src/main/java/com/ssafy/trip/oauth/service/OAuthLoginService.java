package com.ssafy.trip.oauth.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ssafy.trip.member.model.dto.MemberDto;
import com.ssafy.trip.member.model.mapper.MemberMapper;
import com.ssafy.trip.member.model.service.MemberService;
import com.ssafy.trip.oauth.model.dto.OAuthInfoResponse;
import com.ssafy.trip.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginService {
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public Map<String,Object> login(OAuthLoginParams params) throws Exception {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        System.out.println("이메일"+oAuthInfoResponse.getEmail());
        String memberId = findOrCreateMember(oAuthInfoResponse);
        Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("email", oAuthInfoResponse.getEmail());
        if(memberId != null) {
			String accessToken = jwtUtil.createAccessToken(memberId);
			String refreshToken = jwtUtil.createRefreshToken(memberId);
			log.debug("access token : {}", accessToken);
			log.debug("refresh token : {}", refreshToken);
			
//			발급받은 refresh token 을 DB에 저장.
			memberService.saveRefreshToken(memberId, refreshToken);
			
//			JSON 으로 token 전달.
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
			resultMap.put("userId",memberId );
			return resultMap;
        }else {
        	resultMap.put("userId", null);
        	return resultMap;
        }
        
        
        
        
    }

    private String findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) throws SQLException {
    	MemberDto member = memberMapper.getMemberByEmail(oAuthInfoResponse.getEmail());
    	if (member != null) {
    	    return member.getUserId();
    	} else {
    	    return null;
    	}
    }

    
}