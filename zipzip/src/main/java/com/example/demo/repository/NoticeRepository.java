package com.example.demo.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.Notice;
import com.example.demo.dto.NoticeFile;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

	private final SqlSessionTemplate sql;
	
//	public void insert(Notice notice) {
//		sql.insert("Notice.insert", notice);
//	}
	
	public Notice insert(Notice notice) {
		sql.insert("Notice.insert", notice);
		return notice;
	}
	
	public List<Notice> selectList(Notice notice) {
		return sql.selectList("Notice.selectList", notice);
	}

	public List<Notice> selectAll(Notice notice) {
		return sql.selectList("Notice.selectAll", notice);
	}
	
	public long listCnt(Notice notice) {
		return sql.selectOne("Notice.listCnt", notice);
	}

	public void updateHit(long noticeNum) {
		sql.update("Notice.updateHit", noticeNum);
	}

	public Notice selectOne(long noticeNum) {
		return sql.selectOne("Notice.selectOne", noticeNum);
	}

	public Notice update(Notice notice) {
		sql.update("Notice.update", notice);
		return notice;
	}

	public void delete(long noticeNum) {
		sql.delete("Notice.delete", noticeNum);
	}

	public void insertFile(NoticeFile noticeFile) {
		sql.insert("Notice.insertFile", noticeFile);
	}

	// 단일 파일
//	public NoticeFile selectFile(long noticeNum) {
//		return sql.selectOne("Notice.selectFile", noticeNum);
//	}
	
	// 다중 파일
	public List<NoticeFile> selectFile(long noticeNum) {
		return sql.selectList("Notice.selectFile", noticeNum);
	}
}
