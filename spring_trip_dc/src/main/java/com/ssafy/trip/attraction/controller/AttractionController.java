package com.ssafy.trip.attraction.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.attraction.model.dto.AttractionInfoListDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewImageDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewListDto;
import com.ssafy.trip.attraction.model.dto.AttractionWishDto;
import com.ssafy.trip.attraction.model.dto.GugunDto;
import com.ssafy.trip.attraction.model.dto.SidoDto;
import com.ssafy.trip.attraction.model.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/attraction")
@Tag(name = "관광지 컨트롤러", description = "관광지 목록 조회를 위한 REST API 컨트롤러")
@Slf4j
public class AttractionController {

	@Autowired
	private AttractionService aService;

	@Operation(summary = "관광지 키워드 검색", description = "관광지의 이름과 주소에 입력한 키워드를 포함하는 관광지를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/keywordSearch")
	public ResponseEntity<?> keywordSearch(@Parameter(description = "검색할 키워드") @RequestParam("keyword") String keyword,
			@Parameter(description = "콘텐츠 유형 (12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점)") @RequestParam("content_type_id") int contentTypeId,
			@Parameter(description = "페이지당 표시할 행 수") @RequestParam(name = "per_page", required = false, defaultValue = "20") int perPage,
			@Parameter(description = "조회할 페이지") @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		try {
			log.info("keywordSearch - 호출 keyword : {} contentTypeId : {} perPage : {} page : {}", keyword,
					contentTypeId, perPage, page);
			AttractionInfoListDto list = aService.keywordSearch(keyword, contentTypeId, perPage, page);
			System.out.println(list);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "지역기반 관광지 검색", description = "특정 지역의 관광지를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/regionSearch")
	public ResponseEntity<?> regionSearch(@Parameter(description = "시도 구분") @RequestParam("sido_code") int sidoCode,
			@Parameter(description = "구군 구분") @RequestParam(name = "gugun_code", required = false, defaultValue = "0") int gugunCode,
			@Parameter(description = "콘텐츠 유형 (12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점)") @RequestParam("content_type_id") int contentTypeId,
			@Parameter(description = "페이지당 표시할 행 수") @RequestParam(name = "per_page", required = false, defaultValue = "20") int perPage,
			@Parameter(description = "조회할 페이지") @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		try {
			log.info("regionSearch - 호출 sidoCode : {} gugunCode : {} contentTypeId : {} perPage : {} page : {}",
					sidoCode, gugunCode, contentTypeId, perPage, page);
			Map<String, Object> map = new HashMap();
			map.put("sido_code", sidoCode);
			map.put("gugun_code", gugunCode);
			map.put("content_type_id", contentTypeId);
			AttractionInfoListDto list = aService.regionSearch(map, perPage, page);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "시도 목록 조회", description = "시도 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/sidoList")
	public ResponseEntity<?> listSido() {
		try {
			log.info("listSido - 호출");
			List<SidoDto> list = aService.listSido();
			if (list != null && !list.isEmpty()) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
				return ResponseEntity.ok().headers(headers).body(list);
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "구군 목록 조회", description = "구군 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/gugunList")
	public ResponseEntity<?> listGugun(@Parameter(description = "시도 구분") @RequestParam("sidoCode") int sidoCode) {
		try {
			log.info("listSido - 호출 sidoCode : {}", sidoCode);
			List<GugunDto> list = aService.listGugun(sidoCode);
			if (list != null && !list.isEmpty()) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
				return ResponseEntity.ok().headers(headers).body(list);
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "관광지 상세 조회", description = "해당 컨텐트 ID의 관광지 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/detail")
	public ResponseEntity<?> getDetail(
			@Parameter(description = "조회할 관광지 ID") @RequestParam(name = "contentId", required = true) int contentId) {
		try {
			log.info("keywordSearch - 호출 contentId : {}", contentId);
			AttractionInfoDto info = aService.getDetail(contentId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "관광지 리뷰 목록 조회", description = "관광지의 리뷰 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/reviewList")
	public ResponseEntity<?> getReviewList(@Parameter(description = "콘텐츠 ID") @RequestParam("content_id") int contentId,
			@Parameter(description = "페이지당 표시할 행 수") @RequestParam(name = "per_page", required = false, defaultValue = "20") int perPage,
			@Parameter(description = "조회할 페이지") @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		try {
			log.info("getReviewList - 호출 contentId : {}", contentId);
			AttractionReviewListDto info = aService.getReviewList(contentId, perPage, page);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "관광지 리뷰 상세 조회", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/reviewDetail")
	public ResponseEntity<?> getReviewDetail(
			@Parameter(description = "리뷰 ID") @RequestParam("review_id") int reviewId) {
		try {
			log.info("getReviewList - 호출 contentId : {}", reviewId);
			AttractionReviewDto info = aService.getReviewDetail(reviewId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	private final String uploadDir = Paths.get(System.getProperty("user.home"), "trip_dc", "reviews", "upload")
			.toString();

	@Operation(summary = "관광지 리뷰 작성", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PostMapping(path = "/review", consumes = { "multipart/form-data" })
	public ResponseEntity<?> writeReview(@RequestParam int attractionId, @RequestParam String content,
			@RequestParam String writer, @RequestParam int rating,
			@RequestParam(required = false) MultipartFile image) {

		try {
			Map<String, Object> resultMap = new HashMap<>();
			HttpStatus status = HttpStatus.ACCEPTED;

			AttractionReviewDto reviewDto = new AttractionReviewDto();
			reviewDto.setAttractionId(attractionId);
			reviewDto.setWriter(writer);
			reviewDto.setContent(content);
			reviewDto.setRating(rating);

			log.info("writeReview - 호출 contentId : {},  {}", reviewDto, image);

			if (image != null) {
				String orgFilename = image.getOriginalFilename();
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
				String saveFilename = uuid + "." + extension;
				String fileFullPath = Paths.get(uploadDir, saveFilename).toString();

				// 디렉토리 없으면 생성
				File dir = new File(uploadDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				try {
					File uploadFile = new File(fileFullPath);
					image.transferTo(uploadFile);
					// fileNames.add(saveFilename);
					reviewDto.setFirstImage(saveFilename);
					AttractionReviewImageDto imageDto = new AttractionReviewImageDto();
					imageDto.setFileName(saveFilename);
					List<AttractionReviewImageDto> images = new ArrayList();
					images.add(imageDto);
					reviewDto.setImageInfos(images);
					AttractionReviewDto review = aService.writeReview(reviewDto);
					resultMap.put("review", review);
				} catch (IOException e) {
					log.error("파일 업로드 실패: {}", e);
					status = HttpStatus.BAD_REQUEST;
					return new ResponseEntity<Map<String, Object>>(resultMap, status);
				}
			} else {
				AttractionReviewDto review = aService.writeReview(reviewDto);
				resultMap.put("review", review);
			}

			// resultMap.put("fileNames", fileNames);
			status = HttpStatus.OK;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		} catch (Exception e) {
			return exceptionHandling(e);
		}

	}

	@GetMapping(value = "/review-image", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<?> printEditorImage(@RequestParam final String filename) {

		// 업로드 된 파일경로
		String fileFullPath = Paths.get(uploadDir, filename).toString();
		Map<String, Object> resultMap = new HashMap<>();

		HttpStatus status = HttpStatus.ACCEPTED;
		File uploadedFile = new File(fileFullPath);
		if (!uploadedFile.exists()) {
			resultMap.put("message", "요청 파일이 존재하지 않습니다.");
			status = HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

		try {
			byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
			status = HttpStatus.OK;
			return new ResponseEntity<byte[]>(imageBytes, status);
		} catch (IOException e) {
			resultMap.put("message", "파일 반환 중 오류 발생");
			status = HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}

	}

	@Operation(summary = "관광지 리뷰 삭제", description = "관광지 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@DeleteMapping("/review")
	public ResponseEntity<?> deleteReview(@Parameter(description = "리뷰 ID") @RequestParam("review_id") int reviewId) {
		try {
			log.info("deleteReview - 호출 reviewId : {}", reviewId);
			int result = aService.deleteReview(reviewId);
			return ResponseEntity.ok(result == 1 ? "success" : "fail");
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "관광지 찜", description = "관광지를 찜합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "찜 성공"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PostMapping("/wish")
	public ResponseEntity<?> addWish(@RequestBody AttractionWishDto attractionWishDto) {
		try {
			log.info("addWish - 호출 userId : {} itemId: {}", attractionWishDto.getUserId(),
					attractionWishDto.getItemId());
			int result = aService.addWish(attractionWishDto);
			return ResponseEntity.ok(result == 1 ? "success" : "fail");
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "관광지 찜 취소", description = "관광지를 찜을 취소합니다")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "찜 취소 성공"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@DeleteMapping("/wish")
	public ResponseEntity<?> popWish(@RequestParam String userId, @RequestParam int itemId) {
		try {
			log.info("popWish - 호출 userId : {} itemId: {}", userId, itemId);
			Map<String, Object> params = new HashMap<>();
			params.put("userId", userId);
			params.put("itemId", itemId);
			int result = aService.popWish(params);
			return ResponseEntity.ok(result == 1 ? "success" : "fail");
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "관광지 찜 목록 조회", description = "관광지를 찜 목록을 본다")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "찜 목록보기성공"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/wish/{userId}")
	public ResponseEntity<?> viewWishs(@PathVariable String userId) {

		try {
			List<AttractionInfoDto> list = aService.viewWishs(userId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}

	}

	@Operation(summary = "관광지 찜  조회", description = "관광지 찜했는지 본다")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "찜 목록보기성공"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/wish/{userId}/{itemId}")
	public ResponseEntity<?> viewWish(@PathVariable String userId, @PathVariable int itemId) {

		try {
			AttractionInfoDto wish = aService.viewWish(userId, itemId);
			int result = 0;
			if (wish != null) {
				result = 1;
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(result);
		} catch (Exception e) {
			return exceptionHandling(e);
		}

	}

	@Operation(summary = "인기 조회", description = "인기 관광지 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/listPopular")
	public ResponseEntity<?> listPopular() {
		try {
			log.info("listPopular - 호출");
			List<AttractionInfoDto> list = aService.listPopular();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "랜덤 조회", description = "랜덤 관광지를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/random")
	public ResponseEntity<?> pickRandom() {
		try {
			log.info("pickRandom - 호출");
			AttractionInfoDto info = aService.pickRandom();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
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
