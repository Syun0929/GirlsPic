package com.zjy.girlspic.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zjy.girlspic.R;
import com.zjy.girlspic.data.Image;
import com.zjy.girlspic.util.ActivityUtils;


/**
 * Created by jiyoung.tsang on 16/7/2.
 */
public class DetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_act);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final Image image = (Image)getIntent().getSerializableExtra(Image.TAG);

        ab.setTitle(image.title);

        DetailsFragment detailsFragment = (DetailsFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(detailsFragment == null){
            detailsFragment = DetailsFragment.newInstance();
            Bundle bundle =new Bundle();
            bundle.putString(DetailsFragment.DETAILS_URL,image.link);
            detailsFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),detailsFragment,R.id.contentFrame);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
