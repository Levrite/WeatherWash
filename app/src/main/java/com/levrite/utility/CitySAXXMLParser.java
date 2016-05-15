package com.levrite.utility;

import com.levrite.data.CityData;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Michael Zaytsev on 01.05.2016.
 */
public class CitySAXXMLParser {

    public static List<CityData> parseCity(InputStream inputStream, String nameCity){
        List<CityData> cityDataList = null;
        try{

            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            CitySAXXMLHandler citySAXXMLHandler = new CitySAXXMLHandler(nameCity);

            xmlReader.setContentHandler(citySAXXMLHandler);
            xmlReader.parse(new InputSource(inputStream));
            cityDataList = citySAXXMLHandler.getCityDataList();

        }catch (Exception e){
            e.printStackTrace();
        }
        return cityDataList;
    }

}
