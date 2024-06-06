package com.ssafy.trip.ocean.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "BeachInfoDto : 해수욕장 정보", description = "해수욕장 정보를 나타낸다.")
public class BeachInfoDto {
	private int id;
	private String name;
	private String sidoNm;
	private String gugunNm;
	private String staNm;
	private String parcelAddr;
	private String roadAddr;
	private float lat;
	private float lon;
}