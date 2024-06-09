package com.example.demo.controller;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.Community;
import com.example.demo.dto.CommunityComment;
import com.example.demo.dto.User;
import com.example.demo.service.AgentService;
import com.example.demo.service.FreeBoardService;
import com.example.demo.service.UserService;
import com.example.demo.util.Paging;
import com.example.demo.util.PhotoUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FreeBoardController {
	
	   private final PhotoUtil photoUtil;
	   private final UserService userService;
	   private final FreeBoardService freeBoardService;
	   private final AgentService agentService;
	   
	   
	   //자유게시판 리스트 화면
	   @RequestMapping("/board/freeBoard")
	   public String freeBoard(@RequestParam(value = "curPage", defaultValue = "1") long curPage, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(loginId);
//	      User user = null;
	      
	      String searchType = request.getParameter("searchType");
	      String searchValue = request.getParameter("searchValue");
	      String sortParam = request.getParameter("sort");   // java.lang.NumberFormatException: null 에러
	      long sort = (sortParam != null && sortParam != "" && !sortParam.isEmpty() && sortParam != "undefined") ? Long.parseLong(sortParam) : 1L;
	      
	      Community commu = new Community();
	      List<Community> list = null;
	      Paging paging = null;
	      long totalCnt = 0;
	      long commentCnt = 0;
	      
	      commu.setSearchType(searchType);
	      commu.setSearchValue(searchValue);
	      commu.setSort(sort);
	      
	      totalCnt = freeBoardService.listTotalCnt(commu);
	      list = freeBoardService.freeBoardList(curPage, commu);
	      paging = freeBoardService.pagingService(curPage, commu);
//	      commentCnt = freeBoardService.commentListCnt(commentCnt);
	      
	      //중개인??
//	      if(loginId != null) {
//	         if(userService.userSelect(loginId) != null) {
//	            user = userService.userSelect(loginId);
//	            model.addAttribute("user", user);
//	         }
//	         else if(agentService.agentSelect(loginId) != null) {
//	            Agent agent = agentService.agentSelect(loginId);
//	            model.addAttribute("user", agent);
//	         }
//	         else {
//	            log.debug("Not User");
//	         }
//	      }
	      

	      model.addAttribute("user", user);
	      model.addAttribute("searchType", searchType);
	      model.addAttribute("searchValue", searchValue);
	      model.addAttribute("curPage", curPage);
	      model.addAttribute("list", list);
	      model.addAttribute("paging", paging);
	      model.addAttribute("sort", sort);
	      
	      
	      // 로그
	      if(list != null && paging != null) {
	         log.info("컨트롤러 리스트 객체 사이즈 - " + list.size());
	         log.info("컨트롤러 페이징 값 StartPage / getEndPage / PrevBlockPage / CurPage - " + paging.getStartPage() + " / " + paging.getEndPage() + " / " + paging.getPrevBlockPage() + " / " + paging.getCurPage());
	         log.info("컨트롤러 페이징 널? - " + paging);
	      }
//	      log.info("현재페이지" + curPageParam);
	      log.info("컨트롤러 변수들 - " + searchType + " -- " + searchValue + " -- " + curPage);
	      log.info("현재페이지 curPage - " + curPage);
	      log.info("현재페이지 sort - " + sort);
	      log.info("commu sort - " + commu.getSort());
	      
	      return "/board/freeBoard";
	   }
	   
	   private File File(String string) {
	      // TODO Auto-generated method stub
	      return null;
	   }


	   //자유게시판 상세 화면
	   @RequestMapping("/board/freeBoardDetail")
	   public String freeBoardDetail(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(loginId);
	      
	      String searchType = request.getParameter("searchType");
	      String searchValue = request.getParameter("searchValue");
	      String curPageParam = request.getParameter("curPage");
	      long curPage = (curPageParam != null && curPageParam != "" && !curPageParam.isEmpty() && curPageParam != "undefined") ? Long.parseLong(curPageParam) : 1L;
	      String sortParam = request.getParameter("sort");
	      long sort = (sortParam != null && sortParam != "" && !sortParam.isEmpty() && sortParam != "undefined") ? Long.parseLong(sortParam) : 1L;
	      
	      Community commu = null;
	      List<CommunityComment> list = null;
	      String me = "N";
	      String likeDone = "N";
	      long likeTotalCnt = 0;
	      
	      if(communityNum > 0) {
	         commu = freeBoardService.freeBoardDetail(communityNum);
	         
	         if(commu != null && loginId != null) {
	            if(Objects.equals(loginId, commu.getUserId())) {
	               me = "Y";
	            }
	            
	            list = freeBoardService.commentList(communityNum);
	            
	            Community likeCommu = new Community();
	            likeCommu.setUserId(loginId);
	            likeCommu.setCommunityNum(communityNum);
	            
	            if(freeBoardService.likeSelectCnt(likeCommu) > 0) {
	               likeDone = "Y";
	            }
	            
	            likeTotalCnt = freeBoardService.likeTotalCnt(communityNum);
	            
//	            if(commu.getUserId() != null) {   //회원이 글
//	               log.info("회원 글");
//	            }
//	            else if(commu.getAgentId() != null) {   //중개인이 글
//	               log.info("중개인 글");
//	               if(commu.getAgentId() == agentService.agentSelect(loginId).getAgentId()) {
//	                  
//	               }
//	            }
//	            else {
//	               log.error("게시글 작성자 error");
//	            }
	         }
	         else {
	            commu = null;
	         }
	      }
	      
	      
	      model.addAttribute("likeTotalCnt", likeTotalCnt);
	      model.addAttribute("likeDone", likeDone);
	      model.addAttribute("list", list);
	      model.addAttribute("me", me);
	      model.addAttribute("user", user);
	      model.addAttribute("commu", commu);
	      model.addAttribute("communityNum", communityNum);
	      model.addAttribute("searchType", searchType);
	      model.addAttribute("searchValue", searchValue);
	      model.addAttribute("curPage", curPage);
	      model.addAttribute("sort", sort);
	      
	      log.info("[freeBoardDetail] list - " + list);
	      log.info("[freeBoardDetail] commu - " + commu);
	      log.info("[freeBoardDetail] communityNum - " + communityNum);
//	      log.info("[freeBoardDetail] loginId - " + loginId);
//	      log.info("[freeBoardDetail] commu.getUserId() - " + commu.getUserId());
//	      log.info("[freeBoardDetail] me - " + me);
	      
	      return "board/freeBoardDetail.html";
	   }
	   
	   //자유게시판 글쓰기 화면
	   @RequestMapping("/board/freeBoardWrite")
	   public String freeBoardWrite(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(loginId);
	      
	      String curPageParam = request.getParameter("curPage");
	      long curPage = (curPageParam != null && curPageParam != "" && !curPageParam.isEmpty() && curPageParam != "undefined") ? Long.parseLong(curPageParam) : 1L;
	      String searchType = request.getParameter("searchType");
	      String searchValue = request.getParameter("searchValue");
	      String sortParam = request.getParameter("sort");
	      long sort = (sortParam != null && sortParam != "" && !sortParam.isEmpty() && sortParam != "undefined") ? Long.parseLong(sortParam) : 1L;
	      
	      if(loginId != null) {
	         log.info("로그인 아이디 있음");
	      }
	      
	      model.addAttribute("user", user);

	      log.info("loginId -- " + loginId);
	      log.info("curPage / serachType / searchValue / sort -- " + curPage +" / " + searchType +" / " + searchValue +" / " + sort);
	      
	      return "/board/freeBoardWrite.html";
	   }
	   
	   //ckeditor 사진 업로드 (게시글 작성 중)
	   @RequestMapping("/upload")
	   @ResponseBody
	   public ModelAndView upload(MultipartHttpServletRequest request) {
	      log.info("-- /upload 컨트롤러 -- ");
	      
	      ModelAndView mav = new ModelAndView("jsonView");
	      
	      String uploadPath = photoUtil.ckUpload(request);   //서버 업로드 url
	      
//	      String url = "/resources/upload/3fa42e2d-95dc-4101-823f-d3621db6e759@스크린샷 2024-04-23 144049.png";
	      
	      mav.addObject("uploaded", true);
	      mav.addObject("url", uploadPath);
	      
	      log.info("uploadPath -- " + uploadPath);
	      
	      return mav;
	   }
	   
	   //게시글 등록 Proc
	   @RequestMapping("/board/writeProc")
	   @ResponseBody
	   public int writeProc(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(loginId);
	      
	      int resCode = 0;
	      String resMsg = "";
	      
	      String userId = loginId;
	      String agentId = "";
	      String boardWriter = request.getParameter("boardWriter");
	      String boardTitle = request.getParameter("boardTitle");
	      String boardContent = request.getParameter("boardContent");
	      
//	      if(boardWriter.isEmpty() || boardWriter == null) {
//	         log.info("empty");
//	      }
	      
	      
	      if(userService.userSelect(loginId) != null) {
	         log.info("회원 유저");
	      }
	      else if(agentService.agentSelect(loginId) != null) {
	         log.info("중개인 유저");
	      }
	      
	      if(!boardTitle.isEmpty() && !boardContent.isEmpty()) {
	         Community commu = new Community();
	         commu.setUserId(userId);
	         commu.setAgentId(agentId);
	         commu.setCommunityTitle(boardTitle);
	         commu.setCommunityContent(boardContent);
	         
	         if(freeBoardService.communityInsert(commu) > 0) {
	            resCode = 200;
	         }
	         else {
	            resCode = 500;
	         }
	      }
	      else {
	         resCode = 400;
	      }
	      
	      
	      model.addAttribute("user", user);
	      
	      //추가 -> 에러메세지 추가 + 로그인 아이디,쿠키아이디 확인
	      
	      log.info("resCode -- " + resCode);
	      log.info("boardWriter -- " + boardWriter);
	      log.info("boardTitle -- " + boardTitle);
	      log.info("boardContent -- " + boardContent);
	      
	      
	      return resCode;
	   }
	   
	   
	   //게시글 수정 화면
	   @RequestMapping("/board/freeBoardUpdate")
	   public String freeBoardUpdate(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(loginId);
	      
	      Community commu = null;
	      
	      if(communityNum > 0) {
	         commu = freeBoardService.freeBoardDetail(communityNum);
	         if(commu != null && loginId != null) {
	            
	         }
	         else {
	            commu = null;
	         }
	      }
	      
	      model.addAttribute("communityNum", communityNum);
	      model.addAttribute("commu", commu);
	      model.addAttribute("user", user);
	      
	      log.info("commu -- " + commu);
	      
	      return "board/freeBoardUpdate.html";
	   }
	   
	   //게시글 수정 proc
	   @RequestMapping("/board/updateProc")   
	   @ResponseBody
	   public int freeBoardUpdateProc(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(loginId);
	      
	      int resCode = 0;
	      String resMsg = "";
	      
	      String boardWriter = request.getParameter("boardWriter");
	      String boardTitle = request.getParameter("boardTitle");
	      String boardContent = request.getParameter("boardContent");
	      
	      log.info("00000000000");
	      
	      if(communityNum > 0) {
	         log.info("11111111111111");
	         if(!boardTitle.isEmpty() && !boardContent.isEmpty()) {
	            Community commu = freeBoardService.communitySelect(communityNum);
	            log.info("222222222222222");
	            if(commu != null) {
	               commu.setCommunityNum(communityNum);
	               commu.setUserId(loginId);
	               commu.setCommunityTitle(boardTitle);
	               commu.setCommunityContent(boardContent);
	               log.info("333333333333333333");
	               
	               if(freeBoardService.communityUpdate(commu) > 0) {
	                  resCode = 200;
	               }
	               else {
	                  resCode = 500;
	               }
	            }else {
	               resCode = 404;
	            }
	         }else {
	            resCode = 400;
	         }
	      }else {
	         resCode = 400;
	      }

	      
	      
	      model.addAttribute("user", user);
	      
	      log.info("Update - resCode -- " + resCode);
	      log.info("communityNum -- " + communityNum);
	      log.info("boardWriter -- " + boardWriter);
	      log.info("boardTitle -- " + boardTitle);
	      log.info("boardContent -- " + boardContent);
	      
	      return resCode;
	   }
	   
	   
	   //게시글 댓글 등록 Proc
	   @RequestMapping("/board/CommentWriteProc")
	   @ResponseBody
	   public int CommentWriteProc(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, HttpServletRequest request, HttpServletResponse response) {

//	      String userId = "user6";
//	      String agentId = "";
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      
	      int resCode = 0;
	      String resMsg = "";
	      CommunityComment comment = null;
	      
	      String boardComment = request.getParameter("boardComment");
	      String commentGroupStr = request.getParameter("commentGroup");
	      long commentGroup = (commentGroupStr != null && commentGroupStr != "" && !commentGroupStr.isEmpty() && commentGroupStr != "undefined") ? Long.parseLong(commentGroupStr) : 0L;
	      log.info("commentGroup -- " + commentGroup);
	      
	      User user = userService.userSelect(loginId);
	      
	      if(user != null) {   //로그인 안되있을경우도
	         if(Objects.equals(user.getStatus(), "Y")) {
	            if(communityNum > 0 && !boardComment.isEmpty() && boardComment != null) {
	               if(freeBoardService.freeBoardDetail(communityNum) != null) {
	                  comment = new CommunityComment();
	                  comment.setCommunityNum(communityNum);
	                  comment.setUserId(loginId);
	                  comment.setCommentContent(boardComment);
	                  comment.setCommentGroup(commentGroup);
	                  if(freeBoardService.CommentInsert(comment) > 0) {
	                     resCode = 200;
	                  }else {
	                     resCode = 500;
	                  }
	               }else {
	                  resCode = 404;   //게시글 존재x
	               }
	            }else {
	               resCode = 400;   //파라미터값오류
	            }
	         }else {
	            resCode = 301;   //상태x
	         }
	      }else {
	         resCode = 300;   //회원유저x
	      }
	      
	      
	      //추가 -> 에러메세지 추가 + 로그인 아이디,쿠키아이디 확인
	      
	      log.info("resCode -- " + resCode);
	      log.info("boardComment -- " + boardComment);
	      log.info("communityNum -- " + communityNum);
	      
	      
	      return resCode;
	   }
	   
	   
	   //게시글 댓글 삭제 Proc
	   @RequestMapping("/board/CommentDeleteProc")
	   @ResponseBody
	   public int CommentDeleteProc(@RequestParam(value = "commentNum", defaultValue = "0") long commentNum, HttpServletRequest request, HttpServletResponse response) {

	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      
	      int resCode = 0;
	      String resMsg = "";
	      CommunityComment comment = null;
	      
	      User user = userService.userSelect(loginId);
	      
	      String communityNumStr = request.getParameter("communityNum");
	      long communityNum = (communityNumStr != null && communityNumStr != "" && !communityNumStr.isEmpty() && communityNumStr != "undefined") ? Long.parseLong(communityNumStr) : 0L;
	      
	      if(user != null) {   //로그인 안되있을경우도
	         if(Objects.equals(user.getStatus(), "Y")) {
	            if(communityNum > 0 && commentNum > 0) {
	               Community commu = freeBoardService.freeBoardDetail(communityNum);
	               comment = freeBoardService.commentSelect(commentNum);
	               if(commu != null && comment != null) {
	                  if(Objects.equals(user.getUserId(), comment.getUserId())){
	                     if(freeBoardService.commentDeleteUpdate(commentNum) > 0) {
	                        resCode = 200;
	                     }else {
	                        resCode = 500;
	                     }
	                  }else {
	                     resCode = 405;
	                  }
	               }else {
	                  resCode = 404;   //게시글 존재x
	               }
	            }else {
	               resCode = 400;   //파라미터값오류
	            }
	         }else {
	            resCode = 301;   //상태x
	         }
	      }else {
	         resCode = 300;   //회원유저x
	      }
	      
	      
	      log.info("[CommentDeleteProc] resCode -- " + resCode);
	      log.info("communityNum -- " + communityNum);
	      log.info("[CommentDeleteProc] resCode -- " + resCode);
	      
	      return resCode;
	   }   
	   
	   //게시글 추천 Proc
	   @RequestMapping("/board/CommentLikeProc")
	   @ResponseBody
	   public int CommentLikeProc(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, ModelMap model, HttpServletRequest request, HttpServletResponse response) {

	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      
	      int resCode = 0;
	      String resMsg = "";
	      
	      User user = userService.userSelect(loginId);
	      
	      if(user != null) {   
	         if(Objects.equals(user.getStatus(), "Y")) {
	            if(communityNum > 0) {
	               Community commu = freeBoardService.freeBoardDetail(communityNum);
	               if(commu != null) {
	                  commu.setUserId(loginId);
	                  //기존에 있을시 delete
	                  if(freeBoardService.likeSelectCnt(commu) > 0) {
	                     if(freeBoardService.likeDelete(commu) > 0) {
	                        resCode = -200;
	                     }else {
	                        resCode = -500;
	                     }
	                  }else {   //기존에 없을시 insert
	                     if(freeBoardService.likeInsert(commu) > 0) {
	                        resCode = 200;
	                     }else {
	                        resCode = 500;
	                     }                  
	                  }
	               }else {
	                  resCode = 404;   //게시글 존재x
	               }
	            }else {
	               resCode = 400;   //파라미터값오류
	            }
	         }else {
	            resCode = 301;   //상태x
	         }
	      }else {
	         resCode = 300;   //회원유저x
	      }
	      
	      long likeTotalCnt = freeBoardService.likeTotalCnt(communityNum);
	      
	      model.addAttribute("likeTotalCnt", likeTotalCnt);
	      
	      log.info("[CommentLikeProc] resCode -- " + resCode);
	      log.info("[CommentLikeProc] communityNum -- " + communityNum);
	      
	      return resCode;
	   }
	   
	   
	   //게시글 삭제 Proc (추천,댓글,게시글 삭제)
	   @RequestMapping("/board/CommunityDeleteProc")
	   @ResponseBody
	   public int CommunityDeleteProc(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, HttpServletRequest request, HttpServletResponse response) {
	      String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
	      
	      int resCode = 0;
	      String resMsg = "";
	      int cnt = 0;
	      
	      User user = userService.userSelect(loginId);
	      
	      if(user != null) {
	         if(Objects.equals(user.getStatus(), "Y")) {
	            if(communityNum > 0) {
	               Community commu = freeBoardService.freeBoardDetail(communityNum);
	               if(commu != null) {
	                  try {
	                     cnt = freeBoardService.communityDelete(communityNum);
	                     if(cnt > 0) {
	                        resCode = 200;
	                     }
	                     else {
	                        resCode = 500;
	                     }
	                  }catch(Exception e) {
	                     log.debug("자유게시판 Controller Error CommunityDeleteProc -- ", e);
	                  }
	               }else {
	                  resCode = 404;   //게시글 존재x
	               }
	            }else {
	               resCode = 400;   //파라미터값오류
	            }
	         }else {
	            resCode = 301;   //상태x
	         }
	      }else {
	         resCode = 300;   //회원유저x
	      }
	      
	      log.info("[CommunityDeleteProc] cnt -- " + cnt);
	      log.info("[CommunityDeleteProc] resCode -- " + resCode);
	      
	      return resCode;
	   }
	   
}