package com.ssafy.trip.board.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.board.model.dto.BoardListDto;
import com.ssafy.trip.board.model.dto.CommentDto;
import com.ssafy.trip.board.model.dto.CommentListDto;
import com.ssafy.trip.board.model.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, maxAge = 6000)
@RestController
@RequestMapping("/board")
@Tag(name = "게시판 컨트롤러", description = "게시판에 글을 등록, 수정, 삭제, 목록, 상세보기등 전반적인 처리를 하는 클래스.")
@Slf4j
public class BoardController {

	private BoardService boardService;

	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@Operation(summary = "게시판 글작성", description = "새로운 게시글 정보를 입력 : 성공한 경우 작성한 글의 글 번호를 반환한다.")
	@PostMapping
	public ResponseEntity<?> writeArticle(
			@RequestBody @Parameter(description = "작성글 정보.", required = true) BoardDto boardDto) {
		log.info("writeArticle boardDto - {}", boardDto);
		try {

			int result = boardService.writeArticle(boardDto);
//			return ResponseEntity.ok().build();
			return ResponseEntity.ok(result == 1 ? boardDto.getNo() : "fail");
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "게시판 글목록", description = "모든 게시글의 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "게시글목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping
	@Parameter(name = "pgno", schema = @Schema(type = "number"))
	@Parameter(name = "perPage", schema = @Schema(type = "number"))
	@Parameter(name = "keyword", schema = @Schema(type = "string"))
	@Parameter(name = "keywordType", schema = @Schema(type = "string"))
	public ResponseEntity<?> listArticle(
			@RequestParam @Parameter(description = "게시글을 얻기위한 부가정보.", required = true, hidden = true) Map<String, String> map) {
		log.info("listArticle map - {}", map);

		try {
			BoardListDto boardListDto = boardService.listArticle(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(boardListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "게시판 글보기", description = "글번호에 해당하는 게시글의 정보를 반환한다.")
	@GetMapping("/{no}")
	public ResponseEntity<BoardDto> getArticle(
			@PathVariable("no") @Parameter(name = "no", description = "얻어올 글의 글번호.", required = true) int no)
			throws Exception {
		log.info("getArticle - 호출 : " + no);
		BoardDto boardDto = boardService.getArticle(no);
		boardService.updateHit(no);
		if (boardDto == null) {
			return new ResponseEntity<BoardDto>(HttpStatus.NO_CONTENT);
		} else {

			return new ResponseEntity<BoardDto>(boardDto, HttpStatus.OK);
		}
	}

	@Operation(summary = "게시판 글수정", description = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@PutMapping("/{no}")
	public ResponseEntity<String> modifyArticle(
			@RequestBody @Parameter(description = "수정할 글정보.", required = true) BoardDto boardDto,
			@PathVariable("no") @Parameter(name = "no", description = "수정할 글의 글번호.", required = true) int no)
			throws Exception {
		log.info("modifyArticle - 호출 {}", boardDto);
		boardDto.setNo(no);
		int result = boardService.modifyArticle(boardDto);
		return ResponseEntity.ok(result == 1 ? "success" : "fail");
	}

	@Operation(summary = "게시판 글삭제", description = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@DeleteMapping("/{no}")
	public ResponseEntity<String> deleteArticle(
			@PathVariable("no") @Parameter(name = "no", description = "삭제할 글의 글번호.", required = true) int id)
			throws Exception {
		log.info("deleteArticle - 호출");
		int result = boardService.deleteArticle(id);
		return ResponseEntity.ok(result == 1 ? "success" : "fail");
	}

	@Operation(summary = "게시판 댓글작성", description = "새로운 댓글 정보를 입력한다.")
	@PostMapping("/{no}/comment")
	public ResponseEntity<?> writeArticleComment(
			@PathVariable("no") @Parameter(name = "no", description = "작설할 댓글의 대상 글번호.", required = true) int article_no,
			@RequestBody @Parameter(description = "작성 댓글 정보.", required = true) CommentDto commentDto) {
		log.info("writeArticleComment commentDto - {}", commentDto);
		try {
			// commentDto.setWriter("ssafy");
			commentDto.setArticleNo(article_no);
			int result = boardService.writeArticleComment(commentDto);
//			return ResponseEntity.ok().build();
			return ResponseEntity.ok(result == 1 ? "success" : "fail");
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "게시글 댓글목록", description = "해당 id 게시글의 모든 댓글 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "댓글목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping("/{no}/comment")
	@Parameter(name = "pgno", schema = @Schema(type = "number"))
	@Parameter(name = "perPage", schema = @Schema(type = "number"))
	public ResponseEntity<?> listArticleComment(
			@PathVariable("no") @Parameter(name = "no", description = "조회할 대상 글번호.", required = true) String articleNo,
			@RequestParam @Parameter(description = "댓글을 얻기위한 부가정보.", required = true, hidden = true) Map<String, String> map) {
		log.info("listArticleComment map - {}", map);
		map.put("article_no", articleNo);
		try {
			CommentListDto commentListDto = boardService.getArticleComments(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(commentListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@Operation(summary = "게시판 댓글삭제", description = "댓글번호에 해당하는 댓글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@DeleteMapping("/{no}/comment/{c_id}")
	public ResponseEntity<String> deleteArticleComment(
			@PathVariable("no") @Parameter(name = "no", description = "삭제할 댓글의 글번호.", required = true) int a_no,
			@PathVariable("c_id") @Parameter(name = "c_id", description = "삭제할 댓글번호.", required = true) int c_id)
			throws Exception {
		log.info("deleteArticle - 호출");
		int result = boardService.deleteArticleComment(a_no, c_id);
		return ResponseEntity.ok(result == 1 ? "success" : "fail");
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "게시판 인기 글 목록", description = "모든 게시글의 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "게시글목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping("/listTopArticle")
	public ResponseEntity<?> listTopArticle(){
		log.info("listTopArticle map");
		
		try {
			List<BoardDto> boardList = boardService.listTopArticle();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(boardList);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
}