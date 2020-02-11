package com.nagp.helloworld.controller.impl;

import org.springframework.web.bind.annotation.RestController;

import com.nagp.helloworld.controller.HelloWorldRestController;

@RestController
public class HelloWorldRestControllerImpl implements HelloWorldRestController{

	@Override
	public String print(String msg) {
		String dev = "I am from dev environment with this message - "+ msg;
		return dev;
	}

}
