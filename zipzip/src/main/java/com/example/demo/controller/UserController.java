package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Community;
import com.example.demo.dto.CommunityComment;
import com.example.demo.dto.InterestItem;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemOption;
import com.example.demo.dto.Manager;
import com.example.demo.dto.Qna;
import com.example.demo.dto.Response;
import com.example.demo.dto.Search;
import com.example.demo.dto.Suggest;
import com.example.demo.dto.User;
import com.example.demo.service.AgentService;
import com.example.demo.service.FreeBoardService;
import com.example.demo.service.ItemService;
import com.example.demo.service.ManagerService;
import com.example.demo.service.QnaService;
import com.example.demo.service.ReviewBoardService;
import com.example.demo.service.SuggestService;
import com.example.demo.service.UserService;
import com.example.demo.util.Paging;
import com.example.demo.util.StringUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
   
	private final Logger log = LoggerFactory.getLogger(getClass());   
	private final UserService userService;
	private final AgentService agentService;
	private final ItemService itemService;
	private final FreeBoardService freeBoardService;
	private final SuggestService suggestService;
	private final QnaService qnaService;
	private final ReviewBoardService reviewBoardService;
	private final BCryptPasswordEncoder userPasswordEncoder;
	
	private static final int LIST_COUNT = 5;		//한 페이지 게시물 수
	private static final int PAGE_COUNT = 5;
   
	// 회원 테스트
