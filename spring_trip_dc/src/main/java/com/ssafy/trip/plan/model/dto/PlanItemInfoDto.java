package com.ssafy.trip.plan.model.dto;

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
public class PlanItemInfoDto {
	private int id;
	private String name;
	private int plan_id;
	private String addr;
	private double latitude;
	private double longitude;
	private String description;
	private int order;
	
}
