package com.levrite.data;

/**
 * Created by Michael Zaytsev on 03.05.2016.
 */
public class CountryData {

    private String nameCountry;

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    @Override
    public String toString() {
        return nameCountry;
    }
}
