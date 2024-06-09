package com.example.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long itemNum;				//매물등록번호
	private String itemPtype;			//매물가격타입
	private String itemIntro;			//매물소개			
	private String itemIntroDetail;		//매물 상세설명
	private long itemDeposit;			//보증금/매매가
	private int itemMaintainPrice;		//관리비
	private int itemMonthPrice;			//월세
	private Double itemArea;			//전용면적(m²)
	private String itemParking;			//주차가능여부(주차가능:Y 불가:N)
	private String itemBuildDate;		//준공일자(20240412)
	private String itemElevator;		//엘레베이터설치여부(Y/N)
	private String itemPet;				//반려동물 동반 가능 여부(Y/N)
	private String itemDirection;		//매물방향(남동향 등)
	private String itemKind;			//건축물대장 주용도
	private int itemFloor;				//층수(0:옥탑 -1:반지하)
	private String itemMoveAvbl;		//즉시입주가능:Y 불가:N
	private String itemMoveDate;		//입주가능일
	private String itemNear;			//상권

	
}
