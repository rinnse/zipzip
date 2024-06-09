package com.example.demo.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayReady {

	private String tid;
	private String next_redirect_pc_url;
	private Date created_at;
}
