package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemDetail;
import com.example.demo.dto.ItemFile;
import com.example.demo.dto.ItemOption;
import com.example.demo.repository.AgentMyRepository;
import com.example.demo.repository.AgentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final AgentRepository agentRepository;
	private final BCryptPasswordEncoder agentPasswordEncoder;
	
	// 오류 로그 확인용
	private static Logger logger = LoggerFactory.getLogger(AgentService.class);
	
	@Autowired
	private AgentMyRepository agentMyRepository;
	
	// 회원가입
	public void insert(Agent agent) {
		log.info("AgentService => insert");
		
		Agent data = new Agent();
		
		data.setAgentId(agent.getAgentId());
		data.setAgentPwd(agentPasswordEncoder.encode(agent.getAgentPwd()));
		data.setAgentName(agent.getAgentName());
		data.setAgentOfficeName(agent.getAgentOfficeName());
		data.setAgnetPhone(agent.getAgnetPhone());
		data.setAgentEmail(agent.getAgentEmail());
		data.setAgentIntro(agent.getAgentIntro());
		data.setAgentAddress(agent.getAgentAddress());
		data.setAgentAddressDetail(agent.getAgentAddressDetail());
		data.setAgentRight("Y");
		data.setRole("ROLE_AGENT");
		data.setStatus("Y");
		
		log.info("data : " + data);
		
		agentRepository.insert(data);
	}
	
	// 로그인
	public Agent findById(String agentId) {
		return agentRepository.findById(agentId);
	}
	
	// 유효성검사
	public Map<String, String> validateHandler(Errors errors) {
		Map<String, String> validateResult = new HashMap<String, String>();
		
		for (FieldError error : errors.getFieldErrors()) {
			String validKetName = "valid_" + error.getField();
			validateResult.put(validKetName, error.getDefaultMessage());
		}
		
		return validateResult;
	}
	
	
	// 아이디 확인
	public int idCheckAg(String agentId) {
		log.info("UserService => idCheck");
		return agentRepository.idCheckAg(agentId);
	}
	
	// 중개인 정보 조회
	public Agent agentSelect(String agentId) {
		return agentRepository.agentSelect(agentId);
	}
	
	// 중개인 정보 수정
	public void agentUpdate(Agent agent) {
		agentRepository.agentUpdate(agent);
	}
	
	// 프로필 수정
	public void updateProfile(Agent agent, MultipartFile file) throws IOException {
		
		Agent a = agentRepository.agentSelect(agent.getAgentId());
		
		String oldFile = a.getFileName();
		System.out.println("oldFile : " + oldFile);
		
		if (!file.isEmpty()) {
			
			String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\resources\\upload\\agent";
			
			if (oldFile != null) {
				
				File old = new File(savePath + "\\" + oldFile);
				System.out.println("old : " + old);
				old.delete();
				System.out.println("삭제");
			}
			
			UUID uuid = UUID.randomUUID();
			
			String fileOrg = file.getOriginalFilename();
			String fileName = uuid + ".jpg";
			
			File saveFile = new File(savePath, fileName);
			
			file.transferTo(saveFile);
			
			agent.setFileOrg(fileOrg);
			agent.setFileName(fileName);
			System.out.println("newFile : " + fileName);
		}
		
		agentRepository.updateProfile(agent);
	}
	
	// 비밀번호 확인
	public String pwdCheck(String agentId) {
		return agentRepository.pwdCheck(agentId);
	}
	
	// 비밀번호 변경
	public void pwdUpdate(String agentId, String agentPwd) {
		log.info("pwdUpdate");
		agentRepository.pwdUpdate(agentId, agentPwd);
	}
	
	// 탈퇴
	public void updateStatus(Agent agent) {
		agentRepository.updateStatus(agent);
	}
	
	// 결제 확인
	public void updateRight(Agent agent) {
		agentRepository.updateRight(agent);
	}
	
	// ============================================================= //
	
	// 나의 리뷰 평균 조회 
	public double avgReviewScore(String agentId) {
		double count = 0;
			
		try {
			count = agentMyRepository.avgReviewScore(agentId);
		} catch(Exception e) {
			logger.error("[AgentService] avgReviewScore Exception", e);
		}
			
		return count;
	}
	
	//============================================================= YH
	
	// 매물등록
	public int itemInsert(Item item) {
		int cnt = 0;
		
		try {
			cnt = agentMyRepository.itemInsert(item);
		}catch(Exception e) {
			log.debug("[Agent Service Error] itemInsert -- ", e);
		}
		
		return cnt;
	}
	
	// 매물등록 - 상세
	public int itemDetailInsert(ItemDetail itemDetail) {
		int cnt = 0;
		
		try {
			cnt = agentMyRepository.itemDetailInsert(itemDetail);
		}catch(Exception e) {
			log.debug("[Agent Service Error] itemDetailInsert -- ", e);
		}
		
		return cnt;
	}
	
	// 매물등록 - 옵션
	public int itemOptionInsert(ItemOption itemOption) {
		int cnt = 0;
		
		try {
			cnt = agentMyRepository.itemOptionInsert(itemOption);
		}catch(Exception e) {
			log.debug("[Agent Service Error] itemOptionInsert -- ", e);
		}
		
		return cnt;
	}
	
	// 매물등록,수정 - 파일insert
	public int itemFileInsert(ItemFile itemFile) {
		int cnt = 0;
		
		try {
			cnt = agentMyRepository.itemFileInsert(itemFile);
		}catch(Exception e) {
			log.debug("[Agent Service Error] itemFileInsert -- ", e);
		}
		
		return cnt;
	}
	
	
	// 등록 매물 list
	public List<Item> myItemRegList(Item item){
		List<Item> list = null;
		
		try {
			list = agentMyRepository.myItemRegList(item);
		}catch(Exception e) {
			log.debug("[Agent Service Error] myItemRegList -- ", e);
		}
		
		return list;
	}
	
	
	//매물 삭제
	@Transactional
	public int itemDelete(long itemNum) {
		int cnt = 0;

		if(agentMyRepository.deleteItemFile(itemNum) > 0) {
			if(agentMyRepository.deleteItemOption(itemNum) > 0) {
				if(agentMyRepository.deleteItemDetail(itemNum) > 0) {
					if(agentMyRepository.deleteItem(itemNum) > 0) {
						cnt += 1;
					}else {
						log.debug("[Agent Service Error] itemDelete deleteItem Error ");
					}
				}else {
					log.debug("[Agent Service Error] itemDelete deleteItemDetail Error ");
				}
			}else {
				log.debug("[Agent Service Error] itemDelete deleteItemOption Error ");
			}
		}else {
			log.debug("[Agent Service Error] itemDelete deleteItemFile Error ");
		}
		
		return cnt;
	}
	
	
	//매물 단건 조회
	public Item itemSelect(long itemNum) {
		Item item = null;
		
		try {
			item = agentMyRepository.itemSelect(itemNum);
		}catch(Exception e) {
			log.debug("[Agent Service Error] itemSelect -- ", e);
		}
		
		return item;
	}
	
	//매물 수정
	public int itemUpdate(Item item, ItemDetail itemDetail, ItemOption itemOption) {
		int cnt = 0;
		
		if(item != null) {
			if(agentMyRepository.itemUpdate(item) > 0) {
				if(agentMyRepository.itemDetailUpdate(itemDetail) > 0) {
					if(agentMyRepository.itemOptionUpdate(itemOption) > 0) {
						cnt = 1;
					}else {
						cnt = -3;
					}
				}else {
					cnt = -2;
				}
			}else {
				cnt = -1;
			}
		}
		
		return cnt;
	}
	
	// 매물 수정 시 fileDelete
	public int itemFileDelete(long itemNum) {
		int cnt = 0;
		
		try {
			cnt = agentMyRepository.deleteItemFile(itemNum);
		}catch(Exception e) {
			log.debug("[Agent Service Error] itemFileDelete -- ", e);
		}
		
		return cnt;
	}

	//============================================================= YH 끝
		
		
	// 나의 총 리뷰 리스트
	public List<AgentReview> myReviewList(AgentReview agentReview){
		List<AgentReview> myReviewList = null;
			
		try {
			myReviewList = agentMyRepository.myReviewList(agentReview);
		} catch(Exception e) {
			logger.error("[AgentService] myReviewList Exception", e);
		}
			
		return myReviewList;
	}
		
	// 나의 총 리뷰 수
	public int myReviewCount(String agentId) {
		int count = 0;
			
		try {
			count = agentMyRepository.myReviewCount(agentId);
		} catch(Exception e) {
			logger.error("[AgentService] myReviewCount Exception", e);
		}
			
		return count;
	}
}
