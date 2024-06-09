package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Agent;
import com.example.demo.dto.Manager;
import com.example.demo.dto.Qna;
import com.example.demo.dto.User;
import com.example.demo.service.AgentService;
import com.example.demo.service.ManagerService;
import com.example.demo.service.QnaService;
import com.example.demo.service.UserService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QnaController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final QnaService qnaService;
	private final UserService userService;
	private final AgentService agentService;
	private final ManagerService managerService;
	
	private static final int LIST_COUNT = 5;		//한 페이지 게시물 수
	private static final int PAGE_COUNT = 5;		//페이징 수
	
	@GetMapping("/board/qnaInsert")
	public String insert(Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		Agent agent = agentService.findById(userId);
		Manager manager = managerService.findById(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("agent", agent);
		model.addAttribute("manager", manager);
		
		return "board/qnaInsert";
	}
	
	@PostMapping("/board/qnaInsert")
	public String insert(Qna qna) {
		
		qnaService.insert(qna);
		System.out.println("qna : " + qna);
		
		return "redirect:/board/qna";
	}
	
	@RequestMapping("/board/qna")
	public String qna(@RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		List<Qna> qnaList = null;
		Qna qna = new Qna();
		Paging paging = null;
		long totalCnt = 0;
		
		System.out.println("searchValue : " + searchValue);
		System.out.println("curPage : " + curPage);
		
		if (!StringUtil.isEmpty(searchValue)) {
			qna.setSearchValue(searchValue);
		}

		totalCnt = qnaService.listCnt(qna);
		System.out.println("totalCnt : " + totalCnt);
		
		if (totalCnt > 0) {
			paging = new Paging("/board/qna", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			qna.setStartRow(paging.getStartRow());
			qna.setEndRow(paging.getEndRow());
			
			qnaList = qnaService.selectAll(qna);
			System.out.println("qnaList : " + qnaList);
			System.out.println("qna : " + qna);
		}
		
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("qnaList", qnaList);
		
		return "board/qna";
	}
	
	@RequestMapping("/board/qna/{qnaNum}")
	public String selectOne(@PathVariable("qnaNum") long qnaNum, @RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		System.out.println("searchValue : " + searchValue);
		
		// 조회
		qnaService.updateHit(qnaNum);
		
		// 상세
		Qna qna = qnaService.selectOne(qnaNum);
		System.out.println("qna : " + qna);
		
		model.addAttribute("qnaNum", qnaNum);
		model.addAttribute("curPage", curPage);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("qna", qna);
		
		return "board/qnaDetail";
	}
	
	@GetMapping("/board/qnaUpdate/{qnaNum}")
	public String update(@PathVariable("qnaNum") long qnaNum, Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		Agent agent = agentService.findById(userId);
		Manager manager = managerService.findById(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("agent", agent);
		model.addAttribute("manager", manager);
		
		Qna qna = qnaService.selectOne(qnaNum);
		model.addAttribute("qna", qna);
		
		return "board/qnaUpdate";
	}
	
	@PostMapping("/board/qnaUpdate/{qnaNum}")
	public String update(Qna qna, Model model) {
		
		qnaService.update(qna);
		Qna detail = qnaService.selectOne(qna.getQnaNum());
		model.addAttribute("qna", detail);
		
		return "board/qnaDetail";
	}
	
	@GetMapping("/board/qnaDelete/{qnaNum}")
	public String delete(@PathVariable("qnaNum") long qnaNum) {
		
		qnaService.delete(qnaNum);
		
		return "redirect:/board/qna";
	}
	
	@RequestMapping("/board/qnaReply")
	public String reply(@RequestParam(value = "qnaNum", defaultValue = "0") long qnaNum, @RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		String searchValue = request.getParameter("searchValue");
		System.out.println("searchValue : " + searchValue);
		
		Qna qna = null;
		Manager manager = null;
		
		if (qnaNum > 0) {
			qna = qnaService.selectOne(qnaNum);
			
			if (qna != null) {
				manager = managerService.findById(userId);
			}
		}
		
		model.addAttribute("qnaNum", qnaNum);
		model.addAttribute("curPage", curPage);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("qna", qna);
		model.addAttribute("manager", manager);

		return "board/qnaReply";
	}
	
	@RequestMapping("/board/qnaReplyProc")
	public String replyProc(@RequestParam(value = "qnaNum", defaultValue = "0") long qnaNum, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		Manager manager = managerService.findById(userId);
		
		String qnaTitle = request.getParameter("qnaTitle");
		String qnaContent = request.getParameter("qnaContent");
		System.out.println("qnaTitle : " + qnaTitle);
		System.out.println("qnaContent : " + qnaContent);
		
		Qna parent = qnaService.selectOne(qnaNum);
		
		if (parent != null) {
			Qna qna = new Qna();
			
			qna.setUserId(userId);
			qna.setQnaTitle(qnaTitle);
			qna.setQnaContent(qnaContent);
			qna.setQnaGroup(parent.getQnaGroup());
			qna.setQnaOrder(parent.getQnaOrder() + 1);
			qna.setQnaIndent(parent.getQnaIndent() + 1);
			qna.setQnaParent(qnaNum);
			
			qnaService.insertReply(qna);
		}
		
		return "redirect:/board/qna";
	}
}
