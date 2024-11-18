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
	@JsonProperty("crimeRate")
	private int crimeRatePreference;
	@JsonProperty("environmentRate")
	private int environmentRatePreference;
	@JsonProperty("airQualityIndex")
	private int airQualityIndex;
	@JsonProperty("heatMetricIndex")
	private int heatMetricIndex;
	@JsonProperty("uvRadiationIndex")
	private int uvRadiationIndex;
	@JsonProperty("precipationIndex")
	private int precipationIndex;
}
