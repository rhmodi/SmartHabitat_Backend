package com.team4.SmartHabitat.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.SmartHabitat.Entity.Preference;
import com.team4.SmartHabitat.Exception.InternalServerErrorException;
import com.team4.SmartHabitat.Services.LocationService;
import com.team4.SmartHabitat.Utility.Message;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${api.base.url}")
@Tag(name="Preference APIs",description="Request and Get Preference")
@Slf4j
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@GetMapping("/home")
	public String home() {
		return "Home";
	}
	@GetMapping("/updateIndex")
	public void updateIndex() {
		locationService.CalcCrimeIndex();
	}

	@PostMapping("/request-habitat")
	public ResponseEntity<?> requestHabitat(@Valid @RequestBody Preference preference) {
		log.info("Response Recieved: {}", preference);
		locationService.getService();
	
	
		try {
			log.info("Processing Successful: {}", preference);
			return  ResponseEntity.status(HttpStatus.OK).body(Message.successRequestMessage());
		}catch(Exception ex) {
			log.error("[Internal Server Error]: {} ", ex);
			throw new InternalServerErrorException("Error Occurred while processing the request");
		}
		
	}
	@GetMapping("/allhabitat")
	public ResponseEntity<?> getAllHabitat() {
		log.info("Retrieved all habitats according to preferences");
		return  ResponseEntity.status(HttpStatus.OK).body(Message.recieveMessage());
	}
}
