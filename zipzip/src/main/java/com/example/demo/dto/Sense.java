package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sense {
	private long senseNum;
	private String userId;
	private String senseTitle;
	private String senseContent;
	private String regDate;
	private long senseHit;
	
	private String searchValue;
	
	private long startRow;
	private long endRow;
}
