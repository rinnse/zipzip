package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	private long eventNum;
	private String userId;
	private String eventTitle;
	private String eventContent;
	private String fileOrg;
	private String fileName;
	private String regDate;
	private long eventHit;
	
	private String searchValue;
	
	private long startRow;
	private long endRow;
}
