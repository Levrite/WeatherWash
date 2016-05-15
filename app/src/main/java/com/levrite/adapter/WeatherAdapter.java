package com.levrite.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.levrite.washcar.R;

import java.util.List;

/**
 * Created by Michael Zaytsev on 30.04.2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{


        CardView cvWeather;
        TextView tvWeatherType;
        TextView tvWeatherDate;
        ImageView ivWeatherIco;

        public ViewHolder(View itemView) {
            super(itemView);

            cvWeather = (CardView) itemView.findViewById(R.id.cvWeather);
            tvWeatherType = (TextView) itemView.findViewById(R.id.tvWeatherType);
            tvWeatherDate = (TextView) itemView.findViewById(R.id.tvWeatherDate);
            ivWeatherIco = (ImageView) itemView.findViewById(R.id.ivWeatherIco);

        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_infoweather, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
