package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Response;
import com.example.demo.dto.User;
import com.example.demo.service.AgentService;
import com.example.demo.service.ReviewBoardService;
import com.example.demo.service.UserService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewBoardController {

	private final UserService userService;
	private final ReviewBoardService reviewBoardService;
	private final AgentService agentService;
	
	// 페이징 상수 정의
	private static final int LIST_COUNT = 4;   // 한 페이지의 리스트 수
	private static final int PAGE_COUNT = 5;   // 페이징 범위 수 
	
	// 리뷰게시판 페이지
	@RequestMapping("/board/reviewBoard")
	public String reviewBoard(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 리뷰리스트
		List<AgentReview> reList = null;
		AgentReview agentReview = new AgentReview();
		Paging paging = null;
		
		// 현재 페이지
		String curPageString = request.getParameter("curPage");
		int curPage;
		if (curPageString != null && !curPageString.isEmpty()) {
		    try {
		        curPage = Integer.parseInt(curPageString);
		    } catch (NumberFormatException e) {
		        curPage = 1; // 기본값 
		    }
		} else {
		    curPage = 1; // 기본값 
		}

		// 검색 조건
		String searchType = request.getParameter("searchType");
		// 검색 값
		String searchValue = request.getParameter("searchValue");  // reviewBoard.html에 있는 name
	
		if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue)) {
			// 검색조건과 값이 있을 경우
			if(StringUtil.equals(searchType, "1")) {
				// 리뷰 내용
				agentReview.setReviewContent(searchValue);
			} else if(StringUtil.equals(searchType, "2")) {
				// 중개사 이름 
				agentReview.setAgentName(searchValue);
			} else {
				searchType = "";
				searchValue = "";
			}
		}
		  
		int totalCount = reviewBoardService.reviewListCount(agentReview);
		
		if(totalCount > 0) { // searchValue에 리뷰내용/중개사이름 값 받음 
			// 페이징 처리 
			paging = new Paging("/board/reviewBoard", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			
			agentReview.setStartRow(paging.getStartRow()); 
			agentReview.setEndRow(paging.getEndRow()); // 한 페이지에 8개씩 보여줌

			reList = reviewBoardService.reviewList(agentReview); // 리스트 담기 
		} 
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("userId", userId);
	    model.addAttribute("reList", reList);
	    model.addAttribute("searchType", searchType);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
	
		return "board/reviewBoard";
	}
	
	// 리뷰 작성 페이지 (<- 채팅 이후 작성 가능..)
	@RequestMapping("/board/writeReview")
	public String writeReview(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		// 채팅한 사용자 아이디(= 작성자)
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId); 	
		
		// 중개사 
		String agentId = "test2";  // 하드코딩해둠 (채팅한 중개인의 아이디 정보)
		Agent agent = agentService.findById(agentId);
		
		// 별점 (숫자타입이기 때문에 문자열로 변환)
		String ratingString = request.getParameter("rating");
		int rating;
		if (ratingString != null && !ratingString.isEmpty()) {
			try {
				rating = Integer.parseInt(ratingString);
			} catch(NumberFormatException e) {
				rating = 0;  // 디폴트값
			}
		} else {
			rating = 0; // 디폴트값
		}

		model.addAttribute("user", user);
		model.addAttribute("userId", userId);
		model.addAttribute("agent", agent);
		model.addAttribute("agentId", agentId);
		model.addAttribute("rating", rating);
		
		return "board/writeReview";
	}
	
	// 리뷰 등록 처리 
	@RequestMapping("/board/reviewProc")
	@ResponseBody
	public Response<Object> reviewProc(HttpServletRequest request, HttpServletResponse response){
		Response<Object> ajaxResponse = new Response<Object>();
		
		// 채팅한 사용자 아이디(= 작성자)
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		
		// 중개사 
		String agentId = "test2";    // (채팅한 중개인의 아이디 정보)
		Agent agent = agentService.findById(agentId);
		
		// 리뷰 내용
		String reviewContent = request.getParameter("reviewContent");
		// 별점
		String ratingString = request.getParameter("rating");
		int rating;
		if (ratingString != null && !ratingString.isEmpty()) {
			try {
				rating = Integer.parseInt(ratingString);
			} catch(NumberFormatException e) {
				rating = 0;  // 디폴트값
			}
		} else {
			rating = 0;  // 디폴트값
		}
		
		// 리뷰 내용이 입력된 경우
		if(!StringUtil.isEmpty(reviewContent)) {
			AgentReview agentReview = new AgentReview();
			
			// 값 세팅
			agentReview.setUserId(userId);  
			agentReview.setAgentId(agentId);
			agentReview.setReviewContent(reviewContent);
			agentReview.setReviewScore(rating); 
			
			if(reviewBoardService.reviewInsert(agentReview) > 0) {
				// 리뷰 등록 처리 건 수가 존재할 경우
				ajaxResponse.setResponse(0, "Success");
			} else {
				// 등록 처리가 안 되는 경우
				ajaxResponse.setResponse(500, "internal Server error");
			}
		} else {
			// 리뷰 내용 값이 안 넘어온 경우
			ajaxResponse.setResponse(400, "Bad Request");
		}
		
		return ajaxResponse;
	}
	
	// 리뷰 1건 삭제
	@RequestMapping("/review/delete")
	@ResponseBody
	public Response<Object> delete(HttpServletRequest request, HttpServletResponse response){
		Response<Object> ajaxResponse = new Response<Object>();

		// 리뷰 글 번호 
		String reviewNumString = request.getParameter("reviewNum");
		long reviewNum;
		if (reviewNumString != null && !reviewNumString.isEmpty()) {
		    try {
		    	reviewNum = Integer.parseInt(reviewNumString);
		    } catch (NumberFormatException e) {
		    	reviewNum = 0; // 기본값 
		    }
		} else {
			reviewNum = 0; // 기본값 
		}
		
		// log.info("reviewNum : " + reviewNum);  
		
		if(reviewNum > 0) {
			// 글번호 존재할 경우
			if(reviewBoardService.reviewDelete(reviewNum) > 0) {
				// 리뷰 삭제 건 수가 있을 경우
				ajaxResponse.setResponse(0, "Success");
			} else {
				// 삭제 건 수가 없을 경우
				ajaxResponse.setResponse(500, "server error");
			}
		} else {
			// 글번호가 존재하지 않는 경우
			ajaxResponse.setResponse(400, "Bad Request");
		}
		
		return ajaxResponse;
	}
	
}