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
import com.example.demo.dto.Suggest;
import com.example.demo.dto.User;
import com.example.demo.service.AgentService;
import com.example.demo.service.ManagerService;
import com.example.demo.service.SuggestService;
import com.example.demo.service.UserService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SuggestController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	private final SuggestService suggestService;
	private final UserService userService;
	private final AgentService agentService;
	private final ManagerService managerService;
	
	private static final int LIST_COUNT = 5;		//한 페이지 게시물 수
	private static final int PAGE_COUNT = 5;		//페이징 수
		
	@GetMapping("/board/suggestInsert")
	public String insert(Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		Agent agent = agentService.findById(userId);
		Manager manager = managerService.findById(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("agent", agent);
		model.addAttribute("manager", manager);
		
		return "board/suggestInsert";
	}
	
	@PostMapping("/board/suggestInsert")
	public String insert(Suggest suggest) {
		
		suggestService.insert(suggest);
		System.out.println("suggest : " + suggest);
		
		return "redirect:/board/suggest";
	}
		
	@RequestMapping("/board/suggest")
	public String selectAll(@RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		
		List<Suggest> suggestList = null;
		Suggest suggest = new Suggest();
		Paging paging = null;
		long totalCnt = 0;
		
		System.out.println("searchValue : " + searchValue);
		System.out.println("curPage : " + curPage);
		
		if (!StringUtil.isEmpty(searchValue)) {
			suggest.setSearchValue(searchValue);
		}
		
		totalCnt = suggestService.listCnt(suggest);
		System.out.println("totalCnt : " + totalCnt);
				
		if (totalCnt > 0) {
			paging = new Paging("/board/suggest", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			suggest.setStartRow(paging.getStartRow());
			suggest.setEndRow(paging.getEndRow());
			
			suggestList = suggestService.selectAll(suggest);
			System.out.println("suggestList : " + suggestList);
			System.out.println("suggest : " + suggest);
		}
		
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("suggestList", suggestList);
		
		return "board/suggest";
	}
	
	@RequestMapping("/board/suggest/{suggestNum}")
	public String selectOne(@PathVariable("suggestNum") long suggestNum, @RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		System.out.println("searchValue : " + searchValue);
		
		// 조회
		suggestService.updateHit(suggestNum);
		
		// 상세
		Suggest suggest = suggestService.selectOne(suggestNum);
		System.out.println("suggest : " + suggest);
		
		model.addAttribute("suggestNum", suggestNum);
		model.addAttribute("curPage", curPage);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("suggest", suggest);
		
		return "board/suggestDetail";
	}
	
	@GetMapping("/board/suggestUpdate/{suggestNum}")
	public String update(@PathVariable("suggestNum") long suggestNum, Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		Agent agent = agentService.findById(userId);
		Manager manager = managerService.findById(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("agent", agent);
		model.addAttribute("manager", manager);
		
		Suggest suggest = suggestService.selectOne(suggestNum);
		model.addAttribute("suggest", suggest);
		
		return "board/suggestUpdate";
	}
	
	@PostMapping("/board/suggestUpdate/{suggestNum}")
	public String update(Suggest suggest, Model model) {
		
		suggestService.update(suggest);
		
		Suggest detail = suggestService.selectOne(suggest.getSuggestNum());
		model.addAttribute("suggest", detail);
		
		return "board/suggestDetail";
	}
	
	@GetMapping("/board/suggestDelete/{suggestNum}")
	public String delete(@PathVariable("suggestNum") long suggestNum) {
		
		suggestService.delete(suggestNum);
		
		return "redirect:/board/suggest";
	}
}
