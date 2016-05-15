package com.levrite.utility;

import com.levrite.data.CountryData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Zaytsev on 03.05.2016.
 */
public class CountrySAXXMLHandler extends DefaultHandler {

    private List<CountryData> countryDataList;
    private CountryData countryData;

    public CountrySAXXMLHandler(){
        countryDataList = new ArrayList<CountryData>();
    }

    public List<CountryData> getCountryDataList() {
        return countryDataList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("country")){
            countryData = new CountryData();
            countryData.setNameCountry(attributes.getValue("name"));
            countryDataList.add(countryData);
        }
    }

}
