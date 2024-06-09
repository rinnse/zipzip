package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Graph;
import com.example.demo.dto.Item;
import com.example.demo.dto.Manager;
import com.example.demo.dto.Report;
import com.example.demo.dto.Search;
import com.example.demo.dto.TodayCnt;
import com.example.demo.dto.User;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.util.Paging;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final ManagerRepository managerRepository;
	private final BCryptPasswordEncoder managerPasswordEncoder;
	
	private static final long LIST_CNT = 10;
	private static final long PAGE_CNT = 5;
	
	// 로그인
	public Manager findById(String managerId) {
		return managerRepository.findById(managerId);
	}
	
	//report list
	public List<Report> selectReportList (long curPage, Report report){
		List<Report> list = null;
		try {
			long totalCnt = listTotalCnt(report);
			if(totalCnt>0) {
				log.info("totalCnt: {}"+totalCnt);
				Paging paging = new Paging("/Manager/report", totalCnt, LIST_CNT, PAGE_CNT, curPage,"curPage");
				report.setStartRnum(paging.getStartRow());
				report.setEndRnum(paging.getEndRow());
				//log.info("totalCnt: {}"+paging.getStartRow());
				//log.info("totalCnt: {}"+paging.getEndRow());
				list= managerRepository.selectReportList(report);
			}
		}
		catch(Exception e) {
			log.debug("[ManagerService] selectReportList Exception",e);
		}
		return list;
	}
	
	//totalCnt
	public long listTotalCnt (Report report) {
		long totalCnt = 0;
		try {
			totalCnt = managerRepository.reportTotalCnt(report);
		}
		catch(Exception e) {
			log.debug("[ManagerService] listTotalCnt Exception ",e);
		}
		return totalCnt;
	}
	
	//today
	public TodayCnt todaySelectCnt () {
		TodayCnt today = null;
		try {
			today = managerRepository.todayPage();
		}
		catch(Exception e) {
			log.debug("[ManagerService] todaySelectCnt Exception ",e);
		}
		return today;
	}
	
	//update
	public int updateReportStatus (Report report) {
		int cnt=0;
		try {
			cnt = managerRepository.updateReportStatus(report);
		}
		catch(Exception e) {
			log.debug("[ManagerService] updateReportStatus Exception", e);
		}
		return cnt;
	}

	
	//userSelectList
	public List<User> selectUserList (long curPage, Search search){
		
		List<User> list = null;
		
		try {
			long totalCnt = selectUserTotal(search);
			if(totalCnt>0) {
				Paging paging = new Paging("/Manager/user", totalCnt, LIST_CNT, PAGE_CNT, curPage,"curPage");
				search.setStartRnum(paging.getStartRow());
				search.setEndRnum(paging.getEndRow());
			}
			list = managerRepository.selectUserList(search);
		}
		catch(Exception e) {
			log.debug("[ManagerService] selectUserList Exception",e);
		}
		return list;
	}
	
	public long selectUserTotal (Search search) {
		long cnt = 0;
		try {
			cnt = managerRepository.selectUserTotal(search);
		}
		catch(Exception e) {
			log.debug("[ManagerService] selectUserTotal Exception",e);
		}
		return cnt;
	}
	
	//유저 업데이트
	public int updateUserStatus (User user) {
		int cnt = 0;
		
		try {
			cnt = managerRepository.updateUserStatus(user);
		}
		catch(Exception e) {
			log.debug("[ManagerService] updateUserStatus Excpetion ",e);
		}
		return cnt;
	}
	
	//graph
	public List<Graph> userChange (){
		List<Graph> list = null;
		try {
			list= managerRepository.userChange();
		}
		catch(Exception e) {
			log.debug("[ManagerService] userChange Exception",e);
		}
		return list;
	}
	
	//itemList
	public List<Item> selectItemList (long curPage, Search search){
		List<Item> list = null;
		try {
			long totalCnt = selectItemTotal(search);
			if(totalCnt>0) {
				Paging paging = new Paging("/Manager/item", totalCnt, LIST_CNT, PAGE_CNT, curPage,"curPage");
				search.setStartRnum(paging.getStartRow());
				search.setEndRnum(paging.getEndRow());
			}
			list = managerRepository.selectItemList(search);
		}
		catch(Exception e) {
			log.info("[ManagerService] selectItemList Exception");
			log.debug("[ManagerService] selectItemList Exception",e);
		}
		return list;
	}
	//itemTotal
	public long selectItemTotal (Search search) {
		long cnt = 0;
		try {
			cnt = managerRepository.selectItemTotal(search);
			log.info("cnt:"+cnt);
			log.info("search."+search.getSearchValue());
		}
		catch(Exception e) {
			log.info("[ManagerService] selectItemTotal Exception");
			log.debug("[ManagerService] selectItemTotal Exception",e);
		}
		return cnt;
	}
	//item update
	public int updateItemStatus (Item item) {
		int cnt = 0;
		try {
			cnt = managerRepository.updateItemStatus(item);
		}
		catch(Exception e) {
			log.debug("[ManagerService] updateItemStatus Exception",e);
		}
		return cnt;
	}
	
	//중개인 리스트
	public List<Agent> selectAgentList (long curPage,Search search){
		List<Agent> list = null;
		try {
			long totalCnt = selectAgentTotal(search);
			//log.info("totalCnt : "+totalCnt);
			if(totalCnt>0) {
				Paging paging = new Paging("/Manager/user", totalCnt, LIST_CNT, PAGE_CNT, curPage,"curPage");
				search.setStartRnum(paging.getStartRow());
				search.setEndRnum(paging.getEndRow());
			}
			list=managerRepository.selectAgentList(search);
		}
		catch(Exception e) {
			log.debug("[ManagerService] selectAgentList Exception",e);
		}
		return list;
	}
	public long selectAgentTotal (Search search) {
		long cnt = 0;
		
		cnt = managerRepository.selectAgentTotal(search);
		
		return cnt;
	}
	//중개인 업데이트
	public int updateAgentStatus (Agent agent) {
		int cnt = 0;
		try {
			cnt = managerRepository.updateAgentStatus(agent);
		}
		catch(Exception e) {
			log.debug("[ManagerService] updateAgentStatus Exception",e);
		}
		return cnt;
	}
	
	// 리뷰관리
	public List<AgentReview> reviewMgtList(AgentReview agentReview){
		return managerRepository.reviewMgtList(agentReview);
	}
	
	// 리뷰 전체 건 수 조회
	public int reviewMgtCount(AgentReview agentReview) {
		return managerRepository.reviewMgtCount(agentReview);
	}
	
	// 리뷰 한 건 삭제
	public int reviewMgtDelete(long reviewNum) {
		return managerRepository.reviewMgtDelete(reviewNum);
	}
	
	// 리뷰 숨김 처리
	public int hideReview(long reviewNum) {
	       return managerRepository.hideReview(reviewNum);
	}

	 // 리뷰 숨김 해제
	public int unhideReview(long reviewNum) {
	      return managerRepository.unhideReview(reviewNum);
	}
}
