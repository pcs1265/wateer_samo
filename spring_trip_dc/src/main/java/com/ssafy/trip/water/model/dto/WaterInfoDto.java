package com.ssafy.trip.water.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "AttractionInfoDto : 관광지 정보", description = "관광지 정보를 나타낸다.")
public class WaterInfoDto {
	
	private int objtId;
	private String wtrplayPlcNm;
	private String ctprvnNm;
	private String sggNm;
	private String emdNm;
	private String detailPlcnm;
	private String adres;
	private String wtrplayPlcType;
	private String equipCo;
	private String riskSignCnt;
	private String humlfRescuship;
	private String rovngnsStandsCnt;
	private String rescueVestCnt;
	private String lifebuoyCnt;
	private String rescueRopeCnt;
	private String rescubngCnt;
	private String etcCnt;
	private String wtrplaySec;
	private String wtrplayDeep;
	private String wtrplayDeepAvg;
	private String yearVisitor;
	private String acdtCo;
	private String etcNote;
	private String longitude;
	private String latitude;
	private String managementType;
	private String manageAt;
	private String wtrplayEr;
	private String erSignCo;
	private String MngDup;
	private String safetyAct;
	
	private double reviewRating;
	private double reviewNumber;
	
	
}
