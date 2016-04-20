package com.levrite.utility;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXParserCity extends AsyncTask<String, String, String> {


    @Override
    protected String doInBackground(String... params) {

        try{

            URL url = new URL("http://export.yandex.ru/weather-ng/forecasts/27612.xml");

            InputSource inputSource = new InputSource(url.openStream());



            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();

            XMLReader xmlReader = saxParser.getXMLReader();

            XMLContentHandler xmlContentHandler = new XMLContentHandler();
            xmlReader.setContentHandler(xmlContentHandler);
            xmlReader.parse(inputSource);

            List<WeatherData> weatherData = xmlContentHandler.getParsedData();

            Iterator<WeatherData> iterator = weatherData.iterator();
            WeatherData weatherDataItem;

            while(iterator.hasNext()){

                weatherDataItem = (WeatherData) iterator.next();
                String weatherType = weatherDataItem.getWeatherType();
                Log.d("WEATHER_TYPE = ", weatherType);
            }


        }catch (NullPointerException e) {

            Log.d("WEATHER_TYPE = ", "NULLPOINT INTO");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("WEATHER_TYPE = ", "EXCEPTION INTO");
            e.printStackTrace();
        }

        return null;
    }


}
