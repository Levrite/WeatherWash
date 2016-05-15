package com.levrite.washcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.levrite.adapter.CityAdapter;
import com.levrite.data.CityData;
import com.levrite.data.CountryData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.levrite.utility.CitySAXXMLParser.parseCity;
import static com.levrite.utility.CountrySAXXMLParser.parseCountry;


public class CityActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{


    Button btnNext;
    Spinner spnrCity;
    Spinner spnrCountry;
    List<CityData> cityDataList = null;
    List<CountryData> countryDataList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        btnNext = (Button) findViewById(R.id.btnNext);
        spnrCity = (Spinner) findViewById(R.id.spnrCity);
        spnrCountry = (Spinner) findViewById(R.id.spnrCountry);

        btnNext.setOnClickListener(this);

        spnrCountry.setOnItemSelectedListener(this);

        /*
        Parse cities.xml from assets folder and set data to spinner
         */
        try {
            countryDataList = parseCountry(getAssets().open("cities.xml"));
            ArrayAdapter<?> countryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countryDataList);
            countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnrCountry.setAdapter(countryAdapter);

            cityDataList = parseCity(getAssets().open("cities.xml"), spnrCountry.getSelectedItem().toString());
            Collections.sort(cityDataList, new Comparator<CityData>() {
                @Override
                public int compare(CityData lhs, CityData rhs) {
                    return lhs.getNameCity().compareTo(rhs.getNameCity());
                }
            });
            CityAdapter cityAdapter = new CityAdapter(this, R.layout.item_city, cityDataList);
            spnrCity.setAdapter(cityAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
       //checkCity();

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CityActivity.this, MainActivity.class);
        int tempId = (int) spnrCity.getSelectedItemId();
        intent.putExtra("nameCity", cityDataList.get(tempId).getNameCity());
        intent.putExtra("idCity", cityDataList.get(tempId).getIdCity());
        Log.d("WEATHER ID CITY", cityDataList.get(tempId).getIdCity() + "");
        startActivity(intent);

    }

    //Метод для одноразового показа CityActivity
    public void checkCity() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       try {
            cityDataList = parseCity(getAssets().open("cities.xml"), spnrCountry.getSelectedItem().toString());
            Collections.sort(cityDataList, new Comparator<CityData>() {
                @Override
                public int compare(CityData lhs, CityData rhs) {
                    return lhs.getNameCity().compareTo(rhs.getNameCity());
                }
            });
            CityAdapter cityAdapter = new CityAdapter(this, R.layout.item_city, cityDataList);
            spnrCity.setAdapter(cityAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
