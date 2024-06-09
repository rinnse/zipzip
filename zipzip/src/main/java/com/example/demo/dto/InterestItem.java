package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestItem {
   private String userId;       // 유저아이디 
   private long itemNum;      	// 매물 등록 번호
   private String regDate;      // 찜목록 등록일
   
   //찜목록 조회를 위한 추가 
   private String itemName;        	// 매물이름
   private String itemKind;         // 건축물대장 주용도
   private String itemInfo;         // 매물정보   
   private String agentName;		// 중개사 이름
   private String agentOfficeName;	// 중개사무소 이름
   private String agentInfo;        // 중개정보

	
}
