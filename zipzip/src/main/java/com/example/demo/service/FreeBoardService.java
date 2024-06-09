package com.example.demo.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.Community;
import com.example.demo.dto.CommunityComment;
import com.example.demo.repository.FreeBoardRepository;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FreeBoardService {
   
   @Autowired
   FreeBoardRepository freeBoardRepository;
   
   private static final long LIST_CNT = 50;
   private static final long PAGE_CNT = 2;
   
   private static Logger logger = LoggerFactory.getLogger(FreeBoardService.class);
   
   
   // 리스트 
   public List<Community> freeBoardList(long curPage, Community commu) {
      
      List<Community> list = null;
      long totalCnt = listTotalCnt(commu);
      
      try {
           totalCnt = listTotalCnt(commu);

           if (totalCnt > 0) {
               Paging paging = pagingService(curPage, commu);
               commu.setStartRnum(paging.getStartRow());
               commu.setEndRnum(paging.getEndRow());

               list = freeBoardRepository.freeBoardList(commu);
           }
      }catch(Exception e) {
         log.debug("자유게시판 Service Error freeBoardList -- ", e);
         list = Collections.emptyList();
      }
      
      log.info("자유게시판 서비스 SearchType / searchValue / totalCnt -- " + commu.getSearchType() + " / " + commu.getSearchValue() + " / " + totalCnt);
      
      return list;
   }
   
   //페이징 (+ totalCnt)
   public Paging pagingService(long curPage, Community commu) {
      Paging paging = null;
      long totalCnt = 0;
      
      try {
         totalCnt = freeBoardRepository.totalCnt(commu);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error paging -- ", e);
      }
      
      if(totalCnt > 0) {
         paging = new Paging("/board/freeBoard", totalCnt, LIST_CNT, PAGE_CNT, curPage, "curPage");   
      }
      
      return paging;
   }
   
   //totalCnt 
   public long listTotalCnt(Community commu) {
      long totalCnt = 0;
      
      try {
         totalCnt = freeBoardRepository.totalCnt(commu);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error listTotalCnt -- ", e);
      }      
      
      return totalCnt;
   }
   
   
   //게시물 등록 insert
   public int communityInsert(Community commu) {
      int result = 0;
      
      try {
         result = freeBoardRepository.freeBoardInsert(commu);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error communityInsert -- ", e);
      }
      
      return result;
   }
   
   //게시판 상세보기
   public Community freeBoardDetail(long communityNum) {
      Community commu = null;
      
      try {
         commu = freeBoardRepository.freeBoardDetail(communityNum);
         
         if(commu != null) {
            freeBoardRepository.freeBoardReadCnt(communityNum);
            commu = freeBoardRepository.freeBoardDetail(communityNum);
         }
      }catch(Exception e) {
         log.debug("자유게시판 Service Error freeBoardDetail -- ", e);
      }
      
      return commu;
   }
   
   //자유게시글 한건 조회
   public Community communitySelect(long communityNum) {
      Community commu = null;
      
      try {
         commu = freeBoardRepository.freeBoardDetail(communityNum);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error communitySelect -- ", e);
      }
      
      return commu;
   }
   
   //댓글 갯수 cnt
   public long commentListCnt(long communityNum) {
      long cnt = 0;
      
      if(communityNum > 0) {
         try {
            cnt = freeBoardRepository.commentListCnt(communityNum);
         }catch(Exception e) {
            log.debug("자유게시판 Service Error commentListCnt -- ", e);
         }
      }
      
      return cnt;
   }
   
   //댓글 list
   public List<CommunityComment> commentList(long communityNum){
      List<CommunityComment> list = null;
      
      try {
         list = freeBoardRepository.commentList(communityNum);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error commentList -- ", e);
      }
      
      return list;
   }
   
   //댓글 한건 조회
   public CommunityComment commentSelect(long commentNum) {
      CommunityComment comment = null;
      
      try {
         comment = freeBoardRepository.commentSelect(commentNum);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error commentSelect -- ", e);
      }
      
      return comment;
   }
   
   //댓글 등록 insert
   public int CommentInsert(CommunityComment comment) {
      int cnt = 0;
      
      if(comment.getCommentGroup() == 0) {
         try {
            comment.setCommentDepth(0);
            cnt = freeBoardRepository.CommentInsert(comment);
         }catch(Exception e) {
            log.debug("자유게시판 Service Error CommentInsert -- ", e);
         }
      }else {
         try {
            comment.setCommentDepth(1);
            cnt = freeBoardRepository.reCommentInsert(comment);
         }catch(Exception e) {
            log.debug("자유게시판 Service Error reCommentInsert -- ", e);
         }
      }

      log.info("cnt -- " + cnt);
      
      return cnt;
   }
   
   //댓글 삭제 update
   public int commentDeleteUpdate(long commentNum) {
      int cnt = 0;
      
      try {
         cnt = freeBoardRepository.commentDeleteUpdate(commentNum);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error commentDeleteUpdate -- ", e);
      }
      
      return cnt;
   }
   
   
   //게시글 추천 insert
   public int likeInsert(Community commu) {
      int cnt = 0;
      
      try {
         cnt = freeBoardRepository.likeInsert(commu);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error likeInsert -- ", e);
      }
      
      return cnt;
   }
   
   //게시글 추천 delete
   public int likeDelete(Community commu) {
      int cnt = 0;
      
      try {
         cnt = freeBoardRepository.likeDelete(commu);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error likeDelete -- ", e);
      }
      
      return cnt;
   }
   
   //게시글 별 총 추천수
   public long likeTotalCnt(long communityNum) {
      long cnt = 0;
      
      try {
         cnt = freeBoardRepository.likeTotalCnt(communityNum);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error likeSelectCnt -- ", e);
      }
      
      return cnt;      
   }
   
   //게시글 추천 여부 count
   public long likeSelectCnt(Community commu) {
      long cnt = 0;
      
      try {
         cnt = freeBoardRepository.likeSelectCnt(commu);
      }catch(Exception e) {
         log.debug("자유게시판 Service Error likeSelectCnt -- ", e);
      }
      
      return cnt;
   }
   
   //게시글 삭제
   @Transactional
   public int communityDelete(long communityNum) {
      int cnt = 0;
      
      if(freeBoardRepository.likeTotalCnt(communityNum) > 0) {
         cnt = freeBoardRepository.communityDeleteLike(communityNum);
      }
      if(freeBoardRepository.commentListCnt(communityNum) > 0) {
         cnt += freeBoardRepository.communityDeleteComment(communityNum);
      }
      
      cnt += freeBoardRepository.communityDelete(communityNum);
      
      log.debug("cnt -- " + cnt);
      
      return cnt;
   }
   
   //게시글 수정
   public int communityUpdate(Community commu) {
      int cnt = 0;
      
      if(commu != null) {
         try {
            cnt = freeBoardRepository.communityUpdate(commu);
         }catch(Exception e) {
            log.debug("자유게시판 Service Error likeSelectCnt -- ", e);
         }
      }
      
      return cnt;
   }

   
 //24.05.14 나의글 관련 소스 추가
   // 나의글 자유게시판 리스트(마이페이지)
	public List<Community> myWriteBoardList(long curPage, Community commu) {

		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		List<Community> list = null;
		commu.setUserId(userId);
		long totalCnt = myListTotalCnt(commu);

		try 
		{
			totalCnt = myListTotalCnt(commu);

			if (totalCnt > 0) 
			{
				Paging paging = myPagingService(curPage, commu);
				commu.setStartRnum(paging.getStartRow());
				commu.setEndRnum(paging.getEndRow());

				list = freeBoardRepository.myWriteBoardList(commu);

			}
		} catch (Exception e) {
			logger.debug("나의글 자유게시판 Service Error freeBoardList -- ", e);
			list = Collections.emptyList();
		}

		log.info("나의글 자유게시판 서비스 SearchType / searchValue / totalCnt -- " + commu.getSearchType() + " / "
				+ commu.getSearchValue() + " / " + totalCnt);

		return list;
	}

	// 나의글 자유게시판 페이징(마이페이지) (+ totalCnt)
	public Paging myPagingService(long curPage, Community commu) {

		Paging paging = null;
		long totalCnt = 0;

		try {
			totalCnt = freeBoardRepository.myWriteTotalCnt(commu);
		} catch (Exception e) {
			logger.debug("나의 글 자유게시판 Service Error paging -- ", e);
		}

		if (totalCnt > 0) {
			paging = new Paging("/user/myWriteList", totalCnt, LIST_CNT, PAGE_CNT, curPage, "curPage");
		}

		return paging;
	}

	// 나의 글 totalCnt(마이페이지)
	public long myListTotalCnt(Community commu) {
		long totalCnt = 0;

		try {
			totalCnt = freeBoardRepository.myWriteTotalCnt(commu);
		} catch (Exception e) {
			logger.debug("나의 글 자유게시판 Service Error ㅡ myListTotalCnt -- ", e);
		}

		return totalCnt;
	}
   
}