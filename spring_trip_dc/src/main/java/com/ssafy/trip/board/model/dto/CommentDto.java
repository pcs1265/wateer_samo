package com.ssafy.trip.board.model.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "CommentDto : 댓글정보", description = "댓글의 상세 정보를 나타낸다.")
public class CommentDto {

	@Schema(description = "댓글번호")
	private int id;
	@Schema(description = "대상 게시글 번호")
	private int articleNo;
	@Schema(description = "작성자 아이디")
	private String writer;
	@Schema(description = "작성자 닉네임")
	private String nickname;
	@Schema(description = "댓글내용")
	private String content;
	@Schema(description = "작성일")	
	private String writeDate;
	@Schema(description = "수정일")	
	private String modifyDate;
	@Schema(description = "삭제일")	
	private String deleteDate;

}
