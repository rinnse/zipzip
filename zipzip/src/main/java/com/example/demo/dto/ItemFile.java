package com.example.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long itemNum;		//매물 등록 번호		
	private long fileNum;		//매물 파일 번호	
	private long fileIdx;		//파일 순서
	private String fileOrg;		//파일 원본명	
	private String fileName;	//파일명	
	private String fileDate;	//업로드일자

}
