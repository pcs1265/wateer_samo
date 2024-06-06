package com.ssafy.trip.water.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewImageDto;
import com.ssafy.trip.attraction.model.dto.GugunDto;
import com.ssafy.trip.attraction.model.dto.SidoDto;
import com.ssafy.trip.water.model.dto.WaterInfoDto;
import com.ssafy.trip.water.model.dto.WaterReviewDto;
import com.ssafy.trip.water.model.dto.WaterReviewImageDto;

@Mapper
public interface WaterMapper {
	
	List<WaterInfoDto> keywordSearch(Map<String, Object> map);

	List<WaterInfoDto> regionSearch(Map<String, Object> map);
	
	WaterInfoDto getDetail(int contentId);

	int getTotalWaterCount(Map<String, Object> param) throws SQLException;

	List<SidoDto> listSido();
	List<GugunDto> listGugun();

	List<WaterReviewDto> reviewList(Map<String, Object> map);

	WaterReviewDto getReviewDetail(int reviewId);
	List<WaterReviewImageDto> getReviewImages(int reviewId);
	int writeReview(WaterReviewDto reviewDto);
	int deleteReview(int reviewId);
	
	int registerImage(WaterReviewImageDto firstImage);
	

	WaterInfoDto pickRandom();
}
