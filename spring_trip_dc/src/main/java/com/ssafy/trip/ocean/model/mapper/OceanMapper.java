package com.ssafy.trip.ocean.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.ocean.model.dto.BeachInfoDto;
import com.ssafy.trip.ocean.model.dto.KhoaObservDto;

@Mapper
public interface OceanMapper {
	
	List<KhoaObservDto> getKhoaObservList();

	List<String> getBeachSidoList();

	List<BeachInfoDto> getBeachList(String sidoNM);
	
	List<BeachInfoDto> getRandomBeachList();

	BeachInfoDto getBeach(int id);
	BeachInfoDto pickRandom();
}
