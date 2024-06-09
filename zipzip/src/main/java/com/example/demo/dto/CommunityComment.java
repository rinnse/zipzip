package com.example.demo.dto;

import lombok.Data;

@Data
public class CommunityComment {
	
	private long communityNum;		//커뮤니티 번호
	private long commentNum;		//커뮤니티 댓글 번호
	private String userId;			//유저 아이디
	private String agentId;			//중개인 아이디
	private String commentContent;	//커뮤니티 댓글 내용
	private long commentGroup;		//커뮤니티 댓글 그룹
	private long commentDepth;		//커뮤니티 댓글 뎁스
	private long likeCnt;			//좋아요수
	private String regDate;			//등록일
	private long isDeleted;
	
	
}
