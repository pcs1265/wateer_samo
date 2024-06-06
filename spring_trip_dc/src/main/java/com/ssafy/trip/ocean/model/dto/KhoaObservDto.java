package com.ssafy.trip.ocean.model.dto;

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
@Schema(title = "KhoaObservDto : 국립해양조사원 관측소 정보", description = "국립해양조사원 관측소 정보를 나타낸다.")
public class KhoaObservDto {

	@Schema(description = "관측소 번호")
	private String id;

	@Schema(description = "이름")
	private String name;

	@Schema(description = "위도")
	private double latitude;
	
	@Schema(description = "경도")
	private double longitude;
}
