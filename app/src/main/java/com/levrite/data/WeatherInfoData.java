package com.levrite.data;

public class WeatherInfoData {

    String weatherType;
    String weatherDate;
    int icoId;

    public int getIcoId() {
        return icoId;
    }

    public void setIcoId(int icoId) {
        this.icoId = icoId;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }
}
