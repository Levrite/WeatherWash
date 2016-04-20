package com.levrite.washcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.levrite.utility.SAXParserCity;

import javax.xml.parsers.SAXParser;

public class MainActivity extends AppCompatActivity {

    SAXParserCity saxParserCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences("idCityPref", Context.MODE_PRIVATE);
        writeData(sharedPref);

        saxParserCity = new SAXParserCity();

        try {
            saxParserCity.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void writeData(SharedPreferences sharedPref){

        if(getIntent().getStringExtra("nameCity")!=null) {
            SharedPreferences.Editor spEditor = sharedPref.edit();
            spEditor.putString("spIdCity", getIntent().getIntExtra("idCity", 27612) + " ");
            spEditor.putString("spNameCity", getIntent().getStringExtra("nameCity"));
            spEditor.commit();
        } else {
            return;
        }

    }





}
