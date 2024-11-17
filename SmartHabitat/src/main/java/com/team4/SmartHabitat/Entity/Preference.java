package com.team4.SmartHabitat.Entity;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Preference {
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
