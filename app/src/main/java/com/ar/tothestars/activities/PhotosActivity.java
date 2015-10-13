package com.ar.tothestars.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ar.tothestars.R;
import com.ar.tothestars.adapters.CategoriesAdapter;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class PhotosActivity extends AppCompatActivity implements View.OnClickListener,
    ViewPager.OnPageChangeListener, CategoriesAdapter.Listener {

    private ViewPager mViewPager;
    private CategoriesAdapter mAdapter;
    private View mBrowseButton;
    private View mFavoritesButton;
    private View mButtonsContainer;
    private View mHeaderShadow;
    private boolean mButtonsContainerHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new CategoriesAdapter(this);
        mAdapter.setListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);


        mButtonsContainer = findViewById(R.id.buttons_container);
        mHeaderShadow = findViewById(R.id.toolbar_shadow);
        mBrowseButton = findViewById(R.id.browse_button);
        mFavoritesButton = findViewById(R.id.favorites_button);

        mBrowseButton.setOnClickListener(this);
        mFavoritesButton.setOnClickListener(this);

        mFavoritesButton.setSelected(true);
        mBrowseButton.setSelected(true);

        mFavoritesButton.setSelected(false);
        mBrowseButton.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.browse_button:
                mViewPager.setCurrentItem(0);
                mBrowseButton.setSelected(true);
                mFavoritesButton.setSelected(false);
                break;

            case R.id.favorites_button:
                mViewPager.setCurrentItem(1);
                mBrowseButton.setSelected(false);
                mFavoritesButton.setSelected(true);
                break;

            default:
        }
    }

    @Override
    public void onPageSelected(int position) {
        showButtonsContainer();

        switch (position) {
            case 0:
                mBrowseButton.setSelected(true);
                mFavoritesButton.setSelected(false);
                break;
            case 1:
                mBrowseButton.setSelected(false);
                mFavoritesButton.setSelected(true);
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
        if (dy > 0 && recyclerScrollY > mButtonsContainer.getHeight()) {
            hideButtonsContainer();
        } else if (dy < 0) {
            showButtonsContainer();
        }
    }

    private void hideButtonsContainer() {
        if (!mButtonsContainerHidden) {
            mButtonsContainerHidden = true;
            mButtonsContainer.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.main_menu_buttons_height));
            mHeaderShadow.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.main_menu_buttons_height));
        }
    }

    private void showButtonsContainer() {
        if (mButtonsContainerHidden) {
            mButtonsContainerHidden = false;
            mButtonsContainer.animate().translationY(0);
            mHeaderShadow.animate().translationY(0);
        }
    }
}
