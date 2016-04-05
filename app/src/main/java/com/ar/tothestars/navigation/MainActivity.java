package com.ar.tothestars.navigation;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ar.tothestars.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
    ViewPager.OnPageChangeListener, CategoriesAdapter.Listener {

    private ViewPager viewPager;
    private CategoriesAdapter adapter;
    private View browseButton;
    private View favoritesButton;
    private View buttonsContainer;
    private View headerShadow;
    private boolean buttonsContainerHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new CategoriesAdapter(this);
        adapter.setListener(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);


        buttonsContainer = findViewById(R.id.buttons_container);
        headerShadow = findViewById(R.id.toolbar_shadow);
        browseButton = findViewById(R.id.browse_button);
        favoritesButton = findViewById(R.id.favorites_button);

        browseButton.setOnClickListener(this);
        favoritesButton.setOnClickListener(this);

        favoritesButton.setSelected(true);
        browseButton.setSelected(true);

        favoritesButton.setSelected(false);
        browseButton.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.browse_button:
                viewPager.setCurrentItem(0);
                browseButton.setSelected(true);
                favoritesButton.setSelected(false);
                break;

            case R.id.favorites_button:
                viewPager.setCurrentItem(1);
                browseButton.setSelected(false);
                favoritesButton.setSelected(true);
                break;

            default:
        }
    }

    @Override
    public void onPageSelected(int position) {
        showButtonsContainer();

        switch (position) {
            case 0:
                browseButton.setSelected(true);
                favoritesButton.setSelected(false);
                break;
            case 1:
                browseButton.setSelected(false);
                favoritesButton.setSelected(true);
                break;
            default:

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onRecyclerScrolled(int dy, int recyclerScrollY) {
        if (dy > 0 && recyclerScrollY > buttonsContainer.getHeight()) {
            hideButtonsContainer();
        } else if (dy < 0) {
            showButtonsContainer();
        }
    }

    private void hideButtonsContainer() {
        if (!buttonsContainerHidden) {
            buttonsContainerHidden = true;
            buttonsContainer.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.main_menu_buttons_height));
            headerShadow.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.main_menu_buttons_height));
        }
    }

    private void showButtonsContainer() {
        if (buttonsContainerHidden) {
            buttonsContainerHidden = false;
            buttonsContainer.animate().translationY(0);
            headerShadow.animate().translationY(0);
        }
    }
}
