package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
   @NotBlank(message = "아이디는 필수 입력사항 입니다")
   private String userId;      // 아이디
   
   @NotBlank(message = "비밀번호는 필수 입력사항 입니다")
    private String userPwd;      // 비밀번호
    
   @NotBlank(message = "이름은 필수 입력사항 입니다")
    private String userName;   // 이름
    
   @NotBlank(message = "전화번호는 필수 입력사항 입니다 (-제외)")
    private String userPhone;   // 전화번호
    
   @NotBlank(message = "이메일은 필수 입력사항 입니다")
    private String userEmail;   // 이메일
    
    private String status;      // 상태(Y:활동 N:정지 Q:탈퇴)
    
    private String regDate;      // 가입일자
    
    private String emailAuth;   // 이메일 인증(Y, N)
    
    private String role;      // 권한
    
}