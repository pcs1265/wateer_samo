package com.ssafy.trip.oauth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.oauth.service.KakaoLoginParams;
import com.ssafy.trip.oauth.service.OAuthLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {
	
		
	private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<Map<String,Object>> loginKakao(@RequestBody KakaoLoginParams params) {
    	Map<String,Object> result=new HashMap<>();
    	System.out.println(params.getAuthorizationCode());
    	try {
        	
        	result=oAuthLoginService.login(params);
        	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return ResponseEntity.ok(result);
    }
    
   
    

//    @PostMapping("/naver")
//    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }
	
}
