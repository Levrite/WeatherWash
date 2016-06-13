package com.levrite.washcar.data;

import java.io.Serializable;

/**
 * Created by Michael Zaytsev on 19.05.2016.
 */
public class WeatherData implements Serializable{

    private String weatherType;
    private String weatherDate;

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }
}
