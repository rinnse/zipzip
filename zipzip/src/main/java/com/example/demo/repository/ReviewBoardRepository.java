package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AgentReview;

@Repository
@Mapper
public interface ReviewBoardRepository {
	
	// 리뷰게시판 리스트
	public List<AgentReview> reviewList(AgentReview agentReview);
	
	// 전체 리스트 건 수 
	public int reviewListCount(AgentReview agentReview);
	
	// 리뷰 등록
	public int reviewInsert(AgentReview agentReview);
	
	// 리뷰 1건 삭제
	public int reviewDelete(long reviewNum);
	
	// 나의 글(리뷰게시판) 조회
	public List<AgentReview> myReviewBoard(AgentReview agentReview);
	
	// 나의 글(리뷰게시판) 전체 건 수 조회
	public int myReviewBoardCount(AgentReview agentReview);
	
	// 나의 글(리뷰게시판) 상세 1건 조회
	public AgentReview myReviewBoardView(long reviewNum);
}
