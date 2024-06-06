package com.ssafy.trip.member.model.dto;

import org.springdoc.core.providers.SecurityOAuth2Provider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "MemberDto : 회원정보", description = "회원의 상세 정보를 나타낸다.")
public class MemberDto {
	
	@Schema(description = "아이디")
	private String userId;
	@Schema(description = "닉네임")
	private String nickname;
	@Schema(description = "비밀번호")
	private String password;
	@Schema(description = "전화번호")
	private String phone;
	@Schema(description = "이메일")
	private String email;
	@Schema(description = "가입일")
	private String joinDate;
	@Schema(description = "refreshToken")
	private String refreshToken;
	@Schema(description="플랫폼이름")
	private OAuthProvider provider;
	private boolean isIdCheck;
	
	
	public boolean getIsIdCheck() {
		return isIdCheck;
	}
}
