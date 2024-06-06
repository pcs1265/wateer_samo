package com.ssafy.trip.water.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.trip.attraction.model.dto.GugunDto;
import com.ssafy.trip.attraction.model.dto.SidoDto;
import com.ssafy.trip.water.model.dto.WaterInfoDto;
import com.ssafy.trip.water.model.dto.WaterInfoListDto;
import com.ssafy.trip.water.model.dto.WaterReviewDto;
import com.ssafy.trip.water.model.dto.WaterReviewImageDto;
import com.ssafy.trip.water.model.dto.WaterReviewListDto;
import com.ssafy.trip.water.model.mapper.WaterMapper;

@Service
public class WaterService {

	@Autowired
	private WaterMapper waterMapper;

	public WaterInfoListDto keywordSearch(String keyword, String wtrplayPlcType, int perPage, int page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("keyword", keyword);
		map.put("wtrplayPlcType", wtrplayPlcType);
		
		
		int currentPage = page;
		int sizePerPage = perPage;
		int start = currentPage * sizePerPage - sizePerPage;
		map.put("start", start);
		map.put("listsize", sizePerPage);

		map.put("searchType", "keyword");
		
		List<WaterInfoDto> list = waterMapper.keywordSearch(map);

		int totalArticleCount = waterMapper.getTotalWaterCount(map);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		WaterInfoListDto waterInfoListDto = new WaterInfoListDto();
		waterInfoListDto.setWaters(list);
		waterInfoListDto.setCurrentPage(currentPage);
		waterInfoListDto.setTotalPageCount(totalPageCount);

		return waterInfoListDto;
	}
	
	public WaterInfoListDto regionSearch(Map<String, Object> map, int perPage, int page) throws SQLException {
		int currentPage = page;
		int sizePerPage = perPage;
		int start = currentPage * sizePerPage - sizePerPage;
		map.put("start", start);
		map.put("listsize", sizePerPage);

		map.put("searchType", "region");
		System.out.println(map.get("sido"));
		List<WaterInfoDto> list = waterMapper.regionSearch(map);

		int totalArticleCount = waterMapper.getTotalWaterCount(map);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		WaterInfoListDto waterInfoListDto = new WaterInfoListDto();
		waterInfoListDto.setWaters(list);
		waterInfoListDto.setCurrentPage(currentPage);
		waterInfoListDto.setTotalPageCount(totalPageCount);

		return waterInfoListDto;
	}
	
	public WaterInfoDto getDetail(int objtId) throws SQLException {
		WaterInfoDto info = waterMapper.getDetail(objtId);
		return info;
	}
	
	public List<SidoDto> listSido() {
		return waterMapper.listSido();
	}
	
	public List<GugunDto> listGugun() {
		return waterMapper.listGugun();
	}
	
	public WaterReviewListDto getReviewList(int objtId, int perPage, int page) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		int currentPage = page;
		int sizePerPage = perPage;
		int start = currentPage * sizePerPage - sizePerPage;
		map.put("objtId", objtId);
		map.put("start", start);
		map.put("listsize", sizePerPage);
		
		List<WaterReviewDto> list = waterMapper.reviewList(map);

		int totalArticleCount = waterMapper.getTotalWaterCount(map);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		WaterReviewListDto waterReviewListDto = new WaterReviewListDto();
		waterReviewListDto.setReviews(list);
		waterReviewListDto.setCurrentPage(currentPage);
		waterReviewListDto.setTotalPageCount(totalPageCount);

		return waterReviewListDto;
	}
	
	public WaterReviewDto getReviewDetail(int reviewId) throws SQLException {
		WaterReviewDto review = waterMapper.getReviewDetail(reviewId);
		review.setImageInfos(waterMapper.getReviewImages(reviewId));
		return review;
	}
	
	public WaterReviewDto writeReview(WaterReviewDto reviewDto) throws SQLException {
		waterMapper.writeReview(reviewDto);
		List<WaterReviewImageDto> imageInfos = reviewDto.getImageInfos();
		if (imageInfos != null && !imageInfos.isEmpty()) {
			WaterReviewImageDto firstImage = imageInfos.get(0);
			firstImage.setReviewId(reviewDto.getId());
			waterMapper.registerImage(firstImage);
		}
		return reviewDto;
	}
	
	public int deleteReview(int reviewId) throws SQLException {
		int res = waterMapper.deleteReview(reviewId);
		return res;
	}
	
	public WaterInfoDto pickRandom() throws SQLException {
		WaterInfoDto info = waterMapper.pickRandom();
		return info;
	}
	
}
