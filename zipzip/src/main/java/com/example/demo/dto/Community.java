package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Community {
	
	private long rnum;
	private long communityNum;			//커뮤니티 번호
	private String userId;				//유저 아이디
	private String agentId;				//중개인 아이디
	private String communityTitle;		//커뮤니티 제목
	private String communityContent;	//커뮤니티 내용
	private long likeCnt;				//추천수
	private long hateCnt;				//비추천수
	private long readCnt;				//조회수
	private String regDate;				//등록일
	
	private long commentCnt;			//댓글 갯수 변수
	
	private String searchType;			//검색 옵션
	private String searchValue;			//검색 내용
	
	private long startRnum;				//rownum
	private long endRnum;				//rownum
	
	private long sort;					//정렬 구분
}
