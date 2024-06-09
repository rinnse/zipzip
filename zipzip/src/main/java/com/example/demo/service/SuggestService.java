package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Suggest;
import com.example.demo.repository.SuggestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuggestService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final SuggestRepository suggestRepository;
	
	public void insert(Suggest suggest) {
		suggestRepository.insert(suggest);
	}
	
	public List<Suggest> selectAll(Suggest suggest) {
		return suggestRepository.selectAll(suggest);
	}
	
	public long listCnt(Suggest suggest) {
		return suggestRepository.listCnt(suggest);
	}
	
	public void updateHit(long suggestNum) {
		suggestRepository.updateHit(suggestNum);
	}
	
	public Suggest selectOne(long suggestNum) {
		return suggestRepository.selectOne(suggestNum);
	}
	
	public void update(Suggest suggest) {
		suggestRepository.update(suggest);
	}
	
	public void delete(long suggestNum) {
		suggestRepository.delete(suggestNum);
	}
	
	//24.05.16 나의글 건의사항 리스트 관련 소스 추가
	   public List<Suggest> myWriteSelectAll(Suggest suggest) {
		    return suggestRepository.myWriteSelectAll(suggest);
	   }
	   
	  //24.05.21 나의글 건의사항 리스트 개수 관련 소스 추가
	   public long myListCnt(Suggest suggest) {
			return suggestRepository.myListCnt(suggest);
	}
}
