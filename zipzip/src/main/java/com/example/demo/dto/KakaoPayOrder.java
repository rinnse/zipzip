package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayOrder {

	private String partnerOrderId;		//가맹점 주문번호, 최대 100자
	private String partnerUserId;		//가맹점 회원 id, 최대 100자
	private String itemName; 			//상품명, 최대 100자
	private String itemCode; 			//상품코드, 최대 100자
	private int quantity; 				//상품 수량
	private int totalAmount; 			//상품 총액
	private int taxFreeAmount; 			//상품 비과세 금액
	private int vatAmount;  			//상품 부가세 금액
										//값을 보내지 않을 경우 다음과 같이 VAT 자동 계산
										//(상품총액 - 상품 비과세 금액)/11 : 소숫점 이하 반올림
	
	private String tId;					//결제 고유번호, 20자
	private String pgToken;				//결제승인 요청을 인증하는 토큰
										//사용자 결제 수단 완료시, 
										//approval_url로 redirection 해줄때
										//pg_token을 query sSte
}
