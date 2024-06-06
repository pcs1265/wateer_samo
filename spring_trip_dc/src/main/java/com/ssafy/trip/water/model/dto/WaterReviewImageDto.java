package com.ssafy.trip.water.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "WaternReviewDto : 게시글정보", description = "물놀이 리뷰에 등록된 사진 정보를 나타낸다.")
public class WaterReviewImageDto {
	@Schema(description = "사진 번호")
	private int id;

	@Schema(description = "리뷰 번호")
	private int reviewId;

	@Schema(description = "내용")
	private String fileName;
	
}
