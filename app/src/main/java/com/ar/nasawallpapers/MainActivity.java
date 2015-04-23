package com.ar.nasawallpapers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ar.nasawallpapers.models.Photo;
import com.ar.nasawallpapers.services.APODClient;
import com.ar.nasawallpapers.services.ServicesGenerator;

import rx.Observable;
import rx.functions.Action1;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {


    public static final String GOV_DATAS_URL = "https://api.data.gov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APODClient client =
                ServicesGenerator.createService(APODClient.class, GOV_DATAS_URL);

        Photo photo = client.photo("2014-01-02", true, "DEMO_KEY");

        String s = "fdsf";
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
