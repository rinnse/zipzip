package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Sense;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SenseRepository {
	
	private final SqlSessionTemplate sql;
	
	public Sense insert(Sense sense) {
		sql.insert("Sense.insert", sense);
		return sense;
	}
	
	public List<Sense> selseList(Sense sense) {
		return sql.selectList("Sense.selectList", sense);
	}
	
	public List<Sense> selectAll(Sense sense) {
		return sql.selectList("Sense.selectAll", sense);
	}
	
	public long listCnt(Sense sense) {
		return sql.selectOne("Sense.listCnt", sense);
	}
	
	public void updateHit(long senseNum) {
		sql.update("Sense.updateHit", senseNum);
	}
	
	public Sense selectOne(long senseNum) {
		return sql.selectOne("Sense.selectOne", senseNum);
	}
	
	public void update(Sense sense) {
		sql.update("Sense.update", sense);
	}
	
	public void delete(long senseNum) {
		sql.delete("Sense.delete", senseNum);
	}
}
