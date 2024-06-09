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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Event;
import com.example.demo.service.EventService;
import com.example.demo.service.SenseService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EventController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EventService eventService;
	
	private static final int LIST_COUNT = 5;		//한 페이지 게시물 수
	private static final int PAGE_COUNT = 5;		//페이징 수
	
	@GetMapping("/etc/eventInsert")
	public String insert(Model model) {
		return "etc/eventInsert";
	}
	
	@PostMapping("/etc/eventInsert")
	public String insert(Event event, MultipartFile file) throws Exception {
		
		eventService.insert(event, file);
		System.out.println("insert : " + event);
		
		return "redirect:/etc/event";
	}
	
	@RequestMapping("/etc/event")
	public String selectAll(@RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {

		String searchValue = request.getParameter("searchValue");
		
		List<Event> eventList = null;
		Event event = new Event();
		Paging paging = null;
		long totalCnt = 0;
		
		System.out.println("searchValue : " + searchValue);
		System.out.println("curPage : " + curPage);
		
		if (!StringUtil.isEmpty(searchValue)) {
			event.setSearchValue(searchValue);
		}
		
		totalCnt = eventService.listCnt(event);
		System.out.println("totalCnt : " + totalCnt);
		
		if (totalCnt > 0) {
			paging = new Paging("/etc/event", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			event.setStartRow(paging.getStartRow());
			event.setEndRow(paging.getEndRow());
			
			eventList = eventService.selectAll(event);
			System.out.println("eventList : " + eventList);
			System.out.println("event : " + event);
		}
		
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("eventList", eventList);
		
		return "etc/event";
	}
	
	@RequestMapping("/etc/event/{eventNum}")
	public String selectOne(@PathVariable("eventNum") long eventNum, @RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String searchValue = request.getParameter("searchValue");
		System.out.println("searchValue : " + searchValue);
		
		eventService.updateHit(eventNum);
		
		Event event = eventService.selectOne(eventNum);
		System.out.println("evnet : " + event);
		
		model.addAttribute("evnetNum", eventNum);
		model.addAttribute("curPage", curPage);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("event", event);
		
		return "etc/eventDetail";
	}
	
	@GetMapping("/etc/eventUpdate/{eventNum}")
	public String update(@PathVariable("eventNum") long eventNum, Model model) {
		
		Event event = eventService.selectOne(eventNum);
		model.addAttribute("event", event);
		
		return "etc/eventUpdate";
	}
	
	@PostMapping("/etc/eventUpdate/{eventNum}")
	public String update(Event event, MultipartFile file, Model model) throws IOException {
		
		eventService.update(event, file);
		
		//Event detail = eventService.selectOne(event.getEventNum());
		event = eventService.selectOne(event.getEventNum());
		System.out.println("controller event : " + event);
		model.addAttribute("event", event);
		
		return "redirect:/etc/event/{eventNum}";
	}
	
	@GetMapping("/etc/eventDelete/{eventNum}")
	public String delete(@PathVariable("eventNum") long eventNum) {
		
		eventService.delete(eventNum);
		
		return "redirect:/etc/event";
	}
}
