package com.ssafy.trip.attraction.model.dto;

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
@Schema(title = "AttractionInfoDto : 관광지 정보", description = "관광지 정보를 나타낸다.")
public class AttractionInfoDto {
	private String contentId;
	private String contentTypeId;
	private String title;
	private String addr1;
	private String addr2;
	private String zipcode;
	private String tel;
	private String firstImage;
	private String firstImage2;
	private String readcount;
	private String sidoCode;
	private String gugunCode;
	private String latitude;
	private String longitude;
	private String mlevel;
	private String overview;
	
	private double reviewRating;
	private double reviewNumber;
	
	
}
