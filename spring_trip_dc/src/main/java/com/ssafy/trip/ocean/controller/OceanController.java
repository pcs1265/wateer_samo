package com.ssafy.trip.ocean.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.ocean.model.dto.BeachInfoDto;
import com.ssafy.trip.ocean.model.dto.KhoaObservDto;
import com.ssafy.trip.ocean.model.service.OceanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ocean")
@CrossOrigin("*")
@Slf4j
public class OceanController {

	@Autowired
	private OceanService oService;

	private static final String khoa_key = "**key**";

	@GetMapping(value = "/khoaObservList")
	public ResponseEntity<?> getObservList() {
		try {
			log.info("khoaObservList - 호출");
			List<KhoaObservDto> list = oService.getKhoaObservList();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 조류예보
	@GetMapping(value = "/fcTidalCurrent", produces = "application/json")
	public String fcTidalCurrent(@RequestParam("ObsCode") String obsCode, @RequestParam("Date") String date,
			@RequestParam(value = "ResultType", defaultValue = "json") String resultType) {

		StringBuilder apiURL = new StringBuilder();

		apiURL.append("http://www.khoa.go.kr/api/oceangrid/fcTidalCurrent/search.do?");
		apiURL.append("ServiceKey=" + khoa_key);
		apiURL.append("&ObsCode=" + obsCode);
		apiURL.append("&Date=" + date);
		apiURL.append("&ResultType=" + resultType);

		Map<String, String> requestHeaders = new HashMap<>();

		String responseBody = get(apiURL.toString(), requestHeaders);

		System.out.println(responseBody);
		return responseBody;
	}

	// 조석예보
	@GetMapping(value = "/tideObsPreTab", produces = "application/json")
	public String tideObsPreTab(@RequestParam("ObsCode") String obsCode, @RequestParam("Date") String date,
			@RequestParam(value = "ResultType", defaultValue = "json") String resultType) {

		StringBuilder apiURL = new StringBuilder();

		apiURL.append("http://www.khoa.go.kr/api/oceangrid/tideObsPreTab/search.do?");
		apiURL.append("ServiceKey=" + khoa_key);
		apiURL.append("&ObsCode=" + obsCode);
		apiURL.append("&Date=" + date);
		apiURL.append("&ResultType=" + resultType);

		Map<String, String> requestHeaders = new HashMap<>();

		String responseBody = get(apiURL.toString(), requestHeaders);

		System.out.println(responseBody);
		return responseBody;
	}

	// 해수욕장정보
	@GetMapping(value = "/beachList")
	public ResponseEntity<?> beachList(@RequestParam(value = "SIDO_NM", required = false) String sidoNM) {
		try {
			log.info("beachList - 호출");
			List<BeachInfoDto> list = oService.getBeachList(sidoNM);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 해수욕장정보
	@GetMapping(value = "/randomBeachList")
	public ResponseEntity<?> randomBeachList() {
		try {
			log.info("randomBeachList - 호출");
			List<BeachInfoDto> list = oService.getRandomBeachList();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 해수욕장 시도정보
	@GetMapping(value = "/beachSidoList")
	public ResponseEntity<?> beachSidoList() {
		try {
			log.info("beachSidoList - 호출");

			List<String> list = oService.getBeachSidoList();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(list);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 해수욕장정보
	@GetMapping(value = "/beachDetail")
	public ResponseEntity<?> beachDetail(@RequestParam(value = "id") int id) {
		try {
			log.info("beachDetail - 호출");

			BeachInfoDto detail = oService.getBeach(id);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(detail);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 해수욕장정보
	@GetMapping(value = "/beachWeather")
	public ResponseEntity<?> beachWeather(@RequestParam(value = "id") int id) {
		try {
			log.info("beachDetail - 호출");

			Map<String, Object> map = new HashMap<>();

			LocalDateTime now = LocalDateTime.now().minusMinutes(30);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			String formatedNow = now.format(formatter);

			Future<Map<String, Object>> temp = getTemp(id, formatedNow);
			Future<Map<String, Object>> waveHeight = getWaveHeight(id, formatedNow);
			Future<Map<String, Object>> ultraShortWeather = getUltraShortWeather(id, formatedNow);
			
			map.put("temp", temp.get());
			map.put("waveHeight", waveHeight.get());
			map.put("ultraShortWeather", ultraShortWeather.get());
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(map);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	private Future<Map<String, Object>> getTemp(int id, String formatedNow) {
		CompletableFuture<Map<String, Object>> result = new CompletableFuture<>();

		new Thread(() -> {
			Map<String, String> requestHeaders = new HashMap<>();

			String apiURLTemp = "https://apis.data.go.kr/1360000/BeachInfoservice/getTwBuoyBeach"
					+ "?serviceKey=**key**"
					+ "&dataType=JSON" + "&beach_num=" + id + "&searchTime=" + formatedNow;
			String resp = get(apiURLTemp, requestHeaders);

			result.complete(getItems(resp)); // 오랜 시간이 걸리는 계산이 완료되면 Future에 값을 설정한다.
		}).start();
		return result;
	}

	private Future<Map<String, Object>> getWaveHeight(int id, String formatedNow) {
		CompletableFuture<Map<String, Object>> result = new CompletableFuture<>();

		new Thread(() -> {
			Map<String, String> requestHeaders = new HashMap<>();

			String apiURLWaveHeight = "https://apis.data.go.kr/1360000/BeachInfoservice/getWhBuoyBeach"
					+ "?serviceKey=**key**"
					+ "&dataType=JSON" + "&beach_num=" + id + "&searchTime=" + formatedNow;

			String resp = get(apiURLWaveHeight, requestHeaders);

			result.complete(getItems(resp)); // 오랜 시간이 걸리는 계산이 완료되면 Future에 값을 설정한다.
		}).start();
		return result;
	}
	
	private Future<Map<String, Object>> getUltraShortWeather(int id, String formatedNow) {
		CompletableFuture<Map<String, Object>> result = new CompletableFuture<>();
		
		new Thread(() -> {
			Map<String, String> requestHeaders = new HashMap<>();

			String apiURLWaveHeight = "https://apis.data.go.kr/1360000/BeachInfoservice/getUltraSrtFcstBeach"
					+ "?serviceKey=**key**"
					+ "&dataType=JSON" + "&beach_num=" + id + "&base_time=" + formatedNow.substring(8, 12)+ "&base_date=" + formatedNow.substring(0, 8)+ "&numOfRows=" + 60;
			System.out.println(apiURLWaveHeight);
			String resp = get(apiURLWaveHeight, requestHeaders);
			
			result.complete(getItems(resp)); // 오랜 시간이 걸리는 계산이 완료되면 Future에 값을 설정한다.
		}).start();
		return result;
	}
	
	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 오류 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
		}
	}

	private Map<String, Object> getItems(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			JSONObject response = obj.getJSONObject("response");
			JSONObject body = response.getJSONObject("body");
			JSONObject items = body.getJSONObject("items");
			return items.toMap();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(json);
			return new HashMap<>();
		}
		
	}
	
	@Operation(summary = "랜덤 조회", description = "랜덤 해수욕장을 조회합니다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공적인 조회"),
			@ApiResponse(responseCode = "500", description = "서버에러발생") })
	@GetMapping("/random")
	public ResponseEntity<?> pickRandom() {
		try {
			log.info("pickRandom - 호출");
			BeachInfoDto info = oService.pickRandom();
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
