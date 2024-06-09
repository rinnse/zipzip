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
import com.example.demo.dto.Sense;
import com.example.demo.dto.User;
import com.example.demo.service.ManagerService;
import com.example.demo.service.NoticeService;
import com.example.demo.service.SenseService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SenseController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final SenseService senseService;
	private final ManagerService managerService;
	
	private static final int LIST_COUNT = 5;		//한 페이지 게시물 수
	private static final int PAGE_COUNT = 5;		//페이징 수
	
	@GetMapping("/etc/senseInsert")
	public String insert(Model model) {
		
		return "etc/senseInsert";
	}
	
	@PostMapping("/etc/senseInsert")
	public String insert(Sense sense) {
		
		senseService.insert(sense);
		System.out.println("insert : " + sense);
		
		return "redirect:/etc/sense";
	}
	
	@RequestMapping("/etc/sense")
	public String selectAll(@RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		
		List<Sense> senseList = null;
		Sense sense = new Sense();
		Paging paging = null;
		long totalCnt = 0;
		
		System.out.println("searchValue : " + searchValue);
		System.out.println("curPage : " + curPage);
		
		if (!StringUtil.isEmpty(searchValue)) {
			sense.setSearchValue(searchValue);
		}
		
		totalCnt = senseService.listCnt(sense);
		System.out.println("totalCnt : " + totalCnt);
		
		if (totalCnt > 0) {
			paging = new Paging("/etc/sense", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			sense.setStartRow(paging.getStartRow());
			sense.setEndRow(paging.getEndRow());
			
			senseList = senseService.selectAll(sense);
			System.out.println("senseList : " + senseList);
			System.out.println("sense : " + sense);
		}
		
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("senseList", senseList);
		
		return "etc/sense";
	}
	
	@RequestMapping("/etc/sense/{senseNum}")
	public String selectOne(@PathVariable("senseNum") long senseNum, @RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		System.out.println("searchValue : " + searchValue);
		
		senseService.updateHit(senseNum);
		
		Sense sense = senseService.selectOne(senseNum);
		System.out.println("sense : " + sense);
		
		model.addAttribute("senseNum", senseNum);
		model.addAttribute("curPage", curPage);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("sense", sense);
		
		return "etc/senseDetail";
	}
	
	@GetMapping("/etc/senseUpdate/{senseNum}")
	public String update(@PathVariable("senseNum") long senseNum, Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		Manager manager = managerService.findById(userId);
		
		model.addAttribute("manager", manager);
		
		Sense sense = senseService.selectOne(senseNum);
		model.addAttribute("sense", sense);
		
		return "etc/senseUpdate";
	}
	
	@PostMapping("/etc/senseUpdate/{senseNum}")
	public String update(Sense sense, Model model) {
		
		senseService.update(sense);
		
		Sense detail = senseService.selectOne(sense.getSenseNum());
		model.addAttribute("sense", detail);
		
		return "etc/senseDetail";
	}
	
	@GetMapping("/etc/senseDelete/{senseNum}")
	public String delete(@PathVariable("senseNum") long senseNum) {
		
		senseService.delete(senseNum);
		
		return "redirect:/etc/sense";
	}
}
