package cn.appsys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DevUserService;

import cn.appsys.tools.Constants;

@Controller
@RequestMapping("dev")
public class DevUserController {
	
	@Autowired
	private DevUserService service;
	
	@RequestMapping("login")
	public String login(){
		return "devlogin";
	}
	
	@RequestMapping("dologin")
	public String dologin(String devCode,String devPassword,HttpSession session,Model model){
		DevUser user = service.login(devCode);
		if(user==null){
			model.addAttribute("error", "用户不存在");
			return "devlogin";
		}
		
		if(!user.getDevPassword().equals(devPassword)){
			model.addAttribute("error", "密码错误");
			return "devlogin";
		}
		
		session.setAttribute(Constants.DEV_USER_SESSION, user);
		return "redirect:main";
	}
	
	@RequestMapping("main")
	public String main(HttpSession session){
		if(session.getAttribute(Constants.DEV_USER_SESSION)==null){
			return "devlogin";
		}
		return "developer/main";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session){
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "devlogin";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
