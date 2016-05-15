package com.levrite.utility;

import com.levrite.data.CountryData;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Michael Zaytsev on 03.05.2016.
 */
public class CountrySAXXMLParser {

    public static List<CountryData> parseCountry(InputStream inputStream){
        List<CountryData> countryDataList = null;
        try{

            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            CountrySAXXMLHandler countrySAXXMLHandler = new CountrySAXXMLHandler();

            xmlReader.setContentHandler(countrySAXXMLHandler);
            xmlReader.parse(new InputSource(inputStream));
            countryDataList = countrySAXXMLHandler.getCountryDataList();

        }catch (Exception e){
            e.printStackTrace();
        }
        return countryDataList;
    }

}
