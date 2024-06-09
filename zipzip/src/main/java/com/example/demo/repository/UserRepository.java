package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.InterestItem;
import com.example.demo.dto.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final SqlSessionTemplate sql;
	
	// 회원가입
	public void insert(User user) {
		log.info("UserRepository => insert");
		
		sql.insert("User.insert", user);
	}
   
	// 로그인
	public User findById(String userId) {
		return sql.selectOne("User.findById", userId);
	}
	
	// 아이디 중복 검사
	public int idCheck(String userId) {
		log.info("UserRepository => idCheck");
		
		return sql.selectOne("User.idCheck", userId);
	}
	
	// 아이디 찾기
	public User findId(User user) {
		return sql.selectOne("User.findId", user);
	}
	
	// 비밀번호 찾기
	public User findPwd(User user) {
		return sql.selectOne("User.findPwd", user);
	}
	
	// 이메일 찾기
	public User findEmail(User user) {
		return sql.selectOne("User.findEmail", user);
	}
	
	// 임시 비밀번호 변경
	public void tempPwdChange(User user) {
		sql.update("User.tempPwdChange", user);
	}
	
	//회원 정보 조회
	public User userSelect(String userId) {
		return sql.selectOne("User.userSelect", userId);
	}
	
	//회원 정보 수정
	public int userUpdate(User user) {
		return sql.update("User.userUpdate", user);
	}
	
	// 비밀번호 확인
	public String pwdCheck(String userId) {
		log.info("UserRepository => pwdCheck");
		
		return sql.selectOne("User.pwdCheck", userId);
	}
	
	// 비밀번호 변경
	public void pwdUpdate(String userId, String userPwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("userPwd", userPwd);
		
		sql.update("User.pwdUpdate", map);
	}
	
	// 탈퇴
	public void updateStatus(User user) {
		sql.update("User.updateStatus", user);
	}
	
	// ============================================================= //
	
	//찜목록 리스트 조회
	public List<InterestItem> interestItemSelect(String userId) {
		return sql.selectList("User.interestItemSelect",userId);
	}
	
	//찜목록내 존재 여부 조회
	public int interestItemFindSelect(InterestItem interestItem) {
		return sql.selectOne("User.interestItemFindSelect", interestItem);
	}
	
	// 찜목록 추가
    public int interestItemInsert(InterestItem interestItem) {
        return sql.insert("User.interestItemInsert", interestItem);
    }
	
	//찜목록 삭제
    public int interestItemDelete(InterestItem interestItem) {
    	return sql.delete("User.interestItemDelete",interestItem);
    } 
    
}