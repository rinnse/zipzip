package com.example.demo.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.AgentItem;
import com.example.demo.dto.Item;
import com.example.demo.dto.ItemFile;
import com.example.demo.dto.ItemOption;
import com.example.demo.dto.Report;
import com.example.demo.dto.Search;

@Mapper
public interface ItemRepository {
	
	//아파트 리스트
	public List<Item> selectAptList (Search search);
	//아파트 하나
	public Item selectAptDetail (Search search);
	//File List
	public List<ItemFile> selectItemFile (Search search); 
	
	public ItemOption selectItemOption (Search search);
	
	public List<AgentItem> selectAgentItemList (Search search); //아이디/매물번호 필요
	
	//찜매물 상세 페이지
	public Item selectInterestItemDetail (Search search);
	
	//신고 인서트
	public int insertReport (Report report);
	
	//mian
	public List<Item> recent9item ();

}
