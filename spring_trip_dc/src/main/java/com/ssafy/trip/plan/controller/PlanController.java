package com.ssafy.trip.plan.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.attraction.model.dto.AttractionReviewListDto;
import com.ssafy.trip.plan.model.dto.PlanInfoDto;
import com.ssafy.trip.plan.model.dto.PlanItemInfoDto;
import com.ssafy.trip.plan.model.service.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/plan")
@Tag(name = "계획 컨트롤러", description = "관광지 목록 조회를 위한 REST API 컨트롤러")
@Slf4j
public class PlanController {

	@Autowired
	private PlanService aService;

	@Operation(summary = "계획 목록 조회", description = "계획 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/planList")
	public ResponseEntity<?> getPlanList(@RequestParam String user_id) {
		try {
			log.info("getPlanList - 호출 user_id : {}", user_id);
			List<PlanInfoDto> info = aService.getPlanList(user_id);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "계획 상세 조회", description = "계획의 장소 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PostMapping("/planDetail")
	public ResponseEntity<?> getPlanDetail(@RequestBody PlanInfoDto pid) {
		try {
			log.info("getPlanDetail - 호출 contentId : {}", pid);
			PlanInfoDto info = aService.getPlanDetail(pid);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "계획 전체 저장", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PutMapping("/plan")
	public ResponseEntity<?> savePlan(@RequestBody PlanInfoDto planInfoDto) {
		try {
			aService.savePlan(planInfoDto);
			return ResponseEntity.ok(1);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "계획 추가", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PostMapping(path = "/plan")
	public ResponseEntity<?> addPlan(@RequestBody PlanInfoDto pid) {

		try {
			log.info("getReviewList - 호출 contentId : {}", pid);
			aService.addPlan(pid);
			return ResponseEntity.ok(1);
		} catch (Exception e) {
			return exceptionHandling(e);
		}

	}

	@Operation(summary = "계획 삭제", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@DeleteMapping("/plan")
	public ResponseEntity<?> removePlan(@RequestBody PlanInfoDto pid) {
		try {
			aService.removePlan(pid);
			return ResponseEntity.ok(1);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	
	@Operation(summary = "계획 아이템 추가", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PostMapping(path = "/planItem")
	public ResponseEntity<?> addPlanItem(@RequestBody PlanItemInfoDto item) {

		try {
			log.info("addPlanItem - 호출 : {}", item);
			aService.addPlanItem(item);
			return ResponseEntity.ok(1);
		} catch (Exception e) {
			return exceptionHandling(e);
		}

	}

	@Operation(summary = "계획 아이템 삭제", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@DeleteMapping("/planItem")
	public ResponseEntity<?> removePlanItem(@RequestBody PlanItemInfoDto item) {
		try {
			aService.removePlanItem(item);
			return ResponseEntity.ok(1);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	


	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
//		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
	}

}
