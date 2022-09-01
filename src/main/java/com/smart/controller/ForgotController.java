package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.services.EmailService;

@Controller
public class ForgotController {
	Random random = new Random(1000);

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
// email id form open handler
	@GetMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}

	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		System.out.println(email);

		// 4 digit otp

		int otp = random.nextInt(9999);

		System.out.println("OTP " + otp);

		// code to send otp to email

		String message = "<div style='border:1px solid #e2e2e2; padding:20px;'>" + "<h1>" + "OTP is " + "<b>" + otp
				+ "</b>" + "</h1>" + "</div>";
		String subject = "OTP from SCM";
		String to = email;

		boolean flag = this.emailService.sendEmail(subject, message, to);

		if (flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		} else {
			session.setAttribute("message", "Check your email ID!");

			return "forgot_email_form";

		}

	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp , HttpSession session) {
		
		int myOtp = (int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		
		if (myOtp==otp) {
			
			User user = this.userRepository.getUserByUserName(email);
			
			if (user==null) {
				// send error msg
				session.setAttribute("message", "User does not exists with this email!");

				return "forgot_email_form";
			}else {
				//send change pwd form
				
			}
			
			return "password_change_form";
		}else {
			session.setAttribute("message", "You have entered wrong OTP");
			return "verify_otp";
		}
		
		
	}
	// change pwd
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword ,HttpSession session) {
		String email = (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		
		return "redirect:/signin?change=password change successfuly...";
		
	}
}
