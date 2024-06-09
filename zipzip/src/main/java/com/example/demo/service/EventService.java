package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Event;
import com.example.demo.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EventRepository eventRepository;
	
	public void insert(Event event, MultipartFile file) throws IOException {
		
		if (!file.isEmpty()) {
			String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\resources\\upload\\event";
			
			UUID uuid = UUID.randomUUID();
			
			String fileOrg = file.getOriginalFilename();
			String fileName = uuid + ".jpg";
			
			File saveFile = new File(savePath, fileName);
			
			file.transferTo(saveFile);
			
			event.setFileOrg(fileOrg);
			event.setFileName(fileName);
		}
		
		eventRepository.insert(event);
	}
	
	public List<Event> selectList(Event event) {
		return eventRepository.selectList(event);
	}
	
	public List<Event> selectAll(Event event) {
		return eventRepository.selectAll(event);
	}
	
	public long listCnt(Event event) {
		return eventRepository.listCnt(event);
	}
	
	public void updateHit(long eventNum) {
		eventRepository.updateHit(eventNum);
	}
	
	public Event selectOne(long eventNum) {
		return eventRepository.selectOne(eventNum);
	}
	
	public void update(Event event, MultipartFile file) throws IOException {
		
		Event e = eventRepository.selectOne(event.getEventNum());
		
		String oldFile = e.getFileName();
		System.out.println("oldFile : " + oldFile);
		
		if (!file.isEmpty()) {
			
			String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\resources\\upload\\event";
			
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
			
			event.setFileOrg(fileOrg);
			event.setFileName(fileName);
			System.out.println("newFile : " + fileName);
		}
		
		eventRepository.update(event);
	}
	
	public void delete(long eventNum) {
		
		String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\resources\\upload\\event";
		
		Event event = eventRepository.selectOne(eventNum);
		
		String fileName = event.getFileName();
		
		File file = new File(savePath + "\\" + fileName);
		
		if (file.exists()) {
			file.delete();
		}
		
		eventRepository.delete(eventNum);
	}
}
