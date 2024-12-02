package com.team4.SmartHabitat.Services;

import java.util.List;
import java.util.Map;

import com.team4.SmartHabitat.Entity.Preference;

public interface LocationService {
    public void getService();
    public void CalcCrimeIndex();
    public void insertEnvIndex();
    public void updateEnvIndex(Preference preference); // for every county
    public void updatePrefIndex(Preference preference); // for every community
    public List<Map.Entry<String, Float>> CalculateOverallIndex(Preference preference);
}
