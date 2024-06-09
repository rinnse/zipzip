package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Qna;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QnaRepository {
	
	private final SqlSessionTemplate sql;
	
	public Qna insert(Qna qna) {
		sql.insert("Qna.insert", qna);
		return qna;
	}
	
	public List<Qna> selectAll(Qna qna) {
		return sql.selectList("Qna.selectAll", qna);
	}
	
	public long listCnt(Qna qna) {
		return sql.selectOne("Qna.listCnt", qna);
	}
	
	public void updateHit(long qnaNum) {
		sql.update("Qna.updateHit", qnaNum);
	}
	
	public Qna selectOne(long qnaNum) {
		return sql.selectOne("Qna.selectOne", qnaNum);
	}
	
	public void update(Qna qna) {
		sql.update("Qna.update", qna);
	}
	
	public void delete(long qnaNum) {
		sql.delete("Qna.delete", qnaNum);
	}
	
	public void groupUpdate(Qna qna) {
		sql.update("Qna.groupUpdate", qna);
	}

	public Qna insertReply(Qna qna) {
		sql.insert("Qna.insertReply", qna);
		return qna;
	}
	
	//24.05.22 나의 문의 Qna 리스트 조회 소스 추가
	public List<Qna> myQnaSelectAll(Qna qna) {
		return sql.selectList("Qna.myQnaSelectAll", qna);
	}
	
	//24.05.22 나의 문의 Qna 리스트 개수 조회 소스 추가
	public long myQnaListCnt(Qna qna) {
		return sql.selectOne("Qna.myQnaListCnt", qna);
	}
}
