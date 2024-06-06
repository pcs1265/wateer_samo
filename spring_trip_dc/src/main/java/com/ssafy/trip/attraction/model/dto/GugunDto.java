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
@Schema(title = "GugunDto : 구군정보")
public class GugunDto {
	private int gugunCode;
	private String gugunName;
	private int sidoCode;
}
