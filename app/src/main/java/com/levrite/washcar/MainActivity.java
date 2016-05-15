package com.levrite.washcar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.levrite.data.WeatherData;
import com.levrite.utility.XMLContentHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SAXParserCity saxParserCity;
    private TextView textWeather;
    private ImageView imageWeather;
    private ProgressBar progressBar;

    private Button btnCarWash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWeather = (TextView) findViewById(R.id.textWeather);
        imageWeather = (ImageView) findViewById(R.id.imageWeather);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnCarWash = (Button) findViewById(R.id.button_carwash);
        btnCarWash.setOnClickListener(this);


        SharedPreferences sharedPref = this.getSharedPreferences("idCityPref", Context.MODE_PRIVATE);
        writeData(sharedPref);
        saxParserCity = new SAXParserCity(sharedPref.getString("spIdCity", 27612 + ""));

        getSupportActionBar().setTitle(sharedPref.getString("spNameCity", "Москва"));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            saxParserCity.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //Change city Intent
            case R.id.change_city:
                Intent changeCityIntent = new Intent(this, ChangeCityActivity.class);
                startActivity(changeCityIntent);
                break;
            //Share app
            case R.id.share_app:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    //Get data from CityActivity with data (cite name and city ID)
    private void writeData(SharedPreferences sharedPref) {

        if (getIntent().getStringExtra("nameCity") != null) {
            SharedPreferences.Editor spEditor = sharedPref.edit();
            Log.d("WEATHER ID CITY 2", getIntent().getStringExtra("idCity"));
            spEditor.putString("spIdCity", getIntent().getStringExtra("idCity"));
            spEditor.putString("spNameCity", getIntent().getStringExtra("nameCity"));
            spEditor.commit();
        } else {
            return;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_carwash:
                Uri gmmMapIntentUri = Uri.parse("geo:0,0?q=автомойка");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmMapIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }

    //Anonymuos class to parse weather data
    class SAXParserCity extends AsyncTask<Void, Void, Double> {

        private String idCity;
        public double weatherSum = 0;
        public double x;

        public SAXParserCity(String idCity) {
            this.idCity = idCity;
        }


        /* Actions when running doInBackground
        * Hide ImageView and show ProgressBar*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            imageWeather.setVisibility(View.GONE);
            textWeather.setText("Получение данных от сервера...");
        }

        /*Parsing data from url
        * If time is over 500 mil.sec. return number 50 and set ImageView and TextView with eror
         * If process finish return number with sum weather*/
        @Override
        protected Double doInBackground(Void... params) {

            long doTime = SystemClock.currentThreadTimeMillis();
            final long maxTime = 500;
            if (doTime < maxTime) {
                try {
                    URL url = new URL("http://export.yandex.ru/weather-ng/forecasts/" + idCity + ".xml");

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

                    while (iterator.hasNext()) {

                        weatherDataItem = (WeatherData) iterator.next();
                        String weatherType = weatherDataItem.getWeatherType();
                        switch (weatherType) {
                            case "дождь":
                                weatherSum += 1;
                                break;
                            case "пасмурно":
                                weatherSum += 0.3;
                                break;
                            case "пасмурно, небольшой дождь":
                                weatherSum += 0.5;
                                break;
                            case "облачно с прояснениями, небольшой дождь":
                                weatherSum += 0.25;
                                break;
                            case "сильный дождь":
                                weatherSum += 1;
                                break;
                            default:
                                break;
                        }
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    doInBackground();
                    e.printStackTrace();
                }

            } else {
                return 50.0;

            }
            return weatherSum;
        }

        /* Analyze weather and set ImageView and TextView */
        @Override
        protected void onPostExecute(Double weather) {
            super.onPostExecute(weather);

            imageWeather.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Log.d("WEATHER ",weather +"");
            if (weather < 2) {
                textWeather.setText(R.string.good_weather);
                imageWeather.setImageResource(R.drawable.sun);
            } else if (weather <= 5 && weather >= 2) {
                textWeather.setText(R.string.fine_weather);
                imageWeather.setImageResource(R.drawable.cloud);
            } else if (weather >= 5 && weather <= 10) {
                textWeather.setText(R.string.bad_weather);
                imageWeather.setImageResource(R.drawable.lightning);
            } else if (weather == 50) {
                textWeather.setText(R.string.error_connect);
                imageWeather.setImageResource(R.drawable.error);
            }

        }
    }

}

