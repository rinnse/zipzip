package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.demo.dto.InterestItem;
import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder userPasswordEncoder;
	
	// 회원가입
	public void insert(User user) {
		log.info("UserService => insert");
      
		User data = new User();
      
		data.setUserId(user.getUserId());
		data.setUserPwd(userPasswordEncoder.encode(user.getUserPwd()));
		data.setUserName(user.getUserName());
		data.setUserPhone(user.getUserPhone());
		data.setUserEmail(user.getUserEmail());
		data.setStatus("Y");
		data.setEmailAuth("Y");
		data.setRole("ROLE_USER");
      
		log.info("data : " + data);
      
		userRepository.insert(data);
	}
	
	// 로그인
	public User findById(String userId) {
		
		return userRepository.findById(userId);
	}
   
	// 유효성검사
	public Map<String, String> validateHandler(Errors errors) {
		Map<String, String> validateResult = new HashMap<String, String>();
      
		for (FieldError error : errors.getFieldErrors()) {
			String validKeyName = "valid_" + error.getField();
			validateResult.put(validKeyName, error.getDefaultMessage());
		}
      
		return validateResult;
	}
   
	// 아이디 중복 검사
	public int idCheck(String userId) {
		log.info("UserService => idCheck");
		
		return userRepository.idCheck(userId);
	}
	
	// 아이디 찾기
	public User findId(User user) {
		return userRepository.findId(user);
	}
	
	// 비밀번호 찾기
	public User findPwd(User user) {
		return userRepository.findPwd(user);
	}
	
	// 회원 정보 조회
	public User userSelect(String userId) {
		return userRepository.userSelect(userId);
	}
	
	// 회원 정보 수정
	public int userUpdate(User user) {
		return userRepository.userUpdate(user);
	}
	
	// 비밀번호 확인
	public String pwdCheck(String userId) {
		return userRepository.pwdCheck(userId);
	}
	
	// 비밀번호 변경
	public void pwdUpdate(String userId, String userPwd) {
		log.info("pwdUpdate");
		userRepository.pwdUpdate(userId, userPwd);
	}
	
	// 탈퇴
	public void updateStatus(User user) {
		userRepository.updateStatus(user);
	}
	
	// ============================================================= //
	
	//찜목록 리스트 조회
	public List<InterestItem> interestItemSelect(String userId) {
		return userRepository.interestItemSelect(userId);		
	}
	
	//찜목록내 존재 여부 조회
	public int interestItemFindSelect(InterestItem interestItem) {
		return userRepository.interestItemFindSelect(interestItem);
	}
	
	//찜목록 추가
	public int interestItemInsert(InterestItem interestItem) {
		return userRepository.interestItemInsert(interestItem);
	}
	
	
	//찜목록 삭제
	public int interestItemDelete(InterestItem interestItem) {
		return userRepository.interestItemDelete(interestItem);
	}
	
	
}