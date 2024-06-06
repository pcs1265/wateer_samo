package com.ssafy.trip.attraction.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "AttractionReviewDto : 게시글정보", description = "관광지 리뷰에 등록된 사진 정보를 나타낸다.")
public class AttractionReviewImageDto {
	@Schema(description = "사진 번호")
	private int id;

	@Schema(description = "리뷰 번호")
	private int reviewId;

	@Schema(description = "내용")
	private String fileName;
	
}
