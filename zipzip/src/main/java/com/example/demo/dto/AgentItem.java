package com.example.demo.dto;

import lombok.Data;

@Data
public class AgentItem {
	private long itemNum;
	private String itemPtype;
	private String itemItype;
	private long itemDeposit;
	private long itemMonthPrice;
	private String fileName;
	private String itemName;
}