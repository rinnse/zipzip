package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggest {
	private long suggestNum;
	private String userId;
	private String suggestTitle;
	private String suggestContent;
	private String regDate;
	private long suggestHit;
	
	private String searchValue;
	
	private long startRow;
	private long endRow;
}
