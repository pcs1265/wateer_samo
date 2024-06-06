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
@Schema(title = "BoardDto : 게시글정보", description = "게시글의 상세 정보를 나타낸다.")
public class BoardDto {

	@Schema(description = "글번호")
	private int no;
	@Schema(description = "작성자 아이디")
	private String writer;
	@Schema(description = "작성자 닉네임")
	private String nickname;
	@Schema(description = "글제목")
	private String title;
	@Schema(description = "글내용")
	private String content;
	@Schema(description = "조회수")
	private int hits;
	@Schema(description = "작성일")	
	private String writeDate;
	@Schema(description = "수정일")	
	private String modifyDate;
	@Schema(description = "삭제일")	
	private String deleteDate;
	@Schema(description = "업로드 파일정보")
	private List<FileInfoDto> fileInfos;

}
