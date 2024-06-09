package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFile {
	private long noticeNum;
	private long fileNum;
	private long fileIdx;
	private String fileOrg;
	private String fileName;
	private String fileDate;
}
