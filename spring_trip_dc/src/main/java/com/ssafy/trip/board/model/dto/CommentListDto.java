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
@Schema(title = "CommentListDto : 댓글 목록 & 페이지 정보", description = "댓글 목록과 현재 페이지와 전체 페이지 정보를 나타낸다.")
public class CommentListDto {

	@Schema(description = "댓글목록")
	private List<CommentDto> comments;
	@Schema(description = "현재 페이지번호")
	private int currentPage;
	@Schema(description = "전체 페이지 수")
	private int totalPageCount;

}
