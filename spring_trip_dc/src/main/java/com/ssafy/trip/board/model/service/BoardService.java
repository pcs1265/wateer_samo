package com.ssafy.trip.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.board.model.dto.BoardListDto;
import com.ssafy.trip.board.model.dto.CommentDto;
import com.ssafy.trip.board.model.dto.CommentListDto;

public interface BoardService {

	int writeArticle(BoardDto boardDto) throws Exception;
	BoardListDto listArticle(Map<String, String> map) throws Exception;
//	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	BoardDto getArticle(int no) throws Exception;
	void updateHit(int no) throws Exception;
	
	int modifyArticle(BoardDto boardDto) throws Exception;
//	
	int deleteArticle(int no) throws Exception;

	int writeArticleComment(CommentDto commentDto) throws Exception;
	CommentListDto getArticleComments(Map<String, String> map) throws Exception;
	int deleteArticleComment(int article_no, int c_id) throws Exception;
	//void modifyArticle(CommentDto commentDto) throws Exception;
	
	List<BoardDto> listTopArticle();
}
