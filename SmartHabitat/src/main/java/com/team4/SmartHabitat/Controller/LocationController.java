package com.team4.SmartHabitat.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.SmartHabitat.Entity.Preference;
import com.team4.SmartHabitat.Utility.Message;

@RestController
@RequestMapping("${api.base.url}")
public class LocationController {
	
	
	@GetMapping("/home")
	public String home() {
		return "Home";
	}
	@PostMapping("/request-habitat")
	public ResponseEntity<?> requestHabitat(@RequestBody Preference preference) {
		return  ResponseEntity.status(HttpStatus.OK).body(Message.successRequestMessage());
	}
	@GetMapping("/allhabitat")
	public ResponseEntity<?> getAllHabitat() {
		return  ResponseEntity.status(HttpStatus.OK).body(Message.recieveMessage());
	}
}
