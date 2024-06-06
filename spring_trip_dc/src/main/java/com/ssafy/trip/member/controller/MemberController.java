package com.ssafy.trip.member.controller;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.board.model.dto.BoardListDto;
import com.ssafy.trip.member.model.dto.MemberDto;
import com.ssafy.trip.member.model.service.MemberService;
import com.ssafy.trip.util.JWTUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 토큰처리등 회원의 인증관련 처리하는 클래스.")
@Slf4j
@CrossOrigin("*")
public class MemberController {
	
	private final MemberService memberService;
	private final JWTUtil jwtUtil;
	
	
	private JavaMailSenderImpl javaMailSender;
	
	public MemberController(MemberService memberService, JWTUtil jwtUtil) {
		
		this.memberService = memberService;
		this.jwtUtil = jwtUtil;
		this.javaMailSender=createMailSender();
	}

	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		log.debug("login user : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto loginUser = memberService.login(memberDto);
			if(loginUser != null) {
				String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
				String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
				log.debug("access token : {}", accessToken);
				log.debug("refresh token : {}", refreshToken);
				
//				발급받은 refresh token 을 DB에 저장.
				memberService.saveRefreshToken(loginUser.getUserId(), refreshToken);
				
//				JSON 으로 token 전달.
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				
				status = HttpStatus.CREATED;
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
				status = HttpStatus.UNAUTHORIZED;
			} 
			
		} catch (Exception e) {
			log.debug("로그인 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "회원인증", description = "회원 정보를 담은 Token 을 반환한다.")
	@GetMapping("/info/{userId}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userId") @Parameter(description = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
//		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		System.out.println(request.getHeader("Authorization"));
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			log.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				MemberDto memberDto = memberService.userInfo(userId);
				resultMap.put("userInfo", memberDto);
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
	@GetMapping("/logout/{userId}")
	@Hidden
	public ResponseEntity<?> removeToken(@PathVariable ("userId") @Parameter(description = "로그아웃 할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.deleRefreshToken(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody MemberDto memberDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		System.out.println(memberService.getRefreshToken(memberDto.getUserId()));
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		System.out.println(token);
		log.debug("token : {}, memberDto : {}", token, memberDto);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(memberService.getRefreshToken(memberDto.getUserId()))) {
				String accessToken = jwtUtil.createAccessToken(memberDto.getUserId());
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 access token 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("refresh token 도 사용 불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary="회원가입", description="유저의 정보로 회원가입한다.")
	@PostMapping("/join")
	public ResponseEntity<?> joinUser(@RequestBody MemberDto memberDto){
		HttpStatus status=HttpStatus.ACCEPTED;
		System.out.println(memberDto.getUserId());
		Map<String, Object> resultMap = new HashMap<>();
		try {
			memberService.joinMember(memberDto);
			resultMap.put("message", "회원 가입이 정상적으로 처리되었습니다.");
			status=HttpStatus.OK;
		}catch(Exception e){
			log.error("회원가입 실패:{}",e);
			resultMap.put("message", "회원 가입 중 오류가 발생했습니다.");
			status=HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String,Object>>(resultMap,status);
	}
	
	@Operation(summary="아이디 중복체크", description="아이디를 중복 체크한다.")
	@GetMapping("/{userid}")
	public ResponseEntity<?> idCheck(@PathVariable("userid") @Parameter(description = "중복체크 할 회원의 아이디.", required = true)String userId) throws Exception {
		log.debug("idCheck userid : {}", userId);
		Map<String, Object> resultMap = new HashMap<>();
		int cnt = memberService.idCheck(userId);
		HttpStatus status=HttpStatus.OK;

		if(cnt==1) {
			resultMap.put("isDuplicated", true);
		}else {
			resultMap.put("isDuplicated", false);

		}
		return new ResponseEntity<Map<String,Object>>(resultMap,status);
	}
	
	@Operation(summary="회원정보 수정", description="회원정보를 수정한다.")
	@PutMapping("/{userid}")
	public ResponseEntity<?> modifyMember(@PathVariable("userid") @Parameter(description = "중복체크 할 회원의 아이디.", required = true)String userId,
							@RequestBody MemberDto memberDto){
		log.debug("idCheck userid : {}", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status=HttpStatus.ACCEPTED;
		Map<String,String> param=new HashMap<>();
		
		try {
			
			memberService.updateMember(memberDto);
			
			resultMap.put("message", "회원정보가 수정되었습니다.");
			status=HttpStatus.OK;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultMap.put("message", "회원정보 수정중 에러가 발생했습니다.");
			status=HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Map<String,Object>>(resultMap,status);
		}
		return new ResponseEntity<Map<String,Object>>(resultMap,status);

	}
	
	@Operation(summary="비밀번호 찾기", description="유저의 비밀번호를 찾습니다.")
	@PostMapping("/findPw")
	public String findPw(@RequestBody Map<String, String> info){
		
		log.debug("idCheck userid : {}", info.get("userId"));
		Map<String, Object> resultMap = new HashMap<>();
		
		Map<String,String> param=new HashMap<>();
		param.put("userId", info.get("userId"));
		param.put("email", info.get("email"));
		String userPassword=null;
		try {
			userPassword = memberService.findPassword(param);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println(userPassword);
		return userPassword;
	}
	
	
	
	
	@PostMapping("/email")
	public Map email(@RequestBody String email) throws Exception {
		Map map = new HashMap<>();
//		System.out.println(email);
		MemberDto dto = memberService.getMemberByEmail(email);
        
		if (dto != null) {
			map.put("exist", "이미 존재하는 이메일입니다.");
		} else {
			Random random = new Random(); // 난수 생성을 위한 랜덤 클래스
			String key = ""; // 인증번호 담을 String key 변수 생성
            
			SimpleMailMessage message = new SimpleMailMessage(); // 이메일 제목, 내용 작업 메서드
			message.setTo(email); // 스크립트에서 보낸 메일을 받을 사용자 이메일 주소
			
			// 입력 키를 위한 난수 생성 코드 
			for (int i = 0; i < 3; i++) {
				int index = random.nextInt(26) + 65;
				key += (char) index;
			}
			for (int i = 0; i < 6; i++) {
				int numIndex = random.nextInt(10);
				key += numIndex;
			}
			
			String mail = "\n WateerSamo 회원가입 이메일 인증.";
			message.setSubject("회원가입을 위한 이메일 인증번호 메일입니다."); // 이메일 제목
			message.setText("인증번호는 " + key + " 입니다." + mail); // 이메일 내용
			
            try {
				javaMailSender.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("key", key);
			map.put("dto", dto);
		}
		return map;
	}
	
	@Operation(summary = "멤버 목록", description = "멤버 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "게시글목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping
	@Parameter(name = "pgno", schema = @Schema(type = "number"))
	@Parameter(name = "perPage", schema = @Schema(type = "number"))
	public ResponseEntity<?> listMember(
			@RequestParam @Parameter(description = " 부가정보.", required = true, hidden = true) Map<String, Object> map) {
		log.info("listMember map - {}", map);
		HttpHeaders header;
		try {
			List<MemberDto> members= memberService.listMember(map);
			header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(members);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<BoardDto>( HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/one/{userId}")
	public ResponseEntity<?> getMemberById(
			@PathVariable @Parameter(description = "유저아이디.", required = true, hidden = true) String userId) {
		
		HttpHeaders header;
		try {
			MemberDto member= memberService.getMember(userId);
			System.out.println(member.getEmail());
			header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(member);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<BoardDto>( HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	private JavaMailSenderImpl createMailSender() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		Properties prop = new Properties();
		mailSenderImpl.setHost("smtp.gmail.com");
		mailSenderImpl.setPort(587);
		mailSenderImpl.setUsername("3120kang@gmail.com");
		mailSenderImpl.setPassword("jkjy rbev zoxw rhwf");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", true);
        
		mailSenderImpl.setJavaMailProperties(prop);
		return mailSenderImpl;
	}
	
}
