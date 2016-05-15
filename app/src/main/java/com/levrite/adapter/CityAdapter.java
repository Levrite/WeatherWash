package com.levrite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.levrite.data.CityData;
import com.levrite.washcar.R;

import java.util.List;

/**
 * Created by Michael Zaytsev on 01.05.2016.
 */
public class CityAdapter extends ArrayAdapter<CityData> {

    List<CityData> cityDatas;

    public CityAdapter(Context context, int resource, List<CityData> cityDatas) {
        super(context, resource, cityDatas);
        this.cityDatas = cityDatas;
    }

    public View getCustomView(int position, View converterView, ViewGroup parent){

        CityData cityData = cityDatas.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.item_city, parent, false);

        TextView tvId = (TextView) v.findViewById(R.id.txtCityId);
        TextView tvNameCity = (TextView) v.findViewById(R.id.txtCityName);

        tvId.setText(cityData.getIdCity());
        tvNameCity.setText(cityData.getNameCity());

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        CityData cityData = cityDatas.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);

        TextView tvNameCity = (TextView) v.findViewById(R.id.tvCityName);

        tvNameCity.setText(cityData.getNameCity());

        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
