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
@Schema(title = "SidoDto : 시도정보")
public class SidoDto {
	private int sidoCode;
	private String sidoName;
}
