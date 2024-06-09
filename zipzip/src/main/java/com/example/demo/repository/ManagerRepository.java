package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Graph;
import com.example.demo.dto.Item;
import com.example.demo.dto.Manager;
import com.example.demo.dto.Report;
import com.example.demo.dto.Search;
import com.example.demo.dto.TodayCnt;
import com.example.demo.dto.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ManagerRepository {
	
	private final SqlSessionTemplate sql;
	
	// 로그인
	public Manager findById(String managerId) {
		return sql.selectOne("Manager.findById", managerId);
	}
	
	public List<Report> selectReportList (Report report){
		return sql.selectList("Manager.selectReportList", report);
	};
	
	public long reportTotalCnt (Report report) {
		return sql.selectOne("Manager.reportTotalCnt", report);
	}
	
	public int updateReportStatus (Report report) {
		return sql.update("updateReportStatus",report);
	}
	
	public List<User> selectUserList (Search search){
		return sql.selectList("selectUserList",search);
	}
	
	public long selectUserTotal (Search search) {
		return sql.selectOne("selectUserTotal",search);
	}
	
	public int updateUserStatus (User user) {
		return sql.update("updateUserStatus",user);
	}
	
	//today
	public TodayCnt todayPage () {
		return sql.selectOne("todayPage");
	}
	//중개인리스트
	public List<Agent> selectAgentList (Search search){
		return sql.selectList("selectAgentList",search);
	}
	public long selectAgentTotal (Search search) {
		return sql.selectOne("selectAgentTotal",search);
	}
	//agent update
	public int updateAgentStatus (Agent agent) {
		return sql.update("updateAgentStatus",agent);
	}
	//itemList
	public List<Item> selectItemList (Search search){
		return sql.selectList("selectItemList",search);
	}
	//itemTotal
	public long selectItemTotal (Search search) {
		return sql.selectOne("selectItemTotal",search);
	}
	//itemUpdate
	public int updateItemStatus (Item item) {
		return sql.update("updateItemStatus",item);
	}
	
	//graph
	public List<Graph> userChange (){
		return sql.selectList("userChange");
	}
	
	// 리뷰관리
	public List<AgentReview> reviewMgtList(AgentReview agentReview) {
		return sql.selectList("Manager.reviewMgtList", agentReview);
	}
	
	// 리뷰 전체 건 수 조회
	public int reviewMgtCount(AgentReview agentReview) {
		return sql.selectOne("Manager.reviewMgtCount", agentReview);
	}
	
	// 리뷰 한 건 삭제
	public int reviewMgtDelete(long reviewNum) {
		return sql.delete("Manager.reviewMgtDelete", reviewNum);
	}
	
	
	// 리뷰 숨김 처리
    public int hideReview(long reviewNum) {
        return sql.update("Manager.hideReview", reviewNum);
    }

    // 리뷰 숨김 해제
    public int unhideReview(long reviewNum) {
        return sql.update("Manager.unhideReview", reviewNum);
    }
	
}
