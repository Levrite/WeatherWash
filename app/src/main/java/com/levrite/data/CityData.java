package com.levrite.data;

/**
 * Created by Michael Zaytsev on 01.05.2016.
 */
public class CityData {

    private String nameCity;
    private String idCity;

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    @Override
    public String toString() {
        return  nameCity;
    }

}
