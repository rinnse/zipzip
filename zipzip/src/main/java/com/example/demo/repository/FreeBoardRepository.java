package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Community;
import com.example.demo.dto.CommunityComment;

@Mapper
public interface FreeBoardRepository {
   
   //게시물 list
   public List<Community> freeBoardList(Community commu);
   
   //게시물 총 갯수 totalCnt
   public long totalCnt(Community commu);
   
   //게시물 등록 insert
   public int freeBoardInsert(Community commu);
   
   //게시글 상세 view select
   public Community freeBoardDetail(long communityNum);
   
   //조회수 증가 update
   public int freeBoardReadCnt(long communityNum);
   
   //댓글 list cnt (댓글 갯수 조회)
   public long commentListCnt(long communityNum);
   
   //댓글 list select
   public List<CommunityComment> commentList(long communityNum);
   
   //댓글 1건 select
   public CommunityComment commentSelect(long commentNum);
   
   //댓글 등록 insert
   public int CommentInsert(CommunityComment comment);
   
   //대댓글 등록 insert
   public int reCommentInsert(CommunityComment comment);
   
   //댓글 삭제 update
   public int commentDeleteUpdate(long commentNum);
   
   //게시글 추천 insert
   public int likeInsert(Community commu);
   
   //게시글 추천 delete
   public int likeDelete(Community commu);
   
   //게시글 별 총 추천수
   public long likeTotalCnt(long communityNum);
   
   //게시글 추천 여부
   public long likeSelectCnt(Community commu);
   
   //게시글 삭제(추천,댓글,게시글)
   public int communityDeleteLike(long communityNum);
   public int communityDeleteComment(long communityNum);
   public int communityDelete(long communityNum);
   
   //게시글 수정
   public int communityUpdate(Community commu);
   
   //24.05.14 나의글 관련 소스 추가
   //나의 글 List 조회 
   public List<Community> myWriteBoardList(Community commu);
	
   //나의 글 List 총 갯수 totalCnt
   public long myWriteTotalCnt(Community commu);
}