package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentReview {
	private long reviewNum;
	private String userId;
	private String agentId;
	private String reviewContent;
	private int reviewScore;
	private String regDate;
	
	// 리뷰게시판 검색
	private String searchType;
	private String searchValue;    // 리뷰내용, 중개사이름 다 searchValue로 받기
	
	// 페이징 
	private long startRow;
	private long endRow;
	
	// 중개사 정보
	private String agentName;
	private String agentOfficeName;

	// 옵션
	private String option;
	
	// 리뷰내용 비공개 처리
	private int hidden;
	
	// 나의 글(리뷰게시판)
	// 검색조건
	private String searchOption;
	// 검색값 
	private String searchContent;
}
