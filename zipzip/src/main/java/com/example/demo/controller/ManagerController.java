package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Graph;
import com.example.demo.dto.Item;
import com.example.demo.dto.Manager;
import com.example.demo.dto.Report;
import com.example.demo.dto.Response;
import com.example.demo.dto.Search;
import com.example.demo.dto.TodayCnt;
import com.example.demo.dto.User;
import com.example.demo.service.ManagerService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ManagerController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final ManagerService managerService;
	
	// 페이징 상수 정의
	private static final int LIST_COUNT = 10;   // 한 페이지에 보여질 리스트 수
	private static final int PAGE_COUNT = 5;    // 페이징 범위 수 
	
	// 관리자 테스트
//	@GetMapping("/manager")
//	public @ResponseBody String manager() {
//		return "관리자";
//	}
	
	// 관리자 홈
	@GetMapping("/manager/home")
	public String home(Model model) {
		log.info("ManagerController home");
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		System.out.println(context);
		System.out.println(authentication);
		
		TodayCnt today = managerService.todaySelectCnt();
		if(today!=null) {
			model.addAttribute("today",today);
		}
		
		String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Manager manager = managerService.findById(managerId);
		
		model.addAttribute("manager", manager);
		
		return "manager/home";
	}
	
	// 로그인
	@GetMapping("/manager/login")
	public String login() {
		return "manager/login";
	}
	
	//회원관리
	@RequestMapping("/manager/user")
	public String mUser (@RequestParam(value = "curPage", defaultValue = "1") long curPage,
						 @RequestParam(value = "searchType", defaultValue = "") String searchType,
						 @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
						 Model model) {
		Search search = new Search();
		if(!searchType.isEmpty() && !searchValue.isEmpty()) {
			log.info("searchValue : "+searchValue);
			log.info("searchType : "+searchType);
			search.setSearchType(searchType);
			search.setSearchValue(searchValue);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchValue", searchValue);
		}
		List<User> list = managerService.selectUserList(curPage, search);
		
		if(list!= null) {
			//log.info("list is not null");
			if(list.size()>0) {
				model.addAttribute("list", list);
				long totalCnt = managerService.selectUserTotal(search);
				Paging paging = new Paging("/Manager/user", totalCnt, LIST_COUNT, PAGE_COUNT, curPage,"curPage");
				model.addAttribute("paging",paging);
				//log.info("total: "+totalCnt);
			}
		}
		else {
			//log.info("list == null");
		}
		List<Graph> userList = managerService.userChange();
		
		List<String> monthList = new ArrayList<>();
		List<Long> totalList = new ArrayList<>();
		for(Graph a : userList) {
			String month = a.getRegDate();
			long total = a.getTotalCnt();
			//log.info(month);
			monthList.add(month);
			totalList.add(total);
		}
		
		model.addAttribute("month",monthList.toString());
		model.addAttribute("total",totalList);
		
		
		model.addAttribute("curPage", curPage);
		return "manager/user";
	}
	//중개인관리
	@RequestMapping("/manager/agent")
	public String agent (@RequestParam(value = "curPage", defaultValue = "1") long curPage,
						 @RequestParam(value = "searchType", defaultValue = "") String searchType,
						 @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
						 Model model) {
		Search search = new Search();
		if(!searchType.isEmpty() && !searchValue.isEmpty()) {
			
			log.info("searchType : "+searchType);
			search.setSearchType(searchType);
			if(searchType.equals("3")) {
				searchValue = searchValue.toUpperCase();
				log.info("searchValue 대문자: "+searchValue);
			}
			log.info("searchValue : "+searchValue);
			search.setSearchValue(searchValue);
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchValue", searchValue);
		}
		List<Agent> list = managerService.selectAgentList(curPage,search);
		
		if(list!= null) {
			//log.info("list is not null");
			if(list.size()>0) {
				model.addAttribute("list", list);
				long totalCnt = managerService.selectAgentTotal(search);
				Paging paging = new Paging("/Manager/user", totalCnt, LIST_COUNT, PAGE_COUNT, curPage,"curPage");
				model.addAttribute("paging",paging);
				//log.info("total: "+totalCnt);
			}
		}
		else {
			log.info("list == null");
		}
		
		return "manager/agent";
	}
	
	//신고관리
	@RequestMapping("/manager/report")
	public String report (@RequestParam(value = "curPage", defaultValue = "1") long curPage,
						  @RequestParam(value = "userId", defaultValue = "") String userId,
						  @RequestParam(value = "status", defaultValue = "") String status,
						  @RequestParam(value = "type", defaultValue = "") String type,
							Model model, HttpServletRequest request) {
		
		 
		Report report = new Report();
		
		if(!userId.isEmpty()) {
			report.setUserId(userId);
			log.info("userId"+userId);
			model.addAttribute("userId",userId);
		}
		else if(!status.isEmpty()) {
			report.setStatus(status);
			log.info("status"+status);
			model.addAttribute("status",status);
		}
		else if(!type.isEmpty()) {
			report.setReportType(type);
			log.info("type"+type);
			model.addAttribute("type",type);
		}
		
		List<Report> list = managerService.selectReportList(curPage, report);
		if(list != null) {
			if(list.size()>0) {
				long totalCnt = managerService.listTotalCnt(report);
				Paging paging = new Paging("/Manager/report", totalCnt, LIST_COUNT, PAGE_COUNT, curPage,"curPage");
				model.addAttribute("paging",paging);
			}
		}
		else {
			log.info("list == null");
		}
		model.addAttribute("list", list);
		model.addAttribute("curPage", curPage);
		return "manager/report";
	}
	
	@RequestMapping("/reportUpdate")
	@ResponseBody
	public int reportUpdate (@RequestParam(value="reportNum") long reportNum,
							 @RequestParam(value="status") String status) {
		int cnt = 0;
		Report report = new Report();
		if(reportNum != 0 && !status.isEmpty()) {
			report.setReportNum(reportNum);
			report.setStatus(status);
			
			cnt = managerService.updateReportStatus(report);
		}
		else {
			cnt = -1;
		}
		return cnt;
	}
	//유저업데이트
	@RequestMapping("/userUpdate")
	@ResponseBody
	public int userUpdate (@RequestParam(value="userId") String userId,
			 			   @RequestParam(value="status",  defaultValue = "") String status,
			 			   @RequestParam(value="emailAuth",  defaultValue = "") String email) {
		int cnt=0;
		User user = new User();
		if(!userId.isEmpty()) {
			log.info("userId:"+ userId);
			if(status != "" || email != "") {
				user.setUserId(userId);
				user.setStatus(status);
				user.setEmailAuth(email);
				log.info("status: "+status);
				log.info("email:"+email);
				
				cnt = managerService.updateUserStatus(user);
			}
		}
		
		return cnt;
	}	
	//중개사 업데이트
	@RequestMapping("/agentUpdate")
	@ResponseBody
	public int agentUpdate (@RequestParam(value="agentId") String agentId,
			 			   @RequestParam(value="status",  defaultValue = "") String status,
			 			   @RequestParam(value="right",  defaultValue = "") String right) {
		int cnt=0;
		Agent agent = new Agent();
		if(!agentId.isEmpty()) {
			log.info("userId:"+ agentId);
			if(status != "" || right != "") {
				agent.setAgentId(agentId);
				agent.setStatus(status);
				agent.setAgentRight(right);
				log.info("status: "+status);
				log.info("right:"+right);
				
				cnt = managerService.updateAgentStatus(agent);
			}
		}
		
		return cnt;
	}	
	//매물관리 페이지
	@RequestMapping("/manager/item")
	public String itemList (@RequestParam(value = "curPage", defaultValue = "1") long curPage,
						  	@RequestParam(value = "searchType", defaultValue = "") String searchType,
						  	@RequestParam(value = "searchValue", defaultValue = "") String searchValue,
							Model model) {
		
		Search search = new Search();
		

		if(!searchType.isEmpty() && !searchValue.isEmpty()) {
			search.setSearchType(searchType);
			search.setSearchValue(searchValue);
			log.info("searchValue : "+searchValue);
			log.info("searchType : "+searchType);
		}
		List<Item> list = managerService.selectItemList(curPage, search);

		if(list != null) {
			if(list.size()>0) {
				long totalCnt = managerService.selectItemTotal(search);
				Paging paging = new Paging("/Manager/report", totalCnt, LIST_COUNT, PAGE_COUNT, curPage,"curPage");
				model.addAttribute("paging",paging);
				
				for(int i=0;i<list.size();i++) {
					String tamp="";
					switch(list.get(i).getItemPtype()) {
						case "S" : tamp="매매"; break;
						case "Y" : tamp="전세"; break;
						case "M" : tamp="월세"; break;
					}
					
					list.get(i).setItemPtype(tamp);
					//보증금
					long tmp = list.get(i).getItemDeposit();
					if (tmp != 0) {
						String a="";
						if (tmp>=10000) {
							a = String.valueOf(tmp/10000)+"억 ";
							if (tmp % 10000 != 0) {
						        a += String.valueOf(tmp % 10000);
						    }
						}
						else {
							a = String.valueOf(tmp);
						}
						list.get(i).setTransD(a);

					}
					//월세
					tmp = list.get(i).getItemMonthPrice();
					if (tmp != 0) {
						String a="";
						if (tmp>=10000) {
							a = String.valueOf(tmp/10000)+"억 ";
							if (tmp % 10000 != 0) {
						        a += String.valueOf(tmp % 10000);
						    }
						}
						else {
							a = String.valueOf(tmp);
						}
						list.get(i).setTransM(a);
					}
				}
			}
		}
		
		else {
			log.info("list == null");
		}
		
		model.addAttribute("list", list);
		model.addAttribute("curPage", curPage);
		
		return "manager/item";
	}
	//매물 업데이트 
	@RequestMapping("/itemUpdate")
	@ResponseBody
	public int itemUpdate (@RequestParam(value="status") String status,
							@RequestParam(value="itemNum") String itemNum) {
		int cnt =-1;
		log.info("status: "+status);
		log.info("itemNum: "+itemNum);
		
		if(!status.isEmpty()) {
			if(itemNum != null && !itemNum.isEmpty()) {
				long num = Long.parseLong(itemNum);
				Item item = new Item();
				item.setItemNum(num);
				item.setItemStatus(status);
				log.info("status: "+status);
				log.info("item: "+num);
				cnt = managerService.updateItemStatus(item);
			}
			
			
			
			log.info("cnt : "+cnt);
		}
		
		return cnt;
	}
	// 리뷰 관리 페이지
	@RequestMapping("/manager/review")
	public String review(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		// 관리자 로그인 정보
		String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
		Manager manager = managerService.findById(managerId);
		
		List<AgentReview> agList = null;
		AgentReview agentReview = new AgentReview();  // 리뷰 
		Paging paging = null;
		
		// 현재 페이지
		String curPageString = request.getParameter("curPage");
		int curPage;
		if (curPageString != null && !curPageString.isEmpty()) {
			try {
				curPage = Integer.parseInt(curPageString);
			} catch (NumberFormatException e) {
				curPage = 1; // 기본값 
			}
		} else {
			curPage = 1; // 기본값 
		}
		
		// 검색 조건
		String searchType = request.getParameter("searchType");
		// 검색 값
		String searchValue = request.getParameter("searchValue"); 
		
		if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue)) {
			// 검색조건과 값이 있을 경우
			if(StringUtil.equals(searchType, "1")) {
				// 유저 아이디
				agentReview.setUserId(searchValue);
			} else if(StringUtil.equals(searchType, "2")) {
				// 중개인 아이디
				agentReview.setAgentId(searchValue);
			} else if(StringUtil.equals(searchType, "3")) {
				// 리뷰 내용
				agentReview.setReviewContent(searchValue);
			} else {
				searchType = "";
				searchValue = "";
			}
		}
		
		int totalCount  = managerService.reviewMgtCount(agentReview);
		
		if(totalCount > 0) {
			// 페이징 처리
			paging = new Paging("/manager/review", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			
			agentReview.setStartRow(paging.getStartRow());
			agentReview.setEndRow(paging.getEndRow());
			
			agList = managerService.reviewMgtList(agentReview);  // 리스트 담기
		}
		
		model.addAttribute("agList", agList);
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("manager", manager);
		model.addAttribute("paging", paging);
		
		return "manager/review";
	}

	// 리뷰 삭제 처리
	@RequestMapping("/manager/mgtDelete")
	@ResponseBody
	public Response<Object> mgtDelete(HttpServletRequest request, HttpServletResponse response){
		Response<Object> ajaxResponse = new Response<Object>();
		
		// 리뷰 글 번호 
		String reviewNumString = request.getParameter("reviewNum");
		long reviewNum;
		if (reviewNumString != null && !reviewNumString.isEmpty()) {
			try {
				reviewNum = Integer.parseInt(reviewNumString);
			} catch (NumberFormatException e) {
				reviewNum = 0; // 기본값 
			}
		} else {
			reviewNum = 0; // 기본값 
		}
				
		// log.info("reviewNum : " + reviewNum);  
				
		if(reviewNum > 0) {
			// 글번호 존재할 경우
			if(managerService.reviewMgtDelete(reviewNum) > 0) {
				// 리뷰 삭제 건 수가 있을 경우
				ajaxResponse.setResponse(0, "Success");
			} else {
				// 삭제 건 수가 없을 경우
				ajaxResponse.setResponse(500, "server error");
			}
		} else {
			// 글번호가 존재하지 않는 경우
			ajaxResponse.setResponse(400, "Bad Request");
		}
		
		return ajaxResponse;
	}
	
	// 리뷰내용 비공개/공개 처리
	@RequestMapping("/manager/hideReview")
	@ResponseBody
    public Response<Object> hideReview(HttpServletRequest request, HttpServletResponse response) {
		Response<Object> ajaxResponse = new Response<Object>();
		
		// 리뷰 글 번호 
		String reviewNumString = request.getParameter("reviewNum");
		long reviewNum;
		if (reviewNumString != null && !reviewNumString.isEmpty()) {
			try {
				reviewNum = Integer.parseInt(reviewNumString);
			} catch (NumberFormatException e) {
				reviewNum = 0; // 기본값 
			}
		} else {
			reviewNum = 0; // 기본값 
		}
		
		if(reviewNum > 0) {
			// 글번호 존재할 경우
			if(managerService.hideReview(reviewNum) > 0) {
				// 리뷰 삭제 건 수가 있을 경우
				ajaxResponse.setResponse(0, "Success");
			} else {
				// 삭제 건 수가 없을 경우
				ajaxResponse.setResponse(500, "server error");
			}
		} else {
			// 글번호가 존재하지 않는 경우
			ajaxResponse.setResponse(400, "Bad Request");
		}
        
        return ajaxResponse;
    }
	
    @RequestMapping("/manager/unhideReview")
    @ResponseBody
    public Response<Object> unhideReview(HttpServletRequest request, HttpServletResponse response) {
    	Response<Object> ajaxResponse = new Response<Object>();
    	
    	// 리뷰 글 번호 
    	String reviewNumString = request.getParameter("reviewNum");
    	long reviewNum;
    	if (reviewNumString != null && !reviewNumString.isEmpty()) {
    		try {
    			reviewNum = Integer.parseInt(reviewNumString);
    		} catch (NumberFormatException e) {
    			reviewNum = 0; // 기본값 
    		}
    	} else {
    		reviewNum = 0; // 기본값 
    	}
        
    	if(reviewNum > 0) {
			// 글번호 존재할 경우
			if(managerService.unhideReview(reviewNum) > 0) {
				// 리뷰 삭제 건 수가 있을 경우
				ajaxResponse.setResponse(0, "Success");
			} else {
				// 삭제 건 수가 없을 경우
				ajaxResponse.setResponse(500, "server error");
			}
		} else {
			// 글번호가 존재하지 않는 경우
			ajaxResponse.setResponse(400, "Bad Request");
		}
    	
        return ajaxResponse;
    }
}
