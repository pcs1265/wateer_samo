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
public class PlanInfoDto {
	private int id;
	private String name;
	private String user_id;	
	private List<PlanItemInfoDto> items;
}
