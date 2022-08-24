package com.handle.api.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.handle.api.services.HttpUtils;

@RestController
@RequestMapping("/review")
public class ReviewREST {
	
	@RequestMapping(value = "/secuence", method = RequestMethod.POST)
	public void getData() {
		
	}
	
	@RequestMapping(value="/info", method = RequestMethod.POST)
	public void getInformation(HttpServletRequest request) {
		System.out.println(HttpUtils.getRequestIP(request));
		String pattern = "yyyy-MM-dd hh:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String date = sdf.format(new Date());
		System.out.println(date);
	}
}
