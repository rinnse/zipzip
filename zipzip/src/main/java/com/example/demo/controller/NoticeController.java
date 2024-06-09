package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Notice;
import com.example.demo.dto.NoticeFile;
import com.example.demo.service.ManagerService;
import com.example.demo.service.NoticeService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final NoticeService noticeService;
	
	private static final int LIST_COUNT = 5;		//한 페이지 게시물 수
	private static final int PAGE_COUNT = 5;		//페이징 수
	
	@GetMapping("/board/noticeInsert")
	public String insert(Model model) {
		
		return "board/noticeInsert";
	}
	
//	@PostMapping("/board/noticeInsert")
//	public String insert(Notice notice) {
//		
//		noticeService.insert(notice);
//		
//		System.out.println("notice : " + notice);
//		
//		return "redirect:/board/notice";
//	}
	
	@PostMapping("/board/noticeInsert")
	public String insert(Notice notice) throws IOException {
		
		noticeService.insert(notice);
		System.out.println("insert : " + notice);
		
		return "redirect:/board/notice";
	}
	
	@RequestMapping("/board/notice")
	public String selectAll(@RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		
		List<Notice> noticeList = null;
		Notice notice = new Notice();
		Paging paging = null;
		long totalCnt = 0;
		
		System.out.println("searchValue : " + searchValue);
		System.out.println("curPage : " + curPage);
		
		if (!StringUtil.isEmpty(searchValue)) {
			notice.setSearchValue(searchValue);
		}

		totalCnt = noticeService.listCnt(notice);
		System.out.println("totalCnt : " + totalCnt);
		
		if (totalCnt > 0) {
			paging = new Paging("/board/notice", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			notice.setStartRow(paging.getStartRow());
			notice.setEndRow(paging.getEndRow());
			
			noticeList = noticeService.selectAll(notice);
			System.out.println("noticeList : " + noticeList);
			System.out.println("notice : " + notice);
		}
		
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("noticeList", noticeList);
		
		return "board/notice";
	}
	
	@RequestMapping("/board/notice/{noticeNum}")
	public String selectOne(@PathVariable("noticeNum") long noticeNum, @RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		System.out.println("searchValue : " + searchValue);
		
		// 조회
		noticeService.updateHit(noticeNum);
		
		// 상세
		Notice notice = noticeService.selectOne(noticeNum);
		System.out.println("notice : " + notice);
		
		// 단일 파일
//		if (notice.getFileAtt() == 1) {
//			NoticeFile noticeFile = noticeService.selectFile(noticeNum);
//			model.addAttribute("noticeFile", noticeFile);
//		}
		
		// 다중 파일
		if (notice.getFileAtt() == 1) {
			List<NoticeFile> noticeFileList = noticeService.selectFile(noticeNum);
			model.addAttribute("noticeFileList", noticeFileList);
		}
		
		model.addAttribute("noticeNum", noticeNum);
		model.addAttribute("curPage", curPage);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("notice", notice);
		
		return "board/noticeDetail";
	}
	
	@GetMapping("/board/noticeUpdate/{noticeNum}")
	public String update(@PathVariable("noticeNum") long noticeNum, Model model) {
		
		Notice notice = noticeService.selectOne(noticeNum);
		model.addAttribute("notice", notice);
		
		return "board/noticeUpdate";
	}
	
	@PostMapping("/board/noticeUpdate/{noticeNum}")
	public String update(Notice notice, Model model) {
		
		noticeService.update(notice);
		Notice detail = noticeService.selectOne(notice.getNoticeNum());
		model.addAttribute("notice", detail);
		
		return "board/noticeDetail";
	}
	
	@GetMapping("/board/noticeDelete/{noticeNum}")
	public String delete(@PathVariable("noticeNum") long noticeNum) {
		
		noticeService.delete(noticeNum);
		
		return "redirect:/board/notice";
	}
	
	
}
