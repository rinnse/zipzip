package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Sense;
import com.example.demo.repository.SenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SenseService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final SenseRepository senseRepository;
	
	public void insert(Sense sense) {
		senseRepository.insert(sense);
	}
	
	public List<Sense> selectList(Sense sense) {
		return senseRepository.selseList(sense);
	}
	
	public List<Sense> selectAll(Sense sense) {
		return senseRepository.selectAll(sense);
	}
	
	public long listCnt(Sense sense) {
		return senseRepository.listCnt(sense);
	}
	
	public void updateHit(long senseNum) {
		senseRepository.updateHit(senseNum);
	}
	
	public Sense selectOne(long senseNum) {
		return senseRepository.selectOne(senseNum);
	}
	
	public void update(Sense sense) {
		senseRepository.update(sense);
	}
	
	public void delete(long senseNum) {
		senseRepository.delete(senseNum);
	}
}
