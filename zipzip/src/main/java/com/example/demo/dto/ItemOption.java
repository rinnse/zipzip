package com.example.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOption implements Serializable {
   
   private static final long serialVersionUID = 1L;

   private long itemNum;         //매물 등록번호
    private String sink;         //싱크대
    private String air;            //에어컨
    private String laundry;         //세탁기
    private String refrigerator;   //냉장고
    private String closet;         //옷장
    private String gasrange;      //가스레인지
    private String induction;         //인덕션
    private String micro;         //전자레인지
    private String desk;         //책상
    private String bed;            //침대
    private String entrance;      //공동현관
    private String cameratv;      //CCTV
    private String intercom;      //인터폰
    
}