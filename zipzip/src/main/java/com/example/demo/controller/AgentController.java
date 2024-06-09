package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.Agent;
import com.example.demo.dto.AgentReview;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemDetail;
import com.example.demo.dto.ItemFile;
import com.example.demo.dto.ItemOption;
import com.example.demo.dto.Manager;
import com.example.demo.dto.Search;
import com.example.demo.dto.User;
import com.example.demo.service.AgentService;
import com.example.demo.service.ItemFileService;
import com.example.demo.service.ItemService;
import com.example.demo.util.PhotoUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AgentController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final AgentService agentService;
	private final PhotoUtil photoUtil;
	private final ItemFileService itemFileService;
	private final ItemService itemService;
	private final BCryptPasswordEncoder agentPasswordEncoder;
	
	// 중개인 테스트
//	@GetMapping("/agent")
//	public @ResponseBody String agent() {
//		return "중개인";
//	}
	
	// 중개인 홈
	@GetMapping("/agent/home")
	public String home(Model model) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(userId);

		model.addAttribute("agent", agent);
		
		return "agent/home";
	}
	
	// 회원가입 페이지
	@GetMapping("/agent/join")
	public String join(Agent agent) {
		return "agent/join";
	}
	
	// 회원가입
	@PostMapping("/agent/insert")
	public String insert(@Valid Agent agent, Errors errors, Model model) throws Exception {
		
		// POST 요청시 넘어온 agent 입력값에서 Validation에 걸리는 경우
		if (errors.hasErrors()) {
			// 회원가입 실패시 입력 데이터 유지
			model.addAttribute("agnet", agent);
			
			//회원가입 실패시 메세지값을 모델에 매핑해서 전달
			Map<String, String> validateResult = agentService.validateHandler(errors);
			
			// map.keyset() -> 모든 key값을 가져온다
			// 가져온 키를 반복문을 통해 키와 메세지 매핑
			for (String key : validateResult.keySet()) {
				// model.addAttribute("valid_agentId", "아이디는 필수 입력사항 입니다");
				model.addAttribute(key, validateResult.get(key));
			}
			
			return "agent/join";
		}
		
		agentService.insert(agent);
		
		log.info("insert success : " + agent);
		
		return "redirect:/agent/login";
		
	}
	
	// 로그인 페이지
	@GetMapping("/agent/login")
	public String login() {
		return "agent/login";
	}
	
	// 아이디 중복 확인
	@ResponseBody
	@PostMapping("/idCheckAg")
	public Map<Object, Object> idCheckAg(@RequestBody String agentId) throws Exception {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		int result = 0;
		
		result = agentService.idCheckAg(agentId);
		
		map.put("check", result);
		
		return map;
	}
	
	// 중개인 수정 페이지
	@GetMapping("/agent/info")
	public String info(Model model) {
		
		String agentId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.agentSelect(agentId);
		
		System.out.println(agent);
		
		model.addAttribute("agent", agent);
		
		return "agent/info";
	}
	
	// 중개인 수정
	@PostMapping("/agent/agentUpdate")
	public String agentUpdate(Agent agent, Model model) {
		
		agentService.agentUpdate(agent);
		
		model.addAttribute("agent", agent);
		
		return "redirect:/agent/info";
	}
	
	// 프로필 수정
	@PostMapping("/agent/updateProfile")
	public String updateProfile(Agent agent, MultipartFile file) throws Exception {
		
		String agentId = SecurityContextHolder.getContext().getAuthentication().getName();
		agent = agentService.agentSelect(agentId);
		
		agentService.updateProfile(agent, file);
		System.out.println(agent);
		System.out.println(file);
		
		return "redirect:/agent/info";
	}
	
	// 비밀번호 확인
	@PostMapping("/agent/pwdCheck")
	@ResponseBody
	public int pwdCheck(@RequestBody String agentPwd) {
		String agentId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.agentSelect(agentId);
		String orgPwd = agentService.pwdCheck(agentId);
		
		System.out.println("agent : " + agent);
		System.out.println("chkPwd : " + agentPwd);
		System.out.println("orgPwd : " + orgPwd);
		
		if (!agentPasswordEncoder.matches(agentPwd, orgPwd)) {
			return 0;
		}
		return 1;
	}
	
	// 비밀번호 변경
	@PostMapping("/agent/pwdUpdate")
	public String pudUpdate(String agentPwd1, HttpSession session) {
		String agentId = SecurityContextHolder.getContext().getAuthentication().getName();
		String hashPwd = agentPasswordEncoder.encode(agentPwd1);
		
		System.out.println("agentId : " + agentId);
		System.out.println("agenPwd : " + agentPwd1);
		System.out.println("hashPwd : " + hashPwd);
		
		agentService.pwdUpdate(agentId, hashPwd);
		session.invalidate();
		
		return "redirect:/agent/login";
	}
	
	// 탈퇴
	@PostMapping("/agent/updateStatus")
	public String updateStatus(Agent agent, HttpSession session) {
		String agentId = SecurityContextHolder.getContext().getAuthentication().getName();
		agent = agentService.agentSelect(agentId);
		
		agent.setStatus("Q");
		agentService.updateStatus(agent);
		session.invalidate();
		
		return "redirect:/agent/logout";
	}
	
	//============================================================================================================ YH 
	
	// 매물 등록 화면
	@RequestMapping("/agent/itemReg")
	public String agentItemPost(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(loginId);
		
		model.addAttribute("agent", agent);
		
		
		return "agent/itemReg.html";
	}
	
	// 매물 등록 Proc
	@PostMapping("/agent/itemRegProc")
	@ResponseBody
	public int itemRegProc(@RequestParam(value = "itemDeposit", required=false, defaultValue = "0") long itemDeposit,
							@RequestParam(value = "itemMaintainPrice", required=false, defaultValue = "0") int itemMaintainPrice,
							@RequestParam(value = "itemMonthPrice", required=false, defaultValue = "0") int itemMonthPrice,
							@RequestParam(value = "itemArea", required = false, defaultValue = "0.0") Double itemArea,
							@RequestParam(value = "itemFloor", required=false, defaultValue = "0") int itemFloor,
							@RequestParam(value = "itemMoveAvbl", defaultValue = "N") String itemMoveAvbl,
							@RequestParam(value = "sink", defaultValue = "N") String sink,
							@RequestParam(value = "air", defaultValue = "N") String air,
							@RequestParam(value = "laundry", defaultValue = "N") String laundry,
							@RequestParam(value = "refrigerator", defaultValue = "N") String refrigerator,
							@RequestParam(value = "closet", defaultValue = "N") String closet,
							@RequestParam(value = "gasrange", defaultValue = "N") String gasrange,
							@RequestParam(value = "induction", defaultValue = "N") String induction,
							@RequestParam(value = "micro", defaultValue = "N") String micro,
							@RequestParam(value = "desk", defaultValue = "N") String desk,
							@RequestParam(value = "bed", defaultValue = "N") String bed,
							@RequestParam(value = "entrance", defaultValue = "N") String entrance,
							@RequestParam(value = "cameratv", defaultValue = "N") String cameratv,
							@RequestParam(value = "intercom", defaultValue = "N") String intercom,
							ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response) {
		
		int resCode = 0;
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(loginId);
		
		//Item 변수
		String itemName = request.getParameter("itemName");
		String itemAddress = request.getParameter("itemAddress");		
		String itemAddressDetail = request.getParameter("itemAddressDetail");
		String itemItype = request.getParameter("itemItype");
		String itemPtype = request.getParameter("itemPtype");			
		//String itemStatus = request.getParameter("");
		//String recommnedItem = request.getParameter("");
		
		//ItemDetail 변수
		String itemIntro = request.getParameter("itemIntro");	
		String itemIntroDetail = request.getParameter("itemIntroDetail");
		String itemParking = request.getParameter("itemParking");		
		String itemBuildDate = request.getParameter("itemBuildDate");
		String itemElevator = request.getParameter("itemElevator");		
		String itemPet = request.getParameter("itemPet");			
		String itemDirection = request.getParameter("itemDirection");
		String itemKind = request.getParameter("itemKind");
		String itemMoveDate = request.getParameter("itemMoveDate");	
		
		//옵션 변수(13개)
		
		//사진 파일
		List<MultipartFile> fileList = request.getFiles("filePlus");
		
		
		if(agent != null) {
			Item item = new Item();
			ItemDetail itemDetail = new ItemDetail();
			ItemOption itemOption = new ItemOption();
			ItemFile itemFile = new ItemFile();
			
			item.setAgentId(agent.getAgentId());
			item.setItemName(itemName);
			item.setItemAddress(itemAddress);
			item.setItemAddressDetail(itemAddressDetail);
			item.setItemPtype(itemPtype);
			item.setItemItype(itemItype);
			item.setItemStatus("Y");
			item.setRecommendItem("N");
			
			if(agentService.itemInsert(item) > 0) {
				long itemNumSeq = item.getItemNum();
				
				itemDetail.setItemNum(itemNumSeq);
				itemDetail.setItemIntro(itemIntro);
				itemDetail.setItemIntroDetail(itemIntroDetail);
				itemDetail.setItemDeposit(itemDeposit);
				itemDetail.setItemMaintainPrice(itemMaintainPrice);
				itemDetail.setItemMonthPrice(itemMonthPrice);
				itemDetail.setItemArea(itemArea);
				itemDetail.setItemParking(itemParking);
				itemDetail.setItemBuildDate(itemBuildDate);
				itemDetail.setItemElevator(itemElevator);
				itemDetail.setItemPet(itemPet);
				itemDetail.setItemDirection(itemDirection);
				itemDetail.setItemKind(itemKind);
				itemDetail.setItemFloor(itemFloor);
				itemDetail.setItemMoveAvbl(itemMoveAvbl);
				itemDetail.setItemMoveDate(itemMoveDate);
				
				if(agentService.itemDetailInsert(itemDetail) > 0) {
					
					itemOption.setItemNum(itemNumSeq);
					itemOption.setSink(sink);
					itemOption.setAir(air);
					itemOption.setLaundry(laundry);
					itemOption.setRefrigerator(refrigerator);
					itemOption.setCloset(closet);
					itemOption.setGasrange(gasrange);
					itemOption.setInduction(induction);
					itemOption.setMicro(micro);
					itemOption.setDesk(desk);
					itemOption.setBed(bed);
					itemOption.setEntrance(entrance);
					itemOption.setCameratv(cameratv);
					itemOption.setIntercom(intercom);
					
					if(agentService.itemOptionInsert(itemOption) > 0) {
						int max = 1;
				        for (MultipartFile file : fileList) {
				            if (!file.isEmpty()) {
				            	
				            	String orgFileName = file.getOriginalFilename();
				            	String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
				            	String fileName = itemNumSeq + "_" + max + ext;
				            	
				            	itemFile.setItemNum(itemNumSeq);
				            	itemFile.setFileIdx(max);
				            	itemFile.setFileName(fileName);
				            	itemFile.setFileOrg(orgFileName);
				            	
				            	max += 1;
				            	
				            	if(agentService.itemFileInsert(itemFile) > 0) {
				            		if(photoUtil.itemFileUpload(fileName, file) >0) {
				            			resCode = 200;
				            		}else {
				            			resCode = 505;	//itemFile upload Error
				            		}
				            	}else {
				            		resCode = 504;	//itemFile insert Error
				            	}
				            } else {
				                log.warn("업로드된 파일이 비어 있습니다.");
				            }
				        }
					}else {
						resCode = 503;	//itemOption insert Error
					}
				}else {
					resCode = 502;	//itemDetail insert Error
				}
			}else {
				resCode = 501;	//item insert Error
			}
		}else {
			resCode = 404;
		}
		
		
		
		
		model.addAttribute("agent", agent);
		
		
		log.info("login id -- " + loginId);
		
		log.info("============== item ===============");
		log.info("itemName -- " + itemName);
		log.info("itemAddress -- " + itemAddress);
		log.info("itemAddressDetail -- " + itemAddressDetail);
		log.info("itemItype -- " + itemItype);
		log.info("itemPtype -- " + itemPtype);
		
		log.info("============== itemDetail ===============");
		log.info("itemName -- " + itemName);
		log.info("itemIntro -- " + itemIntro);
		log.info("itemIntroDetail -- " + itemIntroDetail);
		log.info("itemDeposit -- " + itemDeposit);
		log.info("itemMaintainPrice -- " + itemMaintainPrice);
		log.info("itemMonthPrice -- " + itemMonthPrice);
		log.info("itemArea -- " + itemArea);
		log.info("itemParking -- " + itemParking);
		log.info("itemBuildDate -- " + itemBuildDate);
		log.info("itemElevator -- " + itemElevator);
		log.info("itemPet -- " + itemPet);
		log.info("itemDirection -- " + itemDirection);
		log.info("itemKind -- " + itemKind);
		log.info("itemFloor -- " + itemFloor);
		log.info("itemMoveAvbl -- " + itemMoveAvbl);
		log.info("itemMoveDate -- " + itemMoveDate);		
		
		log.info("============== Option ===============");
		log.info("옵션 sink -- " + sink);
		log.info("옵션 air -- " + air);
		log.info("옵션 laundry -- " + laundry);
		log.info("옵션 refrigerator -- " + refrigerator);
		
		log.info("============== File ===============");
		log.info("파일??");
		log.info("파일 list 사이즈 - " + fileList.size());
		
        for (MultipartFile file : fileList) {
            if (!file.isEmpty()) {
                log.info("파일 이름 -- " +  file.getOriginalFilename());
                log.info("파일 크기 -- bytes" + file.getSize());
                log.info("컨텐츠 타입 -- " + file.getContentType());
            } else {
                log.warn("업로드된 파일이 비어 있습니다.");
            }
        }
		
		return resCode;
	}
	
	
	
	// 매물 리스트 화면
	@RequestMapping("/agent/itemList")
	public String itemList(@RequestParam(value = "sortItype", required=false, defaultValue = "") String sortItype,
						   @RequestParam(value = "sortPtype", required=false, defaultValue = "") String sortPtype,
							ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(loginId);
		
		List<Item> list = null;
		Item item = null;
		
		if(agent != null) {
			item = new Item();
			item.setAgentId(agent.getAgentId());
			item.setItemItype(sortItype);
			item.setItemPtype(sortPtype);
			
			list = agentService.myItemRegList(item);
			
		}else {
			log.debug("Not Existed Agent");
		}
		
		log.info("sortItype -- " + sortItype);
		log.info("sortPtype -- " + sortPtype);
		
		model.addAttribute("sortPtype", sortPtype);
		model.addAttribute("sortItype", sortItype);
		model.addAttribute("list", list);
		
		return "agent/itemList";
	}
	
	// 매물삭제 Proc
	@RequestMapping("/agent/itemDeleteProc")
	@ResponseBody
	public int itemDeleteProc(@RequestParam(value = "itemNum", required=false, defaultValue = "0") long itemNum, HttpServletRequest request, HttpServletResponse response) {
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(loginId);
		
		int resCode = 0;
		List<ItemFile> fileList = null;
		Item item = agentService.itemSelect(itemNum);
		
		
		if(itemNum > 0) {
			if(item != null && agent != null) {
				fileList = itemFileService.findFile(itemNum);
				try {
					if(agentService.itemDelete(itemNum) > 0) {
						
						//프로젝트내 사진파일 삭제
						if(fileList != null) {
							log.info("[itemDeleteProc] 여기는 오나1111");
							for(int i = 0; i < fileList.size(); i++) {
								log.info("[itemDeleteProc] 여기는 오나2222");
								String fileName = fileList.get(i).getFileName();
								log.info("[itemDeleteProc] 여기는 오나3333");
								photoUtil.itemFileDelete(fileName);
								log.info("[itemDeleteProc] 여기는 오나4444");
							}
						}
						
						resCode = 200;

					}else {
						resCode = 500;
					}
				}catch(Exception e) {
					resCode = 500;
					log.debug("[Agent Comtroller Error] itemDeleteProc -- ", e);
				}
			}else {
				resCode = 404;
			}
		}else{
			resCode = 400;
		}
		
		log.info("[itemDeleteProc] fileList.size() -- " + fileList.size());
		log.info("[itemDeleteProc] fileList -- " + fileList);
		log.info("[itemDeleteProc] itemNum -- " + itemNum);
		log.info("[itemDeleteProc] resCode -- " + resCode);
		
		return resCode;
	}
	
	// item data
	@RequestMapping("/getItemDetail")
	@ResponseBody
	public Item itemDetail(@RequestParam(value = "itemNum", required=false, defaultValue = "0") long itemNum, 
							ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(loginId);
		
		Item itemData = null;
		List<ItemFile> fileList = null;

		if(agent != null && itemNum > 0) {
			
			itemData = agentService.itemSelect(itemNum);
			fileList = itemFileService.findFile(itemNum);
			
			itemData.setItemFileName(fileList);
		}
		
		
		
		model.addAttribute("itemData", itemData);
		
		log.info("itemNum -- " + itemNum);
		//log.info("item itemName -- " + itemData.getItemName());
		
		return itemData;
	}
	
	
	// 매물 수정 화면
	@RequestMapping("/agent/itemUpdate")
	public String itemUpdate(@RequestParam(value = "itemNum", defaultValue = "0") long itemNum, 
							 ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = null;
		
		Item item = null;
		ItemOption itemOption = null;
		List<ItemFile> itemFile = null;
		Search search = new Search();
		
		if(loginId != null) {		
			agent = agentService.findById(loginId);
		}
		
		if(agent != null && "Y".equals(agent.getStatus()) && itemNum > 0) {
			search.setItemNum(itemNum);
			
			log.info("타나");
			item = agentService.itemSelect(itemNum);
			itemOption = itemService.selectItemOption(search);
			
			itemFile = itemFileService.findFile(itemNum);
		}
		
		
		model.addAttribute("itemNum", itemNum);
		model.addAttribute("itemFile", itemFile);
		model.addAttribute("itemOption", itemOption);
		model.addAttribute("item", item);
		model.addAttribute("agent", agent);
		
		log.info("item.getItemMoveDate -- ", item.getItemMoveDate());
		log.info("item.itemBuildDate -- ", item.getItemBuildDate());
		log.info("itemFile -- " + itemFile);
		log.info("agent -- " + agent);
		log.info("agent.getStatus() -- " + agent.getStatus());
		log.info("item -- " + item.getItemName());
		log.info("매물 번호 -- " + itemNum);
		
		return "agent/itemUpdate";
	}
	
	
	// 매물 수정 Proc
	@PostMapping("/agent/itemUpdateProc")
	@ResponseBody
	public int itemUpdateProc(@RequestParam(value = "itemNum", required=false, defaultValue = "0") long itemNum,
							@RequestParam(value = "itemDeposit", required=false, defaultValue = "0") long itemDeposit,
							@RequestParam(value = "itemMaintainPrice", required=false, defaultValue = "0") int itemMaintainPrice,
							@RequestParam(value = "itemMonthPrice", required=false, defaultValue = "0") int itemMonthPrice,
							@RequestParam(value = "itemArea", required = false, defaultValue = "0.0") Double itemArea,
							@RequestParam(value = "itemFloor", required=false, defaultValue = "0") int itemFloor,
							@RequestParam(value = "itemMoveAvbl", defaultValue = "N") String itemMoveAvbl,
							@RequestParam(value = "sink", defaultValue = "N") String sink,
							@RequestParam(value = "air", defaultValue = "N") String air,
							@RequestParam(value = "laundry", defaultValue = "N") String laundry,
							@RequestParam(value = "refrigerator", defaultValue = "N") String refrigerator,
							@RequestParam(value = "closet", defaultValue = "N") String closet,
							@RequestParam(value = "gasrange", defaultValue = "N") String gasrange,
							@RequestParam(value = "induction", defaultValue = "N") String induction,
							@RequestParam(value = "micro", defaultValue = "N") String micro,
							@RequestParam(value = "desk", defaultValue = "N") String desk,
							@RequestParam(value = "bed", defaultValue = "N") String bed,
							@RequestParam(value = "entrance", defaultValue = "N") String entrance,
							@RequestParam(value = "cameratv", defaultValue = "N") String cameratv,
							@RequestParam(value = "intercom", defaultValue = "N") String intercom,
							ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response) {
		
		int resCode = 0;
		
		String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(loginId);
		
		//Item 변수
		String itemName = request.getParameter("itemName");
		String itemAddress = request.getParameter("itemAddress");		
		String itemAddressDetail = request.getParameter("itemAddressDetail");
		String itemItype = request.getParameter("itemItype");
		String itemPtype = request.getParameter("itemPtype");			
		//String itemStatus = request.getParameter("");
		//String recommnedItem = request.getParameter("");
		
		//ItemDetail 변수
		String itemIntro = request.getParameter("itemIntro");	
		String itemIntroDetail = request.getParameter("itemIntroDetail");
		String itemParking = request.getParameter("itemParking");		
		String itemBuildDate = request.getParameter("itemBuildDate");
		String itemElevator = request.getParameter("itemElevator");		
		String itemPet = request.getParameter("itemPet");			
		String itemDirection = request.getParameter("itemDirection");
		String itemKind = request.getParameter("itemKind");
		String itemMoveDate = request.getParameter("itemMoveDate");	
		
		//옵션 변수(13개)
		
		//사진 파일
		List<MultipartFile> fileList = request.getFiles("filePlus");
		List<ItemFile> curFileList = null;
		
		boolean filesEmptyChk = true;
		//매물파일 비었는지 확인 (파일 있으면 true)
		boolean fileIsNotEmpty = fileList.stream().allMatch(file -> !file.isEmpty());	//antMatch
		
		
		if(agent != null && itemNum > 0) {
			
			Item item = new Item();
			ItemDetail itemDetail = new ItemDetail();
			ItemOption itemOption = new ItemOption();
			ItemFile itemFile = new ItemFile();
			
			item.setItemNum(itemNum);
			item.setAgentId(agent.getAgentId());
			item.setItemName(itemName);
			item.setItemAddress(itemAddress);
			item.setItemAddressDetail(itemAddressDetail);
			item.setItemPtype(itemPtype);
			item.setItemItype(itemItype);
			item.setItemStatus("Y");
			item.setRecommendItem("N");
			
			itemDetail.setItemNum(itemNum);
			itemDetail.setItemIntro(itemIntro);
			itemDetail.setItemIntroDetail(itemIntroDetail);
			itemDetail.setItemDeposit(itemDeposit);
			itemDetail.setItemMaintainPrice(itemMaintainPrice);
			itemDetail.setItemMonthPrice(itemMonthPrice);
			itemDetail.setItemArea(itemArea);
			itemDetail.setItemParking(itemParking);
			itemDetail.setItemBuildDate(itemBuildDate);
			itemDetail.setItemElevator(itemElevator);
			itemDetail.setItemPet(itemPet);
			itemDetail.setItemDirection(itemDirection);
			itemDetail.setItemKind(itemKind);
			itemDetail.setItemFloor(itemFloor);
			itemDetail.setItemMoveAvbl(itemMoveAvbl);
			itemDetail.setItemMoveDate(itemMoveDate);
				
			itemOption.setItemNum(itemNum);
			itemOption.setSink(sink);
			itemOption.setAir(air);
			itemOption.setLaundry(laundry);
			itemOption.setRefrigerator(refrigerator);
			itemOption.setCloset(closet);
			itemOption.setGasrange(gasrange);
			itemOption.setInduction(induction);
			itemOption.setMicro(micro);
			itemOption.setDesk(desk);
			itemOption.setBed(bed);
			itemOption.setEntrance(entrance);
			itemOption.setCameratv(cameratv);
			itemOption.setIntercom(intercom);
			
			
			if(agentService.itemUpdate(item, itemDetail, itemOption) > 0) {
				int fileDelCnt = 0;
				if (!fileList.isEmpty() && fileIsNotEmpty) {	//파일 존재 시
					log.info("멀티파일 존재 한다?! 왜?!");
					
					curFileList = itemFileService.findFile(itemNum);
					
					
					//기존DB & 사진파일 삭제
					fileDelCnt = agentService.itemFileDelete(itemNum);
					
					if(fileDelCnt > 0) {
						
						if(curFileList != null) {
							log.info("삭제하러 여기는 오나1111");
							for(int i = 0; i < curFileList.size(); i++) {
								String curFileName = curFileList.get(i).getFileName();
								photoUtil.itemFileDelete(curFileName);
								log.info("삭제 후 여기는 오나2222");
							}
						}
						
					}else {
						resCode = 501;	//fileDelete 실패
					}
					
					//새로운 DB & 사진파일 추가
					int max = 1;
					for (MultipartFile file : fileList) {
			            if (!file.isEmpty()) {
			            	
			            	String orgFileName = file.getOriginalFilename();
			            	String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
			            	String fileName = itemNum + "_" + max + ext;
			            	
			            	itemFile.setItemNum(itemNum);
			            	itemFile.setFileIdx(max);
			            	itemFile.setFileName(fileName);
			            	itemFile.setFileOrg(orgFileName);
			            	
			            	max += 1;
			            	
			            	if(agentService.itemFileInsert(itemFile) > 0) {
			            		if(photoUtil.itemFileUpload(fileName, file) >0) {
			            			resCode = 200;
			            		}else {
			            			resCode = 505;	//itemFile upload Error (File)
			            		}
			            	}else {
			            		resCode = 504;	//itemFile insert Error
			            	}
			            } else {
			                log.warn("업로드된 파일이 비어 있습니다.");
			            }
					}
					
				} else {
					log.info("사진 파일이 비었다! -> 사진 Updatd 는 안했다!");
					resCode = 200;
				}
				
				resCode = 200;
				
			}else {
				resCode = 500;
			}
		}else {
			resCode = 404;
		}
		
		
		
		
		model.addAttribute("agent", agent);
		
		
		log.info("+++++++ filesEmptyChk filesEmptyChk -- " + filesEmptyChk);
		log.info("+++++++ fileIsNotEmpty fileIsNotEmpty -- " + fileIsNotEmpty);
		
		log.info("itemNum -- " + itemNum);
		log.info("login id -- " + loginId);
		
		log.info("============== item ===============");
		log.info("itemName -- " + itemName);
		log.info("itemAddress -- " + itemAddress);
		log.info("itemAddressDetail -- " + itemAddressDetail);
		log.info("itemItype -- " + itemItype);
		log.info("itemPtype -- " + itemPtype);
		
		log.info("============== itemDetail ===============");
		log.info("itemName -- " + itemName);
		log.info("itemIntro -- " + itemIntro);
		log.info("itemIntroDetail -- " + itemIntroDetail);
		log.info("itemDeposit -- " + itemDeposit);
		log.info("itemMaintainPrice -- " + itemMaintainPrice);
		log.info("itemMonthPrice -- " + itemMonthPrice);
		log.info("itemArea -- " + itemArea);
		log.info("itemParking -- " + itemParking);
		log.info("itemBuildDate -- " + itemBuildDate);
		log.info("itemElevator -- " + itemElevator);
		log.info("itemPet -- " + itemPet);
		log.info("itemDirection -- " + itemDirection);
		log.info("itemKind -- " + itemKind);
		log.info("itemFloor -- " + itemFloor);
		log.info("itemMoveAvbl -- " + itemMoveAvbl);
		log.info("itemMoveDate -- " + itemMoveDate);		
		
		log.info("============== Option ===============");
		log.info("옵션 sink -- " + sink);
		log.info("옵션 air -- " + air);
		log.info("옵션 laundry -- " + laundry);
		log.info("옵션 refrigerator -- " + refrigerator);
		
		log.info("============== File ===============");
		log.info("파일???? ---- " + fileList);
		log.info("파일 list 사이즈 - " + fileList.size());
		
        for (MultipartFile file : fileList) {
            if (!file.isEmpty()) {
                log.info("파일 이름 -- " +  file.getOriginalFilename());
                log.info("파일 크기 -- bytes" + file.getSize());
                log.info("컨텐츠 타입 -- " + file.getContentType());
            } else {
                log.warn("업로드된 파일이 비어 있습니다.");
            }
        }
		
		return resCode;
	}
	
	
	
	//============================================================================================================ YH  끝
	
	
	// 나의 평점 리뷰확인 페이지
	@RequestMapping("/agent/agentReview")
	public String agentReview(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String agentId = SecurityContextHolder.getContext().getAuthentication().getName();
		Agent agent = agentService.findById(agentId);

		// 별점의 총 평균 조회
		double avgReviewScore = agentService.avgReviewScore(agentId);

		List<AgentReview> myReList = null;
		AgentReview agentReview = new AgentReview();

		// 정렬 
		String option = request.getParameter("option");
		
		// 로그로 옵션 값 확인
		// System.out.println("Option: " + option); 
				
		// 옵션이 넘어오지 않았을 때 기본값 설정
		if (option == null || option.equals("1")) {
			option = "1"; // 최신순
		} else if(option.equals("2")) {
			option = "2"; // 평점 높은 순
		} else {
			option = "3"; // 평점 낮은 순
		}
		
		agentReview.setOption(option);  // 정렬 옵션 값 세팅
		
		int totalCount = agentService.myReviewCount(agentId);

		if (totalCount > 0) {
			agentReview.setAgentId(agentId); // 중개사 아이디 세팅
			myReList = agentService.myReviewList(agentReview);
		}

		// log.info("totalCount : " + totalCount);

		model.addAttribute("totalCount", totalCount);
		model.addAttribute("myReList", myReList);
		model.addAttribute("agentId", agentId);
		model.addAttribute("avgReviewScore", avgReviewScore);
		model.addAttribute("agent", agent);
		model.addAttribute("option", option);

		return "/agent/agentReview";
	}
}
