package com.ssafy.trip.member.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.trip.member.model.dto.MemberDto;

public interface MemberService {
	
	int idCheck(String userId) throws Exception;
	void joinMember(MemberDto memberDto) throws Exception;
	String findPassword(Map<String, String> map) throws Exception;
	
	MemberDto login(MemberDto memberDto) throws Exception;
	MemberDto userInfo(String userId) throws Exception;
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void deleRefreshToken(String userId) throws Exception;
	MemberDto getMemberByEmail(String email) throws Exception;
	
	/* Admin */
	List<MemberDto> listMember(Map<String, Object> map) throws Exception;
	void updateMember(MemberDto memberDto) throws Exception;
	void deleteMember(String userid) throws Exception;
	MemberDto getMember(String userId) throws Exception;
}
