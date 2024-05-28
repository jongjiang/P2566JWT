package com.test.tfss.controller;

//@RestController
public class HelloController {
//	@RequestMapping(value = "/")
	public String spring() {
		return "Spring Boot!!";
	}

//	@RequestMapping(value = "/hello")
	public String hello() {
		return "Hello World!!";
	}
}
