package com.ssafy.trip.attraction.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.trip.attraction.model.dto.AttractionInfoDto;
import com.ssafy.trip.attraction.model.dto.AttractionInfoListDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewImageDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewListDto;
import com.ssafy.trip.attraction.model.dto.AttractionWishDto;
import com.ssafy.trip.attraction.model.dto.GugunDto;
import com.ssafy.trip.attraction.model.dto.SidoDto;
import com.ssafy.trip.attraction.model.mapper.AttractionMapper;

@Service
public class AttractionService {

	@Autowired
	private AttractionMapper attractionMapper;

	public AttractionInfoListDto keywordSearch(String keyword, int contentTypeId, int perPage, int page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("keyword", keyword);
		map.put("content_type_id", contentTypeId);
		
		int currentPage = page;
		int sizePerPage = perPage;
		int start = currentPage * sizePerPage - sizePerPage;
		map.put("start", start);
		map.put("listsize", sizePerPage);

		map.put("searchType", "keyword");
		
		List<AttractionInfoDto> list = attractionMapper.keywordSearch(map);

		int totalArticleCount = attractionMapper.getTotalAttractionCount(map);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		AttractionInfoListDto attractionInfoListDto = new AttractionInfoListDto();
		attractionInfoListDto.setAttractions(list);
		attractionInfoListDto.setCurrentPage(currentPage);
		attractionInfoListDto.setTotalPageCount(totalPageCount);

		return attractionInfoListDto;
	}
	
	public AttractionInfoListDto regionSearch(Map<String, Object> map, int perPage, int page) throws SQLException {
		int currentPage = page;
		int sizePerPage = perPage;
		int start = currentPage * sizePerPage - sizePerPage;
		map.put("start", start);
		map.put("listsize", sizePerPage);

		map.put("searchType", "region");
		
		List<AttractionInfoDto> list = attractionMapper.regionSearch(map);

		int totalArticleCount = attractionMapper.getTotalAttractionCount(map);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		AttractionInfoListDto attractionInfoListDto = new AttractionInfoListDto();
		attractionInfoListDto.setAttractions(list);
		attractionInfoListDto.setCurrentPage(currentPage);
		attractionInfoListDto.setTotalPageCount(totalPageCount);

		return attractionInfoListDto;
	}
	
	public AttractionInfoDto getDetail(int contentId) throws SQLException {
		AttractionInfoDto info = attractionMapper.getDetail(contentId);
		return info;
	}
	
	public List<SidoDto> listSido() {
		return attractionMapper.listSido();
	}
	
	public List<GugunDto> listGugun(int sido) {
		return attractionMapper.listGugun(sido);
	}
	
	public AttractionReviewListDto getReviewList(int contentId, int perPage, int page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		int currentPage = page;
		int sizePerPage = perPage;
		int start = currentPage * sizePerPage - sizePerPage;
		map.put("contentId", contentId);
		map.put("start", start);
		map.put("listsize", sizePerPage);
		
		List<AttractionReviewDto> list = attractionMapper.reviewList(map);

		int totalArticleCount = attractionMapper.getTotalAttractionCount(map);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		AttractionReviewListDto attractionReviewListDto = new AttractionReviewListDto();
		attractionReviewListDto.setReviews(list);
		attractionReviewListDto.setCurrentPage(currentPage);
		attractionReviewListDto.setTotalPageCount(totalPageCount);

		return attractionReviewListDto;
	}
	
	public AttractionReviewDto getReviewDetail(int reviewId) throws SQLException {
		AttractionReviewDto review = attractionMapper.getReviewDetail(reviewId);
		review.setImageInfos(attractionMapper.getReviewImages(reviewId));
		return review;
	}
	
	public AttractionReviewDto writeReview(AttractionReviewDto reviewDto) throws SQLException {
		attractionMapper.writeReview(reviewDto);
		List<AttractionReviewImageDto> imageInfos = reviewDto.getImageInfos();
		if (imageInfos != null && !imageInfos.isEmpty()) {
			AttractionReviewImageDto firstImage = imageInfos.get(0);
			firstImage.setReviewId(reviewDto.getId());
			attractionMapper.registerImage(firstImage);
		}
		return reviewDto;
	}
	
	public int deleteReview(int reviewId) throws SQLException {
		int res = attractionMapper.deleteReview(reviewId);
		return res;
	}
	
	
	public int addWish(AttractionWishDto wish) {
		return attractionMapper.addWish(wish);
	}
	
	public int popWish(Map<String,Object> wish) {
		return attractionMapper.popWish(wish);
	}
	
	public List<AttractionInfoDto> viewWishs(String userId){
		return attractionMapper.viewWishs(userId);
	}
	
	public AttractionInfoDto viewWish(String userId,int itemId) {
		return attractionMapper.viewWish(userId,itemId);
	}
	
	public List<AttractionInfoDto> listPopular(){
		return attractionMapper.listPopular();
	}
	
	public AttractionInfoDto pickRandom() {
		return attractionMapper.pickRandom();
	}
}
