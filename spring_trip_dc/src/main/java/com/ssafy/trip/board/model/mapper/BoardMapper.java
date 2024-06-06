package com.ssafy.trip.board.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.board.model.dto.CommentDto;
import com.ssafy.trip.board.model.dto.FileInfoDto;

@Mapper
public interface BoardMapper {

	int writeArticle(BoardDto boardDto) throws SQLException;

	void registerFile(BoardDto boardDto) throws Exception;

	List<BoardDto> listArticle(Map<String, Object> param) throws SQLException;

	int getTotalArticleCount(Map<String, Object> param) throws SQLException;

	BoardDto getArticle(int no) throws SQLException;

	void updateHit(int no) throws SQLException;

	int modifyArticle(BoardDto boardDto) throws SQLException;

	void deleteFile(int articleNo) throws Exception;

	int deleteArticle(int no) throws SQLException;

	List<FileInfoDto> fileInfoList(int articleNo) throws Exception;
	
	int writeArticleComment(CommentDto commentDto) throws SQLException;
	List<CommentDto> getArticleComments(Map<String, Object> param) throws SQLException;
	int getTotalArticleCommentsCount(Map<String, Object> param) throws SQLException;
	int deleteArticleComment(@Param("article_no") int article_no, @Param("c_id") int c_id) throws SQLException;
	
	List<AttractionInfoDto> viewWishs(String userId);
	

	List<BoardDto> listTopArticle();
	
	
}
