package com.ssafy.trip.attraction.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(title = "AttractionWish : 찜 정보", description = "찜한 아이템.")
public class AttractionWishDto {
	
	private String userId;
	private int itemId;
}
