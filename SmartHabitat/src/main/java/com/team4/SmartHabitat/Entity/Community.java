package com.team4.SmartHabitat.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Community {
    public Community() {}

    @JsonProperty("IRI")
    public String IRI;

    @JsonProperty("name")
    public String name;
    
    @JsonProperty("seriousCrimeIndex")
    public float seriousCrimeIndex;
    
    @JsonProperty("moderateCrimeIndex")
    public float moderateCrimeIndex;
    
    @JsonProperty("criticalCrimeIndex")
    public float criticalCrimeIndex;
    
    @JsonProperty("airQualityIndex")
    public float airQualityIndex;
    
    @JsonProperty("heatIndex")
    public float heatIndex;
    
    @JsonProperty("uvRadiationIndex")
    public float uvRadiationIndex;
    
    @JsonProperty("precipitationIndex")
    public float precipitationIndex;
}
