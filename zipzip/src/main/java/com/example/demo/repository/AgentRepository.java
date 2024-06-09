package com.example.demo.repository;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Agent;
import com.example.demo.dto.Notice;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AgentRepository {
	
	private final SqlSessionTemplate sql;
	
	// 회원가입
	public void insert(Agent agent) {
		sql.insert("Agent.insert", agent);
	}
	
	// 로그인
	public Agent findById(String agentId) {
		return sql.selectOne("Agent.findById", agentId);
	}
	
	// 아이디 확인
	public int idCheckAg(String agentId) {
		return sql.selectOne("Agent.idCheckAg", agentId);
	}
	
	// 중개인 정보 조회
	public Agent agentSelect(String agentId) {
		return sql.selectOne("Agent.agentSelect", agentId);
	}
	
	// 중개인 정보 수정
	public void agentUpdate(Agent agent) {
		sql.update("Agent.agentUpdate", agent);
	}
	
	// 프로필 변경
	public void updateProfile(Agent agent) {
		sql.update("Agent.updateProfile", agent);
	}
	
	// 비밀번호 확인
	public String pwdCheck(String agentId) {
		return sql.selectOne("Agent.pwdCheck", agentId);
	}
	
	// 비밀번호 변경
	public void pwdUpdate(String agentId, String agentPwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agentId", agentId);
		map.put("agentPwd", agentPwd);
		
		sql.update("Agent.pwdUpdate", map);
	}
	
	// 탈퇴
	public void updateStatus(Agent agent) {
		sql.update("Agent.updateStatus", agent);
	}
	
	// 결제 확인
	public void updateRight(Agent agent) {
		sql.update("Agent.updateRight", agent);
	}
	
}
