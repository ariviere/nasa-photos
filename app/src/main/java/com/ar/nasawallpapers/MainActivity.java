package com.ar.nasawallpapers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ar.nasawallpapers.models.Photo;
import com.ar.nasawallpapers.services.APODClient;
import com.ar.nasawallpapers.services.APODParser;
import com.ar.nasawallpapers.services.RetrofitTools;
import com.ar.nasawallpapers.services.ServicesGenerator;

import rx.android.observables.AndroidObservable;
import rx.functions.Action1;


public class MainActivity extends ActionBarActivity {


    public static final String GOV_DATAS_URL = "https://api.data.gov";

    private Photo mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APODClient client =
                ServicesGenerator.createService(APODClient.class, GOV_DATAS_URL);

        AndroidObservable.bindActivity(this,
                client.getPhoto("2014-01-02", true, "DEMO_KEY")
                        .map(RetrofitTools.transformToString)
                        .onErrorReturn(RetrofitTools.errorHandling)
                        .map(APODParser.PARSE_APOD))
                .subscribe(new Action1<Photo>() {
                    @Override
                    public void call(Photo photo) {
                        mPhoto = photo;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
