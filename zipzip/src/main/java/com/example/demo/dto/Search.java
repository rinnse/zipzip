package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class Search {
	
	private String userId;			//쿠키아이디
	private long itemNum;			//매물번호
	//private String searchAdd;		//주소
	private Double searchSize;		//평수
	private long priceStart;		//가격시작
	private long priceEnd;			//가격끝
	private String pType;			//지불타입	
	
	private List<String> searchAdd;
	
	private String searchType;		//아파트or원룸 ...
	private String searchValue;
	
	private long startRnum;
	private long endRnum;
	
}
