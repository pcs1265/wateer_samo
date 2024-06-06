package com.ssafy.trip.attraction.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewImageDto;
import com.ssafy.trip.attraction.model.dto.AttractionWishDto;
import com.ssafy.trip.attraction.model.dto.GugunDto;
import com.ssafy.trip.attraction.model.dto.SidoDto;

@Mapper
public interface AttractionMapper {
	
	List<AttractionInfoDto> keywordSearch(Map<String, Object> map);

	List<AttractionInfoDto> regionSearch(Map<String, Object> map);
	
	AttractionInfoDto getDetail(int contentId);

	int getTotalAttractionCount(Map<String, Object> param) throws SQLException;

	List<SidoDto> listSido();
	List<GugunDto> listGugun(int sido);

	List<AttractionReviewDto> reviewList(Map<String, Object> map);

	AttractionReviewDto getReviewDetail(int reviewId);
	List<AttractionReviewImageDto> getReviewImages(int reviewId);
	int writeReview(AttractionReviewDto reviewDto);
	int deleteReview(int reviewId);
	
	int registerImage(AttractionReviewImageDto image);
	
	int addWish(AttractionWishDto wish);
	int popWish(Map<String,Object> wish);
	List<AttractionInfoDto> viewWishs(String userId);
	AttractionInfoDto viewWish(String userId,int itemId);
	
	List<AttractionInfoDto> listPopular();
	

	AttractionInfoDto pickRandom();
}
