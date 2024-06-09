package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Qna {
	private long qnaNum;
	private String userId;
	private String qnaTitle;
	private String qnaContent;
	private String regDate;
	private long qnaHit;
	private long qnaGroup;
	private long qnaOrder;
	private long qnaIndent;
	private long qnaParent;
	
	private String searchValue;
	
	private long startRow;
	private long endRow;
}
