package com.levrite.washcar.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.levrite.washcar.R;
import com.levrite.washcar.com.levrite.washcar.storage.InternalStorage;
import com.levrite.washcar.data.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {

    //Unique key for get weather from OpenWeatherMap
    private static final String WEATHER_API_KEY = "d5013c19e34287db6b034d74ab4604c3";

    //Unique key for read and write weatherData from Cache
    public static final String CACHE_KEY = "CACHE_KEY";

    //Location variable
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    //Views
    ProgressBar mProgressBar;
    ImageView mImageWeather;
    TextView mTextWeather;
    TextView mTextLong;
    TextView mTextLat;
    TextView mTextStat;
    Button mButtonFindWeather;
    Button mButtonUpdateData;

    SharedPreferences getPref;

    private ArrayList<WeatherData> mWeatherDatas;
    GetWeather getWeatherTask = new GetWeather();

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mImageWeather = (ImageView) findViewById(R.id.imageWeather);
        mTextWeather = (TextView) findViewById(R.id.textWeather);
        mTextLong = (TextView) findViewById(R.id.tvLong);
        mTextLat = (TextView) findViewById(R.id.tvLat);
        mTextStat = (TextView) findViewById(R.id.tvStatSum);
        mButtonFindWeather = (Button) findViewById(R.id.btnFindWeather);
        mButtonUpdateData = (Button) findViewById(R.id.btnUpdateData);




        showIntroOnce();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean haveData = getPref.getBoolean("haveData", true);
        if(haveData){
            new AnalyzeWeather().execute();
        } else {
            if (isNetworkAviable()) {
                {
                    initGoogleApiClient();
                    mGoogleApiClient.connect();
                }
            } else {
                mProgressBar.setVisibility(View.GONE);
                mImageWeather.setVisibility(View.VISIBLE);
                mButtonFindWeather.setVisibility(View.GONE);
                mButtonUpdateData.setVisibility(View.VISIBLE);
                mImageWeather.setImageResource(R.drawable.error);
                mTextWeather.setText(R.string.error_connect);
            }
        }
    }



    @Override
    protected void onStop() {
        if(mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        super.onStop();
    }



    public void findWeather(View view) {
        Uri gmmMapIntentUri = Uri.parse("geo:0,0?q=" + R.string.carwash);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmMapIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void updateData(View view){
        if(isNetworkAviable()){
            initGoogleApiClient();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (isEnableLocation()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                double longitude = mLastLocation.getLongitude();
                double latitude = mLastLocation.getLatitude();
                String url = String.format("http://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s",
                        latitude, longitude, WEATHER_API_KEY);
                mTextLong.setText("Long = " + longitude);
                mTextLat.setText("Lati = " + latitude);
                if (getWeatherTask.getStatus() != AsyncTask.Status.FINISHED) {
                    getWeatherTask.execute(url);
                }
            }
        } else {
            dialogEnableLocation();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Init googleApiClient
     */
    public void initGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApiIfAvailable(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

    }

    /**
     *
     * @return true if location aviable
     */
    public boolean isEnableLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return true if network aviable
     */
    public boolean isNetworkAviable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public void dialogEnableLocation() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case MainActivity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case MainActivity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        onConnected(null);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(MainActivity.this, 1000);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    private void showIntroOnce() {
        getPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean isFirstStart = getPref.getBoolean("firstStart", true);

        if (isFirstStart) {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);

            SharedPreferences.Editor editor = getPref.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }
    }


    /**
     * Class for get data from url
     */
    private class GetWeather extends AsyncTask<String, Void, String> {

        /* Actions when running doInBackground
         * Hide ImageView and show ProgressBar*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mImageWeather.setVisibility(View.GONE);
            mTextWeather.setText(getResources().getText(R.string.connecting));
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder stringBuilder = null;

            try {
                URL weatherUrl = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) weatherUrl.openConnection();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {

                    stringBuilder.append(inputString);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject dataJsonObject = null;
            WeatherData weatherData = null;
            mWeatherDatas = new ArrayList<WeatherData>();

            try {
                dataJsonObject = new JSONObject(s);
                JSONArray jsonArrayList = dataJsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject weather = jsonArrayList.getJSONObject(i);

                    JSONArray jsonArrayWeather = weather.getJSONArray("weather");
                    for (int z = 0; z < jsonArrayWeather.length(); z++) {
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(z);

                        weatherData = new WeatherData();
                        weatherData.setWeatherType(jsonObjectWeather.getString("main"));
                        mWeatherDatas.add(weatherData);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                InternalStorage.writeObject(MainActivity.this, CACHE_KEY, mWeatherDatas);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = getPref.edit();
            editor.putBoolean("haveData", false);
            editor.apply();
            new AnalyzeWeather().execute();
        }
    }


    /**
     * Class for analyze weather
     */
    private class AnalyzeWeather extends AsyncTask<Double, Void, Double> {

        @Override
        protected Double doInBackground(Double... params) {
            double mWeatherSum = 1;
            double count = 1;
            try {
                mWeatherDatas = (ArrayList<WeatherData>) InternalStorage.readObject(MainActivity.this, CACHE_KEY);

                for (WeatherData weatherData : mWeatherDatas) {
                    Log.d("WEATHER", weatherData.getWeatherType() + " " + count);
                    if(count < 6){
                    if (weatherData.getWeatherType().equals("Rain")) {
                        mWeatherSum = 100;
                    } else if(weatherData.getWeatherType().equals("Clouds")){
                        mWeatherSum += 0.1;
                    }
                    } else if (weatherData.getWeatherType().equals("Rain") && count > 6 && count < 14) {
                            mWeatherSum = 60;
                        }
                     else if (count % 6 == 0 && weatherData.getWeatherType().equals("Rain")) {
                            mWeatherSum += 1;
                        }
                    }
                    count += 1;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            return mWeatherSum;
        }

        @Override
        protected void onPostExecute(Double weather) {
            super.onPostExecute(weather);
            mTextStat.setText("Weather sum =" + weather);
            mImageWeather.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mButtonFindWeather.setVisibility(View.VISIBLE);
            mButtonUpdateData.setVisibility(View.GONE);
            if (weather < 2) {
                mTextWeather.setText(R.string.good_weather);
                mImageWeather.setImageResource(R.drawable.sun);
            } else if (weather <= 5 && weather >= 2) {
                mTextWeather.setText(R.string.fine_weather);
                mImageWeather.setImageResource(R.drawable.cloud);
            } else if (weather >= 5 && weather <= 59) {
                mTextWeather.setText(R.string.bad_weather);
                mImageWeather.setImageResource(R.drawable.lightning);
            } else if (weather >= 100) {
                mTextWeather.setText(R.string.rain_today);
                mImageWeather.setImageResource(R.drawable.cloud);
            } else if (weather >= 60 && weather <=100) {
                mTextWeather.setText(R.string.rain_tomorrow);
                mImageWeather.setImageResource(R.drawable.cloud);
            }

        }
    }

}



