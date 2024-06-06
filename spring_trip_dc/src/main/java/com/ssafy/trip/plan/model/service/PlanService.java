package com.ssafy.trip.plan.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.trip.attraction.model.dto.AttractionReviewDto;
import com.ssafy.trip.attraction.model.dto.AttractionReviewImageDto;
import com.ssafy.trip.plan.model.dto.PlanInfoDto;
import com.ssafy.trip.plan.model.dto.PlanItemInfoDto;
import com.ssafy.trip.plan.model.mapper.PlanMapper;

@Service
public class PlanService {

	@Autowired
	private PlanMapper planMapper;

	public List<PlanInfoDto> getPlanList(String user_id) {
		 List<PlanInfoDto> list = planMapper.getPlanList(user_id);
		 for(PlanInfoDto l : list) {
			 l.setItems(planMapper.getPlanDetail(l));
		 }
		return list;
	}

	public PlanInfoDto getPlanDetail(PlanInfoDto pid) {
		PlanInfoDto res = planMapper.getPlan(pid.getId());
		res.setItems(planMapper.getPlanDetail(res));
		return res;
	}

	public void savePlan(PlanInfoDto pid) {
		planMapper.deleteAllItems(pid);
		planMapper.saveAllItems(pid.getItems());
	}

	public int getisInPlan(PlanItemInfoDto piid) {

		return 0;
	}

	public void addPlan(PlanInfoDto pid) {
		planMapper.addPlan(pid);
	}

	public void removePlan(PlanInfoDto pid) {
		planMapper.removePlan(pid);

	}
	
	public void addPlanItem(PlanItemInfoDto pid) {
		planMapper.addPlanItem(pid);
	}

	public void removePlanItem(PlanItemInfoDto pid) {
		planMapper.removePlanItem(pid);

	}

}
