package com.ssafy.trip.water.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.trip.attraction.controller.AttractionController;
import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.attraction.model.dto.AttractionInfoListDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewImageDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewListDto;
import com.ssafy.trip.attraction.model.dto.GugunDto;
import com.ssafy.trip.attraction.model.dto.SidoDto;
import com.ssafy.trip.attraction.model.service.AttractionService;
import com.ssafy.trip.water.model.dto.WaterInfoDto;
import com.ssafy.trip.water.model.dto.WaterInfoListDto;
import com.ssafy.trip.water.model.dto.WaterReviewDto;
import com.ssafy.trip.water.model.dto.WaterReviewImageDto;
import com.ssafy.trip.water.model.dto.WaterReviewListDto;
import com.ssafy.trip.water.model.service.WaterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/water")
@Tag(name = "물놀이 컨트롤러", description = "물놀이 목록 조회를 위한 REST API 컨트롤러")
@Slf4j
public class WaterController {

	@Autowired
	private WaterService wService;

	@Operation(summary = "물놀이 키워드 검색", description = "물놀이의 이름과 주소에 입력한 키워드를 포함하는 관광지를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/keywordSearch")
	public ResponseEntity<?> keywordSearch(@Parameter(description = "검색할 키워드") @RequestParam("keyword") String keyword,
			@Parameter(description = "콘텐츠 유형 (계곡, 하천)") @RequestParam("wtrplay_plc_type") String wtrplayPlcType,
			@Parameter(description = "페이지당 표시할 행 수") @RequestParam(name = "per_page", required = false, defaultValue = "20") int perPage,
			@Parameter(description = "조회할 페이지") @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		try {
			log.info("keywordSearch - 호출 keyword : {} wtrplayPlcType : {} perPage : {} page : {}", keyword,
					wtrplayPlcType, perPage, page);
			WaterInfoListDto list = wService.keywordSearch(keyword, wtrplayPlcType, perPage, page);
			System.out.println(list);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "지역기반 물놀이 검색", description = "특정 지역의 물놀이 지역을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/regionSearch")
	public ResponseEntity<?> regionSearch(@Parameter(description = "시도 구분") @RequestParam("sido") String sido,
			@Parameter(description = "구군 구분") @RequestParam(name = "gugun", required = false, defaultValue = "0") String gugun,
			@Parameter(description = "콘텐츠 유형 (계곡,하천)") @RequestParam("wtrplay_plc_type") String wtrplayPlcType,
			@Parameter(description = "페이지당 표시할 행 수") @RequestParam(name = "per_page", required = false, defaultValue = "20") int perPage,
			@Parameter(description = "조회할 페이지") @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		try {
			log.info("regionSearch - 호출 sidoCode : {} gugunCode : {} wtrplayPlcType : {} perPage : {} page : {}",
					sido, gugun, wtrplayPlcType, perPage, page);
			Map<String, Object> map = new HashMap();
			map.put("sido", sido);
			map.put("gugun", gugun);
			map.put("wtrplayPlcType", wtrplayPlcType);
			WaterInfoListDto list = wService.regionSearch(map, perPage, page);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

//	@Operation(summary = "시도 목록 조회", description = "시도 목록을 조회합니다.")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
//			@ApiResponse(responseCode = "500", description = "서버에러발생") })
//	@GetMapping("/sidoList")
//	public ResponseEntity<?> listSido() {
//		try {
//			log.info("listSido - 호출");
//			List<SidoDto> list = wService.listSido();
//			if (list != null && !list.isEmpty()) {
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
//				return ResponseEntity.ok().headers(headers).body(list);
//			} else {
//				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//			}
//		} catch (Exception e) {
//			return exceptionHandling(e);
//		}
//	}
//
	@Operation(summary = "구군 목록 조회", description = "구군 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/gugunList")
	public ResponseEntity<?> listGugun() {
		try {
			
			List<GugunDto> list = wService.listGugun();
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

	@Operation(summary = "물놀이 상세 조회", description = "해당 컨텐트 ID의 물놀이 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/detail")
	public ResponseEntity<?> getDetail(
			@Parameter(description = "조회할 물놀이 ID") @RequestParam(name = "objt_id", required = true) int objtId) {
		try {
			log.info("keywordSearch - 호출 objttId : {}", objtId);
			WaterInfoDto info = wService.getDetail(objtId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "물놀이 리뷰 목록 조회", description = "물놀이의 리뷰 목록을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/reviewList")
	public ResponseEntity<?> getReviewList(@Parameter(description = "콘텐츠 ID") @RequestParam("objt_id") int objtId,
			@Parameter(description = "페이지당 표시할 행 수") @RequestParam(name = "per_page", required = false, defaultValue = "20") int perPage,
			@Parameter(description = "조회할 페이지") @RequestParam(name = "page", required = false, defaultValue = "1") int page) {
		try {
			log.info("getReviewList - 호출 objtId : {}", objtId);
			WaterReviewListDto info = wService.getReviewList(objtId, perPage, page);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "물놀이 리뷰 상세 조회", description = "물놀이 리뷰의 상세 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/reviewDetail")
	public ResponseEntity<?> getReviewDetail(
			@Parameter(description = "리뷰 ID") @RequestParam("review_id") int reviewId) {
		try {
			log.info("getReviewList - 호출 reviewId : {}", reviewId);
			WaterReviewDto info = wService.getReviewDetail(reviewId);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(info);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	private final String uploadDir=Paths.get("C:","reviews","upload").toString();

	@Operation(summary = "물놀이 리뷰 작성", description = "물놀이 리뷰를 작성합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 작성"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@PostMapping(path = "/review", consumes = { "multipart/form-data" })
	public ResponseEntity<?> writeReview(@RequestParam int waterId, @RequestParam String content,
			@RequestParam String writer, @RequestParam int rating, @RequestParam(required = false) MultipartFile image) {

		try {
			Map<String, Object> resultMap = new HashMap<>();
			HttpStatus status = HttpStatus.ACCEPTED;

			WaterReviewDto reviewDto = new WaterReviewDto();
			reviewDto.setWaterId(waterId);
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
					WaterReviewImageDto imageDto = new WaterReviewImageDto();
					imageDto.setFileName(saveFilename);
					List<WaterReviewImageDto> images = new ArrayList();
					images.add(imageDto);
					reviewDto.setImageInfos(images);
					WaterReviewDto review = wService.writeReview(reviewDto);
					resultMap.put("review", review);
				} catch (IOException e) {
					log.error("파일 업로드 실패: {}", e);
					status = HttpStatus.BAD_REQUEST;
					return new ResponseEntity<Map<String, Object>>(resultMap, status);
				}
			}else {
				WaterReviewDto review = wService.writeReview(reviewDto);
				resultMap.put("review", review);
			}

			// resultMap.put("fileNames", fileNames);
			status = HttpStatus.OK;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		} catch (Exception e) {
			return exceptionHandling(e);
		}

	}
	
	@GetMapping(value="/review-image", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<?> printEditorImage(@RequestParam final String filename){
		
		//업로드 된 파일경로
		String fileFullPath=Paths.get(uploadDir,filename).toString();
		Map<String,Object> resultMap=new HashMap<>();
		
		
		HttpStatus status=HttpStatus.ACCEPTED;
		File uploadedFile=new File(fileFullPath);
		if(!uploadedFile.exists()) {
			resultMap.put("message", "요청 파일이 존재하지 않습니다.");
			status=HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
		try {
			byte[] imageBytes=Files.readAllBytes(uploadedFile.toPath());
			status=HttpStatus.OK;
			return new ResponseEntity<byte[]>(imageBytes,status);
		}catch(IOException e) {
			resultMap.put("message", "파일 반환 중 오류 발생");
			status=HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		}
		
	}	

	@Operation(summary = "물놀이 리뷰 삭제", description = "물놀이 리뷰를 삭제합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@DeleteMapping("/review")
	public ResponseEntity<?> deleteReview(@Parameter(description = "리뷰 ID") @RequestParam("review_id") int reviewId) {
		try {
			log.info("deleteReview - 호출 reviewId : {}", reviewId);
			int result = wService.deleteReview(reviewId);
			return ResponseEntity.ok(result == 1 ? "success" : "fail");
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "물놀이 랜덤 조회", description = "랜덤 물놀이 정보를 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/random")
	public ResponseEntity<?> pickRandom() {
		try {
			log.info("pickRandom - 호출 objttId : ");
			WaterInfoDto info = wService.pickRandom();
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
