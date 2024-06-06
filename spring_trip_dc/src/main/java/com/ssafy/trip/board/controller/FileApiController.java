package com.ssafy.trip.board.controller;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tui-editor")
@Slf4j
public class FileApiController {

	private final String uploadDir=Paths.get(System.getProperty("user.home"),"trip_dc","tui-editor","upload").toString();
	
	
	@PostMapping("/image-upload")
	public ResponseEntity<?> uploadEditorImage(@RequestParam MultipartFile image) {
		
		//List<String> fileNames = new ArrayList<>();
		Map<String,Object> resultMap=new HashMap<>();
		HttpStatus status=HttpStatus.ACCEPTED;
		
		
		if(image.isEmpty()) {
			resultMap.put("fileName", "");
			status=HttpStatus.OK;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);

		}
		
		
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
	            //fileNames.add(saveFilename);
	            resultMap.put("fileName",saveFilename);
	        } catch (IOException e) {
	            log.error("파일 업로드 실패: {}", e);
	            resultMap.put("message", "파일 업로드 도중 에러발생");
	            status = HttpStatus.BAD_REQUEST;
	            return new ResponseEntity<Map<String, Object>>(resultMap, status);
	        }
	    
	    
	    //resultMap.put("fileNames", fileNames);
	    status = HttpStatus.OK;
	    return new ResponseEntity<Map<String, Object>>(resultMap, status);
        
	}
	
	@GetMapping(value="/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
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
	
	
}
