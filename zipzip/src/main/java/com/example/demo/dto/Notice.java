package com.example.demo.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	private long noticeNum;
	private String userId;
	private String noticeTitle;
	private String noticeContent;
	private String regDate;
	private long noticeHit;
	private int fileAtt;
	
	// 단일 파일
//	private MultipartFile noticeFile;
	
	// 다중 파일
	private List<MultipartFile> noticeFile;
	
	private String searchValue;
	
	private long startRow;
	private long endRow;
}
