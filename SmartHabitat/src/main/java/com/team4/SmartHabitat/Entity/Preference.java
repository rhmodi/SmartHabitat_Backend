package com.team4.SmartHabitat.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Preference {
	@NotNull(message = "City is a required field and cannot be null.")
	@JsonProperty("city")
	private String city;
	@JsonProperty("crimePreferencePercent")
	private int crimePreferencePercent;
	@JsonProperty("environmentPreferencePercent")
	private int environmentPreferencePercent;
	@JsonProperty("airQualityPriority")
	private int airQualityPriority;
	@JsonProperty("heatMetricPriority")
	private int heatMetricPriority;
	@JsonProperty("uvRadiationPriority")
	private int uvRadiationPriority;
	@JsonProperty("precipationPriority")
	private int precipationPriority;
}
