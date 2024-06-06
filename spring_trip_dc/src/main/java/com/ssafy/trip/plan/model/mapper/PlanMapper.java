package com.ssafy.trip.plan.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.plan.model.dto.PlanInfoDto;
import com.ssafy.trip.plan.model.dto.PlanItemInfoDto;

@Mapper
public interface PlanMapper {
	
	public List<PlanInfoDto> getPlanList(String user_id);

	public List<PlanItemInfoDto> getPlanDetail(PlanInfoDto pid);

	public PlanInfoDto getPlan(int pid);

	public void deleteAllItems(PlanInfoDto pid);
	public void saveAllItems(List<PlanItemInfoDto> pid);

	public int getisInPlan(PlanItemInfoDto piid);

	public void addPlan(PlanInfoDto pid);

	public void removePlan(PlanInfoDto pid);
	
	public void addPlanItem(PlanItemInfoDto pid);

	public void removePlanItem(PlanItemInfoDto pid);
}
