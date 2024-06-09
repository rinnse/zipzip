package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AgentReview;
import com.example.demo.repository.ReviewBoardRepository;

@Service
public class ReviewBoardService {
	
	private static Logger logger = LoggerFactory.getLogger(ReviewBoardService.class);
	
	@Autowired
	private ReviewBoardRepository reviewBoardRepository;
	
	// 리뷰게시판 리스트
	public List<AgentReview> reviewList(AgentReview agentReview){
		List<AgentReview> reviewList = null;
		
		try {
			reviewList = reviewBoardRepository.reviewList(agentReview);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] reviewList Exception", e);
		}
		
		return reviewList;
	}
	
	// 전체 리스트 건 수 
	public int reviewListCount(AgentReview agentReview) {
		int count = 0;
		
		try {
			count = reviewBoardRepository.reviewListCount(agentReview);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] reviewListCount Exception", e);
		}
		
		return count;
	}
	
	// 리뷰 등록
	public int reviewInsert(AgentReview agentReview) {
		int count = 0;
		
		try {
			count = reviewBoardRepository.reviewInsert(agentReview);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] reviewInsert Exception", e);
		}
		
		return count;
	}

	// 리뷰 1건 삭제
	public int reviewDelete(long reviewNum) {
		int count = 0;
		
		try {
			count = reviewBoardRepository.reviewDelete(reviewNum);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] reviewDelete Exception", e);
		}
		
		return count;
	}

	// 나의 글(리뷰게시판) 조회
	public List<AgentReview> myReviewBoard(AgentReview agentReview){
		List<AgentReview> myReviewList = null;
		
		try {
			myReviewList = reviewBoardRepository.myReviewBoard(agentReview);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] myReviewBoard Exception", e);
		}
		
		return myReviewList;
	}
	
	// 나의 글(리뷰게시판) 전체 건 수 조회
	public int myReviewBoardCount(AgentReview agentReview) {
		int count = 0;
		
		try {
			count = reviewBoardRepository.myReviewBoardCount(agentReview);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] myReviewBoardCount Exception", e);
		}
		
		return count;
	}
	
	// 나의 글(리뷰게시판) 상세 1건 조회
	public AgentReview myReviewBoardView(long reviewNum){
		AgentReview agentReview = null;
		
		try {
			agentReview = reviewBoardRepository.myReviewBoardView(reviewNum);
		} catch(Exception e) {
			logger.error("[ReviewBoardService] myReviewBoardView Exception", e);
		}
		
		return agentReview;	
	}
}
