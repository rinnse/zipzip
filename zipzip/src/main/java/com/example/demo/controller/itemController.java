package com.example.demo.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.AgentItem;
import com.example.demo.dto.InterestItem;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemOption;
import com.example.demo.dto.Report;
import com.example.demo.dto.Search;
import com.example.demo.dto.User;
import com.example.demo.service.ItemService;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class itemController {
	
	@Autowired
	ItemService itemService;
	@Autowired
	UserService userService;
	
	@GetMapping("/item/one")
	public String one(@RequestParam(value="info", required=false) String info,
						Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userService.findById(userId);
		model.addAttribute("user",user);
		Search search = new Search();
		//search 객체에 쿠키 아이디 추가
		search.setUserId(null);
		//search 객체에 필터 추가
		//일단 하드코딩으로 처음 로딩되는 범위 
		search.setSearchType("O");
		List<String> sl = new ArrayList<>();

		if(info != null && !info.isEmpty()) {
			sl.add(info);
			Map<String, String> ret = itemService.getGeoCode(info);
			model.addAttribute("lat", ret.get("lat"));
			model.addAttribute("lng",ret.get("lng"));
			log.info(ret.get("lat"),ret.get("lng"));
		}
		else {
			sl.add("마포구");
		}
		search.setSearchAdd(sl);

		List<Item> list = itemService.selectAptList(search);
		if(list!=null) {
			for(int i=0;i<list.size();i++) {
				//log.info("몇개? {}",list.get(i).getItemNum());
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
	
		model.addAttribute("list", list);
		if(!list.isEmpty()) {
			model.addAttribute("first",list.get(0).getJsonObj());
		}
		return "item/one";
	}
	
	@GetMapping("/item/two")
	public String two(@RequestParam(value="info", required=false) String info,Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userService.findById(userId);
		model.addAttribute("user",user);
		Search search = new Search();
		//search 객체에 쿠키 아이디 추가
		//search 객체에 필터 추가
		//일단 하드코딩으로 처음 로딩되는 범위 
		search.setSearchType("T");
		List<String> sl = new ArrayList<>();
		if(info != null && !info.isEmpty()) {
			sl.add(info);
			Map<String, String> ret = itemService.getGeoCode(info);
			model.addAttribute("lat", ret.get("lat"));
			model.addAttribute("lng",ret.get("lng"));
			log.info(ret.get("lat"),ret.get("lng"));
		}
		else {
			sl.add("마포구");
		}
		search.setSearchAdd(sl);

		List<Item> list = itemService.selectAptList(search);
		if(list!=null) {
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
	
		model.addAttribute("list", list);
		if(!list.isEmpty()) {
			model.addAttribute("first",list.get(0).getJsonObj());
		}
		return "item/two";
	}
	
	@GetMapping("/item/off")
	public String off(@RequestParam(value="info", required=false) String info,Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userService.findById(userId);
		model.addAttribute("user",user);
		Search search = new Search();
		//search 객체에 쿠키 아이디 추가
		//search 객체에 필터 추가
		//일단 하드코딩으로 처음 로딩되는 범위 
		search.setSearchType("F");
		List<String> sl = new ArrayList<>();
		if(info != null && !info.isEmpty()) {
			sl.add(info);
			Map<String, String> ret = itemService.getGeoCode(info);
			model.addAttribute("lat", ret.get("lat"));
			model.addAttribute("lng",ret.get("lng"));
			log.info(ret.get("lat"),ret.get("lng"));
		}
		else {
			sl.add("마포구");
		}
		search.setSearchAdd(sl);
		//log.info("search.getItemNum : {}", search.getSearchSize());
		List<Item> list = itemService.selectAptList(search);
		
		//log.info("list의 길이:{}",list.size());
		
		if(list!=null) {
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
					//log.info(list.get(i).getTransD());
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

		model.addAttribute("list", list);
		model.addAttribute("first",list.get(0).getJsonObj());
		return "item/off";
	}
	

	@GetMapping("/item/apt")
	public String apt(@RequestParam(value="info", required=false) String info,Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userService.findById(userId);
		model.addAttribute("user",user);
		Search search = new Search();
		//search 객체에 쿠키 아이디 추가
		//search 객체에 필터 추가
		//일단 하드코딩으로 처음 로딩되는 범위 
		search.setSearchType("A");
		List<String> sl = new ArrayList<>();
		if(info != null && !info.isEmpty()) {
			sl.add(info);
			Map<String, String> ret = itemService.getGeoCode(info);
			model.addAttribute("lat", ret.get("lat"));
			model.addAttribute("lng",ret.get("lng"));
			log.info(ret.get("lat"),ret.get("lng"));
		}
		else {
			sl.add("마포구");
		}
		search.setSearchAdd(sl);
		//log.info("search.getItemNum : {}", search.getSearchSize());
		List<Item> list = itemService.selectAptList(search);
		
		//log.info("list의 길이:{}",list.size());
		
		if(list!=null) {
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
					//log.info(list.get(i).getTransD());
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

		model.addAttribute("list", list);
		model.addAttribute("first",list.get(0).getJsonObj());
		
		return "item/apt";
	}
	
	
	
	@RequestMapping(value = "/cluster", method = RequestMethod.POST)
	@ResponseBody
	public String makeClusterAjax (@RequestParam(value="info", required=false) String info, 
								   @RequestParam(value="type", required=false) String type,
								   Model model) 
			throws Exception {
		//@RequestBody String add
		log.info("makeClusterAjax 시작");
		
		//위치값을 받아서 서비스에서 select 해오기
		Search search = new Search();
		log.info("info:{}",info);

		if(!type.isEmpty()) {
			search.setSearchType(type);
		}
		if (!info.isEmpty() && info!=null) {
			search.setSearchAdd(new ArrayList<>());
			String[] tmp = info.split(",");
			for (String address : tmp) {
				log.info("tmp: {}"+tmp);
		        search.getSearchAdd().add(address);
		    }
		}
		
		List<String> test = new ArrayList<>();
		List<Item> list = itemService.selectAptList(search);
		
		log.info("Cluster list의 길이 : {}",list.size());
		
		for(int i=0;i<list.size();i++) {
			test.add(list.get(i).getItemAddress());
		}
		model.addAttribute("list2",list);

		
		List<Map<String, Object>> jsonArray = itemService.getGeoCode(test);
		JSONArray codezip = null;
		try {
			codezip = itemService.convertListToJson(jsonArray);
			
			log.info("codezip: {}",codezip);
			
		} catch (Exception e) {
			log.info("오류?");
			e.printStackTrace();
		}
		return codezip.toString();
	}
	
	@RequestMapping(value = "/reList", method = RequestMethod.POST)
	public String makeListAjax (@RequestParam(value="info", required=false) String info, 
			   					@RequestParam(value="type", required=false) String type,Model model) {
		//@RequestBody String add
		//log.info("makeListAjax 시작");

		Search search = new Search();
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = userService.findById(userId);

		model.addAttribute("user",user);
		
		if(!type.isEmpty()) {
			search.setSearchType(type);
		}

		if (!info.isEmpty() && info!=null) {
			//log.info("if 조건 시작");
			search.setSearchAdd(new ArrayList<>());
			String[] tmp = info.split(",");
			for (String address : tmp) {
		        search.getSearchAdd().add(address);
		    }
		}
		 
		List<Item> list = itemService.selectAptList(search);
		
		if(list!=null) {
			
			for(int i=0;i<list.size();i++) {
				//log.info(list.get(i).getItemItype());
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
					//log.info(list.get(i).getTransD());
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
		
		//log.info("Cluster list의 길이 : {}",list.size());
		model.addAttribute("list2",list);
		
		return "item/item";
	};
	
	@RequestMapping("/detail")
	   public String detailPage    (@RequestParam(value = "itemNum")long itemNum,
	                        @RequestParam(value = "type")String type, Model model) {
	      
	      Item apart = new Item();
	      Search search = new Search();
	      String userId = SecurityContextHolder.getContext().getAuthentication().getName();

	      if(userId != null) {
	         //로그인 유저
	         model.addAttribute("user",userId);
	         
	         if(itemNum != 0) {
	            InterestItem item = new InterestItem();
	            item.setUserId(userId);
	            item.setItemNum(itemNum);
	            
	            int zzim = userService.interestItemFindSelect(item);
	            
	            if (zzim > 0) {
	               model.addAttribute("like",zzim);
	            }
	         }
	      }
	      else {
	         model.addAttribute("user"," ");
	         model.addAttribute("like",0);
	      }
	      
	      search.setItemNum(itemNum);
	      
	      if(!type.isEmpty()) {
	         search.setSearchType(type);
	      }
	      
	      ItemOption io = itemService.selectItemOption(search);
	      if(itemNum != 0) {
	         apart = itemService.selectAptDetail(search);
	         apart.setItemNum(itemNum);
	         
	         long tmp = apart.getItemDeposit();
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
	            apart.setTransD(a);
	         }
	         //월세
	         tmp = apart.getItemMonthPrice();
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
	            apart.setTransM(a);
	         }
	      }
	      String tamp="";
	      switch(apart.getItemPtype()) {
	         case "S" : tamp="매매 "; break;
	         case "Y" : tamp="전세 "; break;
	         case "M" : tamp="월세 "; break;
	      }
	      apart.setItemPtype(tamp);
	      
	      apart.setINum(String.format("%010d", itemNum));
	      if(apart != null) {
	         log.info("agentId: {}",apart.getAgentId());
	         search.setUserId(apart.getAgentId());
	         log.info("getItemNum: {}",apart.getItemNum());
	         search.setItemNum(apart.getItemNum());
	      }
	      //해당 중개인의 다른 매물
	      List<AgentItem> list = itemService.selectAgentItemList(search);
	      log.info("list size? {}",list.size());
	      if(list.size()>0) {
	         for(int i=0;i<list.size();i++) {
	            String tmp = "";
	            switch(list.get(i).getItemPtype()) {
	            case "S" : tmp="매매 "; break;
	            case "Y" : tmp="전세 "; break;
	            case "M" : tmp="월세 "; break;
	            }
	            list.get(i).setItemPtype(tmp);
	         }
	         
	         model.addAttribute("list", list);   
	      }
	      else {
	         model.addAttribute("list", " ");
	      }
	      //해당 매물 정보
	      model.addAttribute("item",apart);   
	      
	      //해당 매물 옵션 정보
	      model.addAttribute("io", io);
	      //해당 매물 사진 리스트
	      if(apart.getItemFileName().size()>0) {
	         model.addAttribute("photos",apart.getItemFileName());
	      }
	      else {
	         model.addAttribute("photos"," ");
	      }
	      log.info("controller");
	   
	      return "item/detail";
	   }
	
	@RequestMapping ("/report")
	@ResponseBody
	public int reportProbAjax (HttpServletRequest request) {
		long itemNum = Long.valueOf(request.getParameter("thisNum"));
		String reportType = request.getParameter("classified");
		String reportContent = request.getParameter("message");
		String userId = request.getParameter("userId");
		int code=0;
		
		if(itemNum != 0 && reportType != "") {
			Report report = new Report();
			report.setItemNum(itemNum);
			report.setUserId(userId);
			report.setReportContent(reportContent);
			report.setReportType(reportType);
			
			code = itemService.insertReport(report);
			
		}
		else {
			code = -1;
		}
		return code;
	}
	
	@RequestMapping("/likeitem")
	@ResponseBody
	public int likeItem (@RequestParam(value = "itemNum")long itemNum) {
		int cnt = 0;
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!userId.isEmpty() && itemNum != 0) {
			InterestItem item = new InterestItem();
			
			item.setUserId(userId);
			item.setItemNum(itemNum);
			
			int like = userService.interestItemFindSelect(item);
			//log.info("like:"+like);
			
			if(like>0) {
				cnt = userService.interestItemDelete(item);
				//log.info("cnt:"+cnt);
			}
			else {
				cnt = userService.interestItemInsert(item);
				//log.info("cnt:"+cnt);
			}
		}
		
		//log.info("itemNum:"+itemNum);
		//log.info("userId:"+userId);
		//log.info("cnt:"+cnt);
		
		return cnt;
	}
	

	

}
