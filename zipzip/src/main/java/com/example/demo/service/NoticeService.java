package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Notice;
import com.example.demo.dto.NoticeFile;
import com.example.demo.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final NoticeRepository noticeRepository;
	
//	public void insert(Notice notice) {
//		noticeRepository.insert(notice);
//	}
	
	// 단일 파일
//	public void insert(Notice notice) throws IOException {
//		if (notice.getNoticeFile().isEmpty()) {
//			notice.setFileAtt(0);
//			noticeRepository.insert(notice);
//		}
//		
//		else {
//			notice.setFileAtt(1);
//			Notice insert = noticeRepository.insert(notice);
//			
//			int idx = 0;
//			
//			MultipartFile file = notice.getNoticeFile();
//			
//			long noticeNum = notice.getNoticeNum();
//			System.out.println("noticeNum : " + noticeNum);
//			
//			idx++;
//			System.out.println("fileIdx : " + idx);
//			
//			String orgName = file.getOriginalFilename();
//			System.out.println("orgName : " + orgName);
//			
//			String fileName = noticeNum + "_" + idx + ".jpg";
//			System.out.println("fileName : " + fileName);
//			
//			NoticeFile noticeFile = new NoticeFile();
//			noticeFile.setNoticeNum(insert.getNoticeNum());
//			noticeFile.setFileIdx(idx);
//			noticeFile.setFileOrg(orgName);
//			noticeFile.setFileName(fileName);
//			
//			
//			String savePath = "C:/project/final/test/src/main/resources/static/resources/upload/board/notice/" + fileName;
//			//String savePath = "C:/project/final/zipzip/src/main/resources/static/resources/upload/board/notice/" + fileName;
//			file.transferTo(new File(savePath));
//			
//			noticeRepository.insertFile(noticeFile);
//		}
//	}
	
	// 다중 파일
	public void insert(Notice notice) throws IOException {
		if (notice.getNoticeFile().get(0).isEmpty()) {
			notice.setFileAtt(0);
			noticeRepository.insert(notice);
		}
		
		else {
			notice.setFileAtt(1);
			Notice insert = noticeRepository.insert(notice);
			
			int idx = 0;
			
			for (MultipartFile file : notice.getNoticeFile()) {
				long noticeNum = notice.getNoticeNum();
				System.out.println("noticeNum : " + noticeNum);
				
				idx++;
				System.out.println("fileIdx : " + idx);
				
				String orgName = file.getOriginalFilename();
				System.out.println("orgName : " + orgName);
				
				String fileName = noticeNum + "_" + idx + ".jpg";
				System.out.println("fileName : " + fileName);
				
				NoticeFile noticeFile = new NoticeFile();
				noticeFile.setNoticeNum(insert.getNoticeNum());
				noticeFile.setFileIdx(idx);
				noticeFile.setFileOrg(orgName);
				noticeFile.setFileName(fileName);
				
				
				String savePath = "C:/project/final/zipzip/src/main/resources/static/resources/upload/board/notice/" + fileName;
				file.transferTo(new File(savePath));
				
				noticeRepository.insertFile(noticeFile);
			}
		}
	}
	
	public List<Notice> selectList(Notice notice) {
		return noticeRepository.selectList(notice);
	}

	public List<Notice> selectAll(Notice notice) {
		return noticeRepository.selectAll(notice);
	}
	
	public long listCnt(Notice notice) {
		return noticeRepository.listCnt(notice);
	}

	public void updateHit(long noticeNum) {
		noticeRepository.updateHit(noticeNum);
	}

	public Notice selectOne(long noticeNum) {
		return noticeRepository.selectOne(noticeNum);
	}

	public void update(Notice notice) {
		noticeRepository.update(notice);
	}

	public void delete(long noticeNum) {
		noticeRepository.delete(noticeNum);
	}

	// 단일 파일
//	public NoticeFile selectFile(long noticeNum) {
//		return noticeRepository.selectFile(noticeNum);
//	}
	
	// 다중 파일
	public List<NoticeFile> selectFile(long noticeNum) {
		return noticeRepository.selectFile(noticeNum);
	}
}
