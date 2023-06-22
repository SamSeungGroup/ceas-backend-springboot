package com.samseung.ceas.testcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class controllertest{
	@GetMapping
	public String controller()
	{
		return "hello";
	}
}