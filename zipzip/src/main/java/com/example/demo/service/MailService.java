package com.example.demo.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Mail;
import com.example.demo.dto.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final JavaMailSender javaMailSender;
	private final UserRepository userRepository;
	private static final String senderEmail= "zipzip@gmail.com";
	private static int number;
	
	public static void createNumber() {
		// (int) Math.random() * (최댓값-최소값+1) + 최소값
		number = (int)(Math.random() * (90000)) + 100000;
	}
	
	 public MimeMessage CreateMail(String userEmail) {
		log.info("MailService => createMail");
		System.out.println("userEmail : " + userEmail);
		System.out.println("number : " + number);
		
		createNumber();
		MimeMessage message = javaMailSender.createMimeMessage();
		
		try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, userEmail);
            message.setSubject("ZIPZIP 이메일 인증 안내 이메일 입니다");
            String body = "";
            body += "<div style='margin:20px;'>";
            body += "<h1> 안녕하세요 ZIPZIP 입니다. </h1>";
            body += "<br>";
            body += "<p>아래 코드를 입력해주세요<p>";
            body += "<br>";
            body += "<p>감사합니다.<p>";
            body += "<br>";
            body += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            body += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
            body += "<div style='font-size:130%'>";
            body += "CODE : <strong>";
            body += number + "</strong><div><br/> ";
            body += "</div>";
            message.setText(body,"UTF-8", "html");
        } 
		
		catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
	}
	
	 public int sendMail(String userEmail) {
		log.info("MailService => sendMail");
		
		MimeMessage message = CreateMail(userEmail);
		javaMailSender.send(message);
		
		return number;
	}
	
	 
	//===============================================================================================================//
	 
	public static String tempPwd() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        
        return str;
	}
	
	public Mail tempPwdMail(User user) {
		
		String str = tempPwd();
		Mail mail = new Mail();
		
		String msg = "";
		msg += "<div style='margin:20px;'>";
		msg += "<h1> 안녕하세요 ZIPZIP 입니다. </h1>";
		msg += "<br>";
		msg += "<p>회원님의 임시 비밀번호를 아래와 같이 보내드립니다<p>";
		msg += "<br>";
		msg += "<p>임시 비밀번호는 로그인 한 후 반드시 비밀번호를 변경해 주세요<p>";
		msg += "<br>";
		msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
		msg += "<h3 style='color:blue;'>임시 비밀번호</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "PASSWORD : <strong>";
        msg += str + "</strong><div><br/> ";
        msg += "</div>";
		
		mail.setAddress(user.getUserEmail());
		mail.setTitle("ZIPZIP 임시 비밀번호 안내 이메일 입니다");
        mail.setMessage(msg);
        updatePwdUser(str, user);
        
        return mail;
		
	}
	
	public void tempPwdMailSend(Mail mail) {
		System.out.println("전송완료");
		
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(mail.getAddress());
//		message.setSubject(mail.getTitle());
//		message.setText(mail.getMessage());
//		message.setFrom("ZIPZIP@gmail.com");
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			messageHelper.setTo(mail.getAddress());
			messageHelper.setSubject(mail.getTitle());
			messageHelper.setText(mail.getMessage(), true);
			messageHelper.setFrom("ZIPZIP@gmail.com");
			
			javaMailSender.send(mimeMessage);
		}
		
		catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean updatePwdUser(String str, User user) {
		
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			System.out.println(encoder);
			String encodePwd = encoder.encode(str);
			System.out.println(encodePwd);
			System.out.println(user);

			User userEmail = userRepository.findEmail(user);

			System.out.println("userEmail : " + userEmail);
			user.setUserPwd(encodePwd);
			userRepository.tempPwdChange(user);
			System.out.println("after : " + user);
			
			return true;
		}
		
		catch (Exception e) {
			return false;
		}
	}
}
