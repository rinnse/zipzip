package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Suggest;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SuggestRepository {
	
	private final SqlSessionTemplate sql;
	
	public Suggest insert(Suggest suggest) {
		sql.insert("Suggest.insert", suggest);
		return suggest;
	}
	
	public List<Suggest> selectAll(Suggest suggest) {
		return sql.selectList("Suggest.selectAll", suggest);
	}
	
	public long listCnt(Suggest suggest) {
		return sql.selectOne("Suggest.listCnt", suggest);
	}
	
	public void updateHit(long suggestNum) {
		sql.update("Suggest.updateHit", suggestNum);
	}
	
	public Suggest selectOne(long suggestNum) {
		return sql.selectOne("Suggest.selectOne", suggestNum);
	}
	
	public void update(Suggest suggest) {
		sql.update("Suggest.update", suggest);
	}
	
	public void delete(long suggestNum) {
		sql.delete("Suggest.delete", suggestNum);
	}
	
	//24.05.16 나의글 건의사항 리스트 관련 소스 추가
	   public List<Suggest> myWriteSelectAll(Suggest suggest) {
		   return sql.selectList("Suggest.myWriteSelectAll",suggest);
	   }
	   
	   //24.05.21 나의글 건의사항 리스트 개수 관련 소스 추가
	   public long myListCnt(Suggest suggest) {
			return sql.selectOne("Suggest.myListCnt", suggest);
		}
}
