package com.ssafy.trip.attraction.model.dto;

import java.util.List;

import com.ssafy.trip.board.model.dto.FileInfoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "AttractionReviewDto : 게시글정보", description = "관광지에 대한 리뷰를 나타낸다.")
public class AttractionReviewDto {

	@Schema(description = "리뷰 번호")
	private int id;

	@Schema(description = "관광지 번호")
	private int attractionId;

	@Schema(description = "내용")
	private String content;

	@Schema(description = "작성자ID")
	private String writer;
	@Schema(description = "작성자 닉네임")
	private String nickname;

	@Schema(description = "별점")
	private int rating;

	@Schema(description = "썸네일 정보")
	private String firstImage;

	@Schema(description = "이미지 정보")
	private List<AttractionReviewImageDto> imageInfos;
}