//	@GetMapping("/user")
//	public @ResponseBody String user() {
//		return "회원";
//	}
	
	// 회원가입 페이지
	@GetMapping("/user/join")
   	public String join(User user) {
		return "user/join";
	}
   
	// 회원가입
	@PostMapping("/user/insert")
	public String insert(@Valid User user, Errors errors, Model model) throws Exception {
      
		// POST 요청시 넘어온 user 입력값에서 Validation에 걸리는 경우
		if (errors.hasErrors()) {
			// 회원가입 실패시 입력 데이터 유지
			model.addAttribute("user", user);         
         
			// 회원가입 실패시 메세지값을 모델에 매핑해서 전달
			Map<String, String> validateResult = userService.validateHandler(errors);
         
			// map.keyset() -> 모든 key값을 가져온다
			// 가져온 키를 반복문을 통해 키와 메세지 매핑
			for (String key : validateResult.keySet()) {
            
				// model.addAttribute("valid_userId", "아이디는 필수 입력사항 입니다");
				model.addAttribute(key, validateResult.get(key));
			}
         
			return "user/join";
		}
      
		userService.insert(user);
      
		log.info("insert success : " + user);
      
		return "redirect:/user/login";
      
	}
   
	// 로그인 페이지
	@GetMapping("/user/login")
	public String login() {
		return "user/login";
	}
   
	// 아이디 중복 검사
	@ResponseBody
	@PostMapping("/idCheck")
	public Map<Object, Object> idCheck(@RequestBody String userId) throws Exception {
      
		Map<Object, Object> map = new HashMap<Object, Object>();
		int result = 0;
      
		result = userService.idCheck(userId);
      
		map.put("check", result);
      
		return map;
      
	}
	
	// 아이디 비밀번호 찾기 페이지
	@GetMapping("/user/findIdPwd")
	public String findIdPwd() {		
		return "user/findIdPwd";
	}
	
	// 아이디 찾기
	@PostMapping("/user/findId")
	public ResponseEntity<String> findId(User user) {
		log.info("findId");
		
		User userId = userService.findId(user);
		
		System.out.println("search : " + user);
		System.out.println("result : " + userId);
		
		if (userId == null) {
			return ResponseEntity.ok("0");
		}
		
		else {
			return ResponseEntity.ok(userId.getUserId());
		}
	}
	
	// 비밀번호 찾기
	@PostMapping("/user/findPwd")
	public ResponseEntity<String> findPwd(User user) {
		log.info("findPwd");
		
		User userId = userService.findPwd(user);
		
		System.out.println("search : " + user);
		System.out.println("result : " + userId);
		
		if (userId == null) {
			return ResponseEntity.ok("0");
		}
		
		else {
			return ResponseEntity.ok(userId.getUserPwd());
		}
	}
	
	// 회원 수정 화면
	@GetMapping("/user/info")
	public String modifyUser(Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.userSelect(userId);
		
		model.addAttribute("user",user);
		
		return "user/info";
	}
	
	// 회원 수정
	@PostMapping("/user/userUpdate")
	public String userUpdate(User user, Model model) {
		
		userService.userUpdate(user);
		
		model.addAttribute("user", user);
		
		return "redirect:/user/info";
	}
	
	// 비밀번호 확인
	@PostMapping("/user/pwdCheck")
	@ResponseBody
	public int pwdCheck(@RequestBody String userPwd) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.userSelect(userId);
		
		String orgPwd = userService.pwdCheck(user.getUserId());
		
		System.out.println("user : " + user);
		System.out.println("chkPwd : " + userPwd);
		System.out.println("orgPwd : " + orgPwd);

		if (!userPasswordEncoder.matches(userPwd, orgPwd)) {
			return 0;
		}
		
		else {
			return 1;
		}
	}
	
	// 비밀번호 변경
	@PostMapping("/user/pwdUpdate")
	public String pwdUpdate(String userPwd1, HttpSession session) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		String hashPwd = userPasswordEncoder.encode(userPwd1);
		
		System.out.println("userId : " + userId);
		System.out.println("userPwd : " + userPwd1);
		System.out.println("hashPwd : " + hashPwd);
		
		userService.pwdUpdate(userId, hashPwd);
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 탈퇴
	@PostMapping("/user/updateStatus")
	public String updateStatus(User user, HttpSession session) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		user = userService.userSelect(userId);
		
		user.setStatus("Q");
		userService.updateStatus(user);
		session.invalidate();
		
		return "redirect:/user/logout";
	}
	
	// ============================================================= //
	
	//찜목록 리스트 화면
	@GetMapping("/user/interestItem")
	public String interestItem(Model model) throws Exception {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.userSelect(userId);
		
		List<InterestItem> interestItem = userService.interestItemSelect(userId);
		
		model.addAttribute("user",user); 
		model.addAttribute("interestItem",interestItem);
			
		return "/user/interestItem";
	}
	
	//찜목록 상세페이지
   @RequestMapping("/user/interestItemDetail")
   public String interestItemDetail (@RequestParam(value = "itemNum")long itemNum, Model model) {
         Item item = new Item();
         Search search = new Search();
         
         String userId = SecurityContextHolder.getContext().getAuthentication().getName();
         User user = userService.userSelect(userId);
           
         search.setItemNum(itemNum);
         search.setUserId(userId);    
            
         ItemOption io = itemService.selectItemOption(search);
      if(itemNum != 0)
      {
         item = itemService.selectInterestItemDetail(search);
         
         //보증금(매매가)
         long tmp = item.getItemDeposit();
         
         if( item.getItemPtype().equals("S")|| item.getItemPtype().equals("Y"))
         {
            if (tmp != 0) {
               String a="";
               if (tmp>=10000) 
               {
                  a = String.valueOf(tmp/10000)+"억 ";
                  if (tmp % 10000 != 0) 
                  {
                       a += String.valueOf(tmp % 10000)+"만원";
                  }
               }
               
               else { a=String.valueOf(tmp)+"만원"; }
               
               
               item.setTransD(a);
            }
         }
         else
         {
          //월세
            tmp = item.getItemMonthPrice();
            
            if (tmp != 0) 
            {
               String a="";
               if (tmp>=10000) 
               {
                  a = String.valueOf(tmp/10000)+"억";
                  if (tmp % 10000 != 0) 
                  {
                       a += String.valueOf(tmp % 10000)+"만원";
                  }
               }
             
               else { a=String.valueOf(tmp)+"만원"; } 
                
               item.setTransM(a);
            }
         }
           
      }
      
      String tamp="";
      switch(item.getItemPtype()) {
         case "S" : tamp="매매 "; break;
         case "Y" : tamp="전세 "; break;
         case "M" : tamp="월세 "; break;
      }
      item.setItemPtype(tamp);
      item.setINum(String.format("%010d", itemNum));
      
      model.addAttribute("io", io);
      model.addAttribute("itemNum",itemNum);
      model.addAttribute("userId",userId);
      model.addAttribute("item",item);
      model.addAttribute("user",user); 
      
      return "/user/interestItemDetail";
   }

	
	//찜목록 추가(ajax 통신)
	@PostMapping("/user/interestAddProc")
	@ResponseBody
	public ResponseEntity<String> interestAddProc(@RequestParam("itemNum") String itemNum ) {
		  
   	InterestItem interestItem = new InterestItem();
   	
   	String userId = SecurityContextHolder.getContext().getAuthentication().getName();
   	
   	long itemNum2 =  Long.parseLong(itemNum);
   	
   	interestItem.setUserId(userId);
   	interestItem.setItemNum(itemNum2);
   	
   	int cnt = userService.interestItemFindSelect(interestItem);
   	
   	if(cnt == 0)
   	{
   		int count = userService.interestItemInsert(interestItem);
       	
           if (count > 0) 
           {
               return ResponseEntity.ok("200");
           } 
           else 
           {
               return ResponseEntity.status(HttpStatus.OK).body("400");
           }
   	}
   	else
   	{
   		return ResponseEntity.ok("500");
   	}	
   	
   }
	
   //찜목록 삭제(ajax 통신)
   @PostMapping("/user/interestDeleteProc")
   @ResponseBody
   public ResponseEntity<String> interestDeleteProc(@RequestParam("itemNum") String itemNum ) {
 
   	InterestItem interestItem = new InterestItem();
   	
   	String userId = SecurityContextHolder.getContext().getAuthentication().getName();
   	
   	long itemNum2 =  Long.parseLong(itemNum);
   	
   	interestItem.setUserId(userId);
   	interestItem.setItemNum(itemNum2);
   	
   	int cnt = userService.interestItemFindSelect(interestItem);
   	
   	if(cnt >0)
   	{
   		int count = userService.interestItemDelete(interestItem);
           
           if (count > 0) 
           {
               return ResponseEntity.ok("200");
           } 
           else 
           {
               return ResponseEntity.status(HttpStatus.OK).body("404");
           }
   	}
   	else
   	{
   		return ResponseEntity.ok("500");
   	}

   }
   
   //나의 글 화면
   @RequestMapping("/user/myWriteList")
	public String myWriteList(@RequestParam(value = "curPage", defaultValue = "1") long curPage,@RequestParam(value = "curPage2", defaultValue = "1") long curPage2,@RequestParam(value = "checkRadio", defaultValue = "F") String checkRadio, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		  
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findById(userId);
		Agent agent = agentService.findById(userId);
	        
		String searchType = request.getParameter("searchType");
		String searchValue = request.getParameter("searchValue");
		String sortParam = request.getParameter("sort");	// java.lang.NumberFormatException: null 에러
		
		long sort = (sortParam != null && sortParam != "" && !sortParam.isEmpty() && sortParam != "undefined") ? Long.parseLong(sortParam) : 1L;
			
		if(checkRadio == null || checkRadio == "")
		{
			checkRadio = "T";
		}
		
		//나의글(자유게시판) 
		Community commu = new Community();
		List<Community> list = null;
		Paging paging = null;
		/* long totalCnt = 0; */
		
		commu.setUserId(userId);
		commu.setSearchType(searchType);
		commu.setSearchValue(searchValue);
		commu.setSort(sort);
		
		/* totalCnt = freeBoardService.myListTotalCnt(commu); */
				
		list = freeBoardService.myWriteBoardList(curPage, commu);
		paging = freeBoardService.myPagingService(curPage, commu);
		
		model.addAttribute("user", user);
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("curPage", curPage);
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		model.addAttribute("sort", sort);
		
		// 변수 확인 로그
		if(list != null && paging != null) {
			log.info("컨트롤러 리스트 객체 사이즈 - " + list.size());
			log.info("컨트롤러 페이징 값 StartPage / getEndPage / PrevBlockPage / CurPage - " + paging.getStartPage() + " / " + paging.getEndPage() + " / " + paging.getPrevBlockPage() + " / " + paging.getCurPage());
			log.info("컨트롤러 페이징 널? - " + paging);
		}

		log.info("컨트롤러 변수들 - " + searchType + " -- " + searchValue + " -- " + curPage);
		log.info("현재페이지 curPage - " + curPage);
		log.info("현재페이지 sort - " + sort);
		log.info("commu sort - " + commu.getSort());
				
		//나의글(건의사항)
		//24.05.21소스추가 시작
		String searchValue2 = request.getParameter("searchValue2");
		
		List<Suggest> suggestList = null;
		Suggest suggest = new Suggest();
		suggest.setUserId(userId);	
		Paging paging2 = null;
		long totalCnt2 = 0;
			
		if(!StringUtil.isEmpty(searchValue2)){
			suggest.setSearchValue(searchValue2);	
		}		
		
		totalCnt2 = suggestService.myListCnt(suggest); 	
		
		if(totalCnt2 > 0) {
			paging2 = new Paging("/user/myWriteList",totalCnt2,LIST_COUNT,PAGE_COUNT,curPage2,"curPage");
			suggest.setStartRow(paging2.getStartRow());
			suggest.setEndRow(paging2.getEndRow());
			
			suggestList = suggestService.myWriteSelectAll(suggest);
			
		}
		model.addAttribute("searchValue2",searchValue2);
		model.addAttribute("curPage2",curPage2);
		model.addAttribute("paging2",paging2);
		model.addAttribute("totalCnt2",totalCnt2);
		//24.05.21소스추가 종료
		System.out.println("data:"+checkRadio);
		
		model.addAttribute("agent", agent);
		model.addAttribute("suggestList", suggestList);
		model.addAttribute("checkRadio", checkRadio);
		
		// 나의 글(리뷰게시판)
	      AgentReview agentReview = new AgentReview();
	      List<AgentReview> myReview = null;
	      
	      agentReview.setUserId(userId);  // 로그인한 유저 세팅
	      
	      // 통합검색 및 검색값 
	      String searchOption = request.getParameter("searchOption");
	      String searchContent = request.getParameter("searchContent");
	      
	      if(!StringUtil.isEmpty(searchOption) && !StringUtil.isEmpty(searchContent)) {
	         // 검색조건과 값이 있을 경우
	         if(StringUtil.equals(searchOption, "1")) {
	            // 중개인
	            agentReview.setAgentName(searchContent);
	         } else if(StringUtil.equals(searchOption, "2")) {
	            // 리뷰내용
	            agentReview.setReviewContent(searchContent);
	         } else {
	            searchOption = "";
	            searchContent = "";
	         }
	      }
	      
	      int totalCount = reviewBoardService.myReviewBoardCount(agentReview);  // 로그인한 유저가 작성한 리뷰 리스트 건 수 담기

	      log.info("totalCount .. : " + totalCount); 
	      
	      if(totalCount > 0) {
	         // 페이징 처리 
	         paging = new Paging("/user/myWriteList", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
	                  
	         agentReview.setStartRow(paging.getStartRow()); 
	         agentReview.setEndRow(paging.getEndRow());

	         // log.info("totalCount .. " + totalCount);
	         
	         myReview = reviewBoardService.myReviewBoard(agentReview);
	      }
	      
	      model.addAttribute("searchOption", searchOption);
	      model.addAttribute("searchContent", searchContent);
	      model.addAttribute("myReview", myReview);
	      model.addAttribute("paging", paging);

		
		return "/user/myWriteList";
	}
   
   //나의글(자유게시판) 상세 화면
	   @RequestMapping("/user/myFreeBoardDetail")
	   public String myFreeBoardDetail(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
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
	      
	      return "/user/myFreeBoardDetail";
	   }
	   
	 //나의글(자유게시판)게시글 수정 화면
	   @RequestMapping("/user/myFreeBoardUpdate")
	   public String myFreeBoardUpdate(@RequestParam(value = "communityNum", defaultValue = "0") long communityNum, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
	      
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
	      
	      return "/user/myFreeBoardUpdate";
	   }
		   
	   //나의글(건의사항)상세 화면
	   @RequestMapping("/user/mySuggestDetail")
	   public String selectOne(@RequestParam(value = "suggestNum", defaultValue = "") long suggestNum, Model model,HttpServletRequest request, HttpServletResponse response) {
	      
	      // 조회	
	      suggestService.updateHit(suggestNum);
	      
	      String checkRadio = request.getParameter("checkRadio");
	      String searchValue2 = request.getParameter("searchValue2");
	      
	      if(checkRadio == null || checkRadio == "")
	      {
			checkRadio = "S";
	      }
	      
	      // 상세
	      Suggest suggest = suggestService.selectOne(suggestNum);
	      model.addAttribute("suggest", suggest);
	      model.addAttribute("checkRadio", checkRadio);
	      model.addAttribute("searchValue2", searchValue2);
	      model.addAttribute("suggestNum", suggestNum);
	      
	      System.out.println("checkRadio : " + checkRadio);
	      System.out.println("suggest : " + suggest);
	            
	      return "/user/mySuggestDetail";
	   }
  
	   //나의글(건의사항)게시물 삭제
	   @RequestMapping("/user/suggestDelete")
	   public String delete(@RequestParam(value = "suggestNum", defaultValue = "") long suggestNum, @RequestParam(value = "checkRadio", defaultValue = "T") String checkRadio, ModelMap model,HttpServletRequest request, HttpServletResponse response) {
		   
		   if(checkRadio == null || checkRadio == "")
			{
				checkRadio = "S";
			}
		   
	      suggestService.delete(suggestNum);
	      
	      model.addAttribute("checkRadio", checkRadio);
	      
	      return "redirect:/user/myWriteList?checkRadio=S"; 
	   }
	   
	   //나의글(건의사항)게시물 수정 화면
	   @RequestMapping("/user/mySuggestUpdate")
	   public String update(@RequestParam(value = "suggestNum", defaultValue = "") long suggestNum ,@RequestParam(value = "checkRadio", defaultValue = "T") String checkRadio,Model model,HttpServletRequest request, HttpServletResponse response) {
	      
	      String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(userId);
	      Agent agent = agentService.findById(userId);
	      String searchValue2 = request.getParameter("searchValue2");
	      
	      if(checkRadio == null || checkRadio == "")
			{
				checkRadio = "S";
			}
	      
	      model.addAttribute("user", user);
	      model.addAttribute("agent", agent);
	      model.addAttribute("checkRadio",checkRadio);
	      model.addAttribute("searchValue2",searchValue2);

	      Suggest suggest = suggestService.selectOne(suggestNum);
	      model.addAttribute("suggest", suggest);
	      model.addAttribute("suggestNum", suggestNum);
	      
	      return "/user/mySuggestUpdate";
	   }
	   
	   
	   //나의글(건의사항)게시물 수정
	   @RequestMapping("/user/mySuggestUpdateProc")
	   public String updateProc(Suggest suggest,@RequestParam(value = "suggestNum", defaultValue = "") long suggestNum ,@RequestParam(value = "checkRadio", defaultValue = "T") String checkRadio,Model model,HttpServletRequest request, HttpServletResponse response) {
	      
	      String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	      User user = userService.findById(userId);
	      Agent agent = agentService.findById(userId);
	      String searchValue2 = request.getParameter("searchValue2");
	      
	      if(checkRadio == null || checkRadio == "")
			{
				checkRadio = "S";
			}
	      
	      model.addAttribute("user", user);
	      model.addAttribute("agent", agent);
	      model.addAttribute("checkRadio",checkRadio);
	      model.addAttribute("searchValue2",searchValue2);

	      Suggest suggest2 = suggestService.selectOne(suggestNum);
	      model.addAttribute("suggest", suggest);
	      
	      suggestService.update(suggest); 
	      Suggest detail = suggestService.selectOne(suggest.getSuggestNum());
	      model.addAttribute("suggest", detail);
	      model.addAttribute("suggestNum", suggestNum);
	      
	      return "/user/mySuggestDetail";
	   }
	   
	   //나의 문의 화면
	   @RequestMapping("/user/myQna")
		public String myQna(@RequestParam(value = "curPage", defaultValue = "1") long curPage, HttpServletRequest request, HttpServletResponse response, Model model) {
			
		   String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findById(userId);
			Agent agent = agentService.findById(userId);
			
			String searchValue = request.getParameter("searchValue");
			List<Qna> qnaList = null;
			Qna qna = new Qna();
			qna.setUserId(userId);
			Paging paging = null;
			long totalCnt = 0;
			
			System.out.println("searchValue : " + searchValue);
			System.out.println("curPage : " + curPage);
			
			if (!StringUtil.isEmpty(searchValue)) {
				qna.setSearchValue(searchValue);
			}
			
			totalCnt = qnaService.myQnaListCnt(qna);
			System.out.println("totalCnt : " + totalCnt);
			
			if (totalCnt > 0) {
				paging = new Paging("/user/myQna", totalCnt, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
				qna.setStartRow(paging.getStartRow());
				qna.setEndRow(paging.getEndRow());
				
				qnaList = qnaService.myQnaSelectAll(qna);
				System.out.println("qnaList : " + qnaList);
				System.out.println("qna : " + qna);
			}
			
			model.addAttribute("searchValue", searchValue);
			model.addAttribute("curPage", curPage);
			model.addAttribute("paging", paging);
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("qnaList", qnaList);
			
			return "user/myQna";
		}
	   
	   //나의 문의 상세 화면
	   @RequestMapping("/user/myQna/{qnaNum}")
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
			
			return "user/myQnaDetail";
		}
	   
	   //나의 문의 게시글 수정
	   @GetMapping("/user/myQnaUpdate/{qnaNum}")
		public String update(@PathVariable("qnaNum") long qnaNum, Model model) {
			
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findById(userId);
			Agent agent = agentService.findById(userId);
			
			model.addAttribute("user", user);
			model.addAttribute("agent", agent);
			
			Qna qna = qnaService.selectOne(qnaNum);
			model.addAttribute("qna", qna);
			
			return "user/myQnaUpdate";
		}
		
		@PostMapping("/user/myQnaUpdate/{qnaNum}")
		public String update(Qna qna, Model model) {
			
			qnaService.update(qna);
			Qna detail = qnaService.selectOne(qna.getQnaNum());
			model.addAttribute("qna", detail);
			
			return "user/myQnaDetail";
		}
		
		//나의 문의 게시글 삭제
		@GetMapping("/user/myQnaDelete/{qnaNum}")
		public String delete(@PathVariable("qnaNum") long qnaNum) {
			
			qnaService.delete(qnaNum);
			
			return "redirect:/user/myQna";
		}
	   
		
		   // 나의 글(리뷰게시판) 상세 화면 
		   @RequestMapping("/user/myReviewBoardDetail")
		   public String myReviewBoardDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
			   String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			   User user = userService.findById(userId);

			   AgentReview agentReview = new AgentReview();
			   
			   // 체크라디오 (리뷰게시판 => 'R')
			   String checkRadio = request.getParameter("checkRadio");
			   
			   // 통합검색 및 검색값 
			   String searchOption = request.getParameter("searchOption");
			   String searchContent = request.getParameter("searchContent");

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
				   // 리뷰가 존재할 경우
				   agentReview = reviewBoardService.myReviewBoardView(reviewNum);
			   }
			   
			   model.addAttribute("user", user);
			   model.addAttribute("searchOption", searchOption);
			   model.addAttribute("searchContent", searchContent);	  
			   model.addAttribute("curPage", curPage);
			   model.addAttribute("reviewNum", reviewNum);
			   model.addAttribute("agentReview", agentReview);
			   model.addAttribute("checkRadio", checkRadio);
			   model.addAttribute("userId", userId);
			   
			   return "user/myReviewBoardDetail";
		   }
		   
		   // 나의 글(리뷰게시판) 1건 삭제
		   @RequestMapping("/user/reviewDelete")
		   @ResponseBody
			public Response<Object> reviewDelete(HttpServletRequest request, HttpServletResponse response){
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
					if(reviewBoardService.reviewDelete(reviewNum) > 0) {
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