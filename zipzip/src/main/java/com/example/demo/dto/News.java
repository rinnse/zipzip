package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class News {
	private String image;		// 이미지
	private String subject;		// 제목
	private String content;		// 내용
	private String url;			// URL
	private String info;		// 신문사
}

