package com.nagp.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public interface HelloWorldRestController {
	
	@GetMapping(value = "/helloworld") 
	public @ResponseBody String print(@RequestParam("msg") String msg);
	
}
