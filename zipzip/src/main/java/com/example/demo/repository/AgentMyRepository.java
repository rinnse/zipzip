package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemDetail;
import com.example.demo.dto.ItemFile;
import com.example.demo.dto.ItemOption;

@Repository
@Mapper
public interface AgentMyRepository {
	// 나의 리뷰 평균 조회 
	public double avgReviewScore(String agentId);
		
	// 나의 총 리뷰 리스트
	public List<AgentReview> myReviewList(AgentReview agentReview);
		
	// 나의 총 리뷰 수
	public int myReviewCount(String agentId);
	
	//============================================================= YH
	
	//매물 등록
	public int itemInsert(Item item);
	//매물 등록 - 상세
	public int itemDetailInsert(ItemDetail itemDetail);
	//매물 등록 - 옵션
	public int itemOptionInsert(ItemOption itemOption);
	//매물 등록 - 파일
	public int itemFileInsert(ItemFile itemFile);
	
	//등록 매물 리스트
	public List<Item> myItemRegList(Item item);
	
	//매물삭세 (매물,상세,옵션,파일)
	public int deleteItem(long itemNum);
	public int deleteItemDetail(long itemNum);
	public int deleteItemOption(long itemNum);
	public int deleteItemFile(long itemNum);
	
	//item 단건 조회
	public Item itemSelect(long itemNum);
	
	//매물 수정
	public int itemUpdate(Item item);
	public int itemDetailUpdate(ItemDetail itemDetail);
	public int itemOptionUpdate(ItemOption itemOption);
	
	//============================================================= YH 끝
	
}
