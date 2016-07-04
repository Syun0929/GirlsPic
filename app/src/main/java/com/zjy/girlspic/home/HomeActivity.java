package com.zjy.girlspic.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zjy.girlspic.R;
import com.zjy.girlspic.util.ActivityUtils;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(homeFragment == null){
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    homeFragment,R.id.contentFrame);

        }
    }
}
