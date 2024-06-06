package com.ssafy.trip.ocean.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.trip.ocean.model.dto.BeachInfoDto;
import com.ssafy.trip.ocean.model.dto.KhoaObservDto;
import com.ssafy.trip.ocean.model.mapper.OceanMapper;

@Service
public class OceanService {

	@Autowired
	private OceanMapper oceanMapper;

	public List<KhoaObservDto> getKhoaObservList() throws SQLException {
		return oceanMapper.getKhoaObservList();
	}

	public List<String> getBeachSidoList() throws SQLException {
		return oceanMapper.getBeachSidoList();
	}
	
	public List<BeachInfoDto> getBeachList(String sidoNM) throws SQLException {
		return oceanMapper.getBeachList(sidoNM);
	}
	
	public List<BeachInfoDto> getRandomBeachList() throws SQLException {
		return oceanMapper.getRandomBeachList();
	}
	
	public BeachInfoDto getBeach(int id) throws SQLException {
		return oceanMapper.getBeach(id);
	}
	
	public BeachInfoDto pickRandom() throws SQLException {
		return oceanMapper.pickRandom();
	}
}
