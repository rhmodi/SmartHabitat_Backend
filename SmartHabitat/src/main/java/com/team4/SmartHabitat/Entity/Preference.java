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
	public String city;
	@JsonProperty("crimePreferencePercent")
	public int crimePreferencePercent;
	@JsonProperty("environmentPreferencePercent")
	public int environmentPreferencePercent;
	@JsonProperty("airQualityPriority")
	public int airQualityPriority;
	@JsonProperty("heatMetricPriority")
	public int heatMetricPriority;
	@JsonProperty("uvRadiationPriority")
	public int uvRadiationPriority;
	@JsonProperty("precipationPriority")
	public int precipationPriority;
}
