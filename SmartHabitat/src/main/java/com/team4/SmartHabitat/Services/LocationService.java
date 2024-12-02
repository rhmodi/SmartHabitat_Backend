package com.team4.SmartHabitat.Services;

import com.team4.SmartHabitat.Entity.Preference;

public interface LocationService {
    public void getService();
    public void CalcCrimeIndex();
    public void insertEnvIndex();
    public void updateEnvIndex(Preference preference); // for every county
    public void updatePrefIndex(Preference preference); // for every community
}
