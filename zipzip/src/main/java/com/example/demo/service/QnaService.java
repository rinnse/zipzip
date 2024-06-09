package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.Qna;
import com.example.demo.repository.QnaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final QnaRepository qnaRepository;
	
	public void insert(Qna qna) {
		qnaRepository.insert(qna);
	}
	
	public List<Qna> selectAll(Qna qna) {
		return qnaRepository.selectAll(qna);
	}
	
	public long listCnt(Qna qna) {
		return qnaRepository.listCnt(qna);
	}
	
	public void updateHit(long qnaNum) {
		qnaRepository.updateHit(qnaNum);
	}
	
	public Qna selectOne(long qnaNum) {
		return qnaRepository.selectOne(qnaNum);
	}
	
	public void update(Qna qna) {
		qnaRepository.update(qna);
	}
	
	public void delete(long qnaNum) {
		qnaRepository.delete(qnaNum);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void insertReply(Qna qna) {
		qnaRepository.groupUpdate(qna);
		qnaRepository.insertReply(qna);
	}
	
	//24.05.22 나의 문의 Qna 리스트 조회 소스 추가 
	public List<Qna> myQnaSelectAll(Qna qna) {
		return qnaRepository.myQnaSelectAll(qna);
	}
	
	//24.05.22 나의 문의 Qna 리스트 개수 조회 소스 추가
	public long myQnaListCnt(Qna qna) {
		return qnaRepository.myQnaListCnt(qna);
	}
}
