package com.levrite.washcar.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.*;
import com.levrite.washcar.R;

public class IntroActivity extends AppIntro2 {


    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro_title_welcome), getResources().getString(R.string.intro_desc_welcome), R.drawable.intro_welcome, Color.parseColor("#4CAF50")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro_title_geo), getResources().getString(R.string.intro_desc_geo), R.drawable.intro_location, Color.parseColor("#4CAF50")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro_title_app), getResources().getString(R.string.intro_desc_app), R.drawable.intro_app, Color.parseColor("#4CAF50")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro_title_enjoy), getResources().getString(R.string.intro_desc_enjoy), R.drawable.intro_enjoy, Color.parseColor("#4CAF50")));

        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);

    }


    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {

    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
