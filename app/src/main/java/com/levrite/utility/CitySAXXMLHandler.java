package com.levrite.utility;

import com.levrite.data.CityData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael Zaytsev on 01.05.2016.
 */
public class CitySAXXMLHandler extends DefaultHandler {

    private boolean mCountryCheck = false;
    private List<CityData> cityDataList;
    private CityData mCityData;
    private String tempVal;
    private String nameCountry;

    public CitySAXXMLHandler(String nameCountry){
        cityDataList = new ArrayList<CityData>();
        this.nameCountry = nameCountry;

    }

    public List<CityData> getCityDataList() {
        return cityDataList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("city") && attributes.getValue("country").equals(nameCountry)){
            mCityData = new CityData();
            cityDataList.add(mCityData);
            mCountryCheck = true;
            mCityData.setIdCity(attributes.getValue("id"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(localName.equalsIgnoreCase("city") && mCountryCheck){
            mCityData.setNameCity(tempVal);
        }
        mCountryCheck = false;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }
}
