package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Event;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventRepository {
	
	private final SqlSessionTemplate sql;
	
	public Event insert(Event event) {
		sql.insert("Event.insert", event);
		return event;
	}
	
	public List<Event> selectList(Event event) {
		return sql.selectList("Event.selectList", event);
	}
	
	public List<Event> selectAll(Event event) {
		return sql.selectList("Event.selectAll", event);
	}
	
	public long listCnt(Event event) {
		return sql.selectOne("Event.listCnt", event);
	}
	
	public void updateHit(long eventNum) {
		sql.update("Event.updateHit", eventNum);
	}
	
	public Event selectOne(long eventNum) {
		return sql.selectOne("Event.selectOne", eventNum);
	}
	
	public void update(Event event) {
		sql.update("Event.update", event);
	}
	
	public void delete(long eventNum) {
		sql.delete("Event.delete", eventNum);
	}
}
