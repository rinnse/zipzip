package com.example.demo.dto;

import lombok.Data;

@Data
public class Report {
	
	private long itemNum;
	private String userId;
	private String reportContent;
	private String reportType;
	private long reportNum;
	private String regDate;
	private String status;
	
	private long startRnum;
	private long endRnum;

}
