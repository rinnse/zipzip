package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AgentItem;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemFile;
import com.example.demo.dto.ItemOption;
import com.example.demo.dto.Report;
import com.example.demo.dto.Search;
import com.example.demo.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemService {
	
	
	private static String GEOCODE_URL="http://dapi.kakao.com/v2/local/search/address.json?query=";
    private static String GEOCODE_USER_INFO="533996219fc60d357981a219962147f5"; 
	
	@Autowired
	ItemRepository itemRepository;
	
	public List<Item> selectAptList (Search search){
		List<Item> apt = null;
		try {
			apt = itemRepository.selectAptList(search);
			List<String> add = new ArrayList<>(apt.size());
			log.info("ItemService try~ catch 문 시작 + for문 시작");
			for (Item item : apt) {
			    add.add(item.getItemAddress());
			}
			
			List<Map<String,Object>> res = getGeoCode(add);
			JSONArray arr = convertListToJson(res);
			if(arr!=null && arr.length()>0) {
				apt.get(0).setJsonObj(arr.toString());
				//log.info(apt.get(0).getJsonObj());
			}
			log.info("toString 완료");
			
		}
		catch(Exception e) {
			log.info("ItemService Exception : ", e);
		}
		
		return apt;
	}
	
	public ItemOption selectItemOption (Search search) {
		ItemOption io = null;
		try {
			io = itemRepository.selectItemOption(search);
		}
		catch(Exception e) {
			log.debug("ItemService selectITemOption Exception",e);
		}
		
		return io;
	}
	
	public Item selectAptDetail (Search search) {
		Item apt = itemRepository.selectAptDetail(search);
		// 파일 넣기
		  List<ItemFile> list = itemRepository.selectItemFile(search); if(list.size()>0) {
		  apt.setItemFileName(list);
		  }
		 
		 return apt;
	}

    //list->json으로
	
	public List<AgentItem> selectAgentItemList (Search search){
		List<AgentItem> list = null;
		try {
			list = itemRepository.selectAgentItemList(search);
		}
		catch(Exception e) {
			log.debug("itemService selectAgentItemList Exception", e);
		}
		return list;
	}
    
    public JSONArray convertListToJson(List<Map<String, Object>> list) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : list) {
            jsonArray.put(convertMapToJson(map));
        }
        log.info("jsonArray 변환 완료");
        return jsonArray;
    }

    private JSONObject convertMapToJson(Map<String, Object> map) throws Exception {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
       
				jsonObject.put(entry.getKey(), entry.getValue());
			
        }
        return jsonObject;
    }
    
    //주소-좌표 변환 1개
	public Map<String, String> getGeoCode(String add) {
		
        try {
        	log.info("getGeo Start");
        	log.info("Address:"+add);
        	
        	
        	String address = URLEncoder.encode(add, "UTF-8");
        	String url = GEOCODE_URL + address+"&key="+GEOCODE_USER_INFO;
        	URL Url = new URL(url);
        	
        	HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
        	conn.setRequestProperty("Authorization", "KakaoAK " + GEOCODE_USER_INFO); // 인증 헤더 추가
        	log.info("====================1");
        	//url.openConnection.getInputStream 리턴값을 is에 담기
        	
        	InputStream is = conn.getInputStream();
        	BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        	
        	StringBuilder resBuilder = new StringBuilder();
        	String inputStr;
        	
        	log.info("====================2");
        	
        	while ((inputStr=bf.readLine())!=null) {
        		//log.info(">>>"+inputStr);
        		resBuilder.append(inputStr); //inputStr를 파라미터로 갖는 리턴값 반환
        	}
        	
        	log.info("====================3");
        	
        	JSONObject jo = new JSONObject(resBuilder.toString());
        	//log.info("jsonObj : " + jo);
        	JSONArray results = jo.getJSONArray("documents");
        	log.info("comeBack...");
        	
        	//리턴할 맵 생성
        	Map<String, String> ret = new HashMap<String, String>();
        	
        	
        	if(results != null && results.length() > 0) {
        		JSONObject jsonObj = results.getJSONObject(0);
        		JSONObject address2 = jsonObj.getJSONObject("address");

        		// 위도 및 경도 정보 가져오기
        		Double lat = address2.getDouble("y"); // 위도
        		Double lng = address2.getDouble("x"); // 경도
        		String addressName = address2.getString("address_name"); // 한글 주소


        		// "address" 정보에서 위도, 경도 및 한글 주소 추가
        		ret.put("lat", lat.toString());
        		ret.put("lng", lng.toString());
        		ret.put("addressName", addressName);

        		// 로그 출력
				/*
				 * log.info("==========*return*==========="); log.info("LAT(위도) : " + lat);
				 * log.info("LNG(경도) : " + lng); log.info("한글 주소: " + addressName);
				 * log.info("=============================");
				 */
        	}
        	return ret;
        	
        }    
        catch(Exception e) {
        	log.debug("geocode Exception", e);

        	return null;
        }
        
	}
	
	//오버라이딩
	public List<Map<String, Object>> getGeoCode (List<String> add) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
        try {
        	log.info("getGeo List Start");
        	log.info("Address:"+add);
        	
        	for(int i=0;i<add.size();i++) {
        		
        		String address = URLEncoder.encode(add.get(i), "UTF-8");
	            String url = GEOCODE_URL + address+"&key="+GEOCODE_USER_INFO;
	            URL Url = new URL(url);
	            	
	            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
	            conn.setRequestProperty("Authorization", "KakaoAK " + GEOCODE_USER_INFO); 
	            //log.info("====================1");
	            	
	            InputStream is = conn.getInputStream();
	            BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	            	
	            StringBuilder resBuilder = new StringBuilder();
	            String inputStr;
	            	
	            //log.info("====================2");
	            	
	            while ((inputStr=bf.readLine())!=null) {
	            
	            	resBuilder.append(inputStr); //inputStr를 파라미터로 갖는 리턴값 반환
	            }
	            	
	            //log.info("====================3");
	            	
	            JSONObject jo = new JSONObject(resBuilder.toString());
	            //log.info("jsonObj : " + jo);
	            JSONArray results = jo.getJSONArray("documents");
	            //log.info("comeBack...");
	            	
	            	//리턴할 맵 생성
	            Map<String, Object> ret = new HashMap<String, Object>();
	            	
	            	
	            if(results != null && results.length() > 0) {
	            	JSONObject jsonObj = results.getJSONObject(0);
	            	JSONObject address2 = jsonObj.getJSONObject("address");
	
	            	// 위도 및 경도 정보 가져오기
	            	Double lat = address2.getDouble("y"); // 위도
	            	Double lng = address2.getDouble("x"); // 경도
	            	String addressName = address2.getString("address_name"); // 한글 주소
	
	
	            		// "address" 정보에서 위도, 경도 및 한글 주소 추가
	            	ret.put("lat", lat.toString());
	            	ret.put("lng", lng.toString());
	            	ret.put("addressName", addressName);
	        	}
        	
        	result.add(ret);
        	}
        	log.info("geoCode 변환 완료!");
        	return result;
        }    
        catch(Exception e) {
        	log.debug("geocode Exception", e);
        	return null;
        }
        
	}
	
	// 찜매물 상세 페이지
   public Item selectInterestItemDetail (Search search) {
	      Item interstItem = itemRepository.selectInterestItemDetail(search);
	      /* 파일 넣기
	       * List<ItemFile> list = itemMapper.selectItemFile(search); if(list.size()>0) {
	       * apartD.setItemFile(list); }
	       */
	       return interstItem;
	   }
   public int insertReport (Report report) {
	   int res = 0;
	   try {
		   if(itemRepository.insertReport(report)>0) {
			   res = 1;
		   }
	   }
	   catch(Exception e) {
		   res = -1;
	   }
	   return res;
   }
   
   //for main page
   public List<Item> recent9item (){
	   List<Item> list = null;
	   try {
		   list = itemRepository.recent9item();
	   }
	   catch(Exception e) {
		   log.debug("[ItemService] recent9item Exception",e);
	   }
	   return list;
   }

}
