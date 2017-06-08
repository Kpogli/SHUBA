package com.example.android.shuba1;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.example.android.shuba1.com.example.adapters.MyFragmentPagerAdapter;
import com.example.android.shuba1.com.example.fragments.Fragment1;
import com.example.android.shuba1.com.example.fragments.Fragment2;
import com.example.android.shuba1.com.example.fragments.Fragment3;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.shuba1.MapsActivity.locationLatitudes;
import static com.example.android.shuba1.MapsActivity.locationLongitudes;
import static com.example.android.shuba1.MapsActivity.locationStops;
import static com.example.android.shuba1.MapsActivity.locationTitles;

public class NearbyStopsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ViewPager viewPager;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_stops);

        //changing statusbar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.myDarkRed));
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar_crowd);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(this.getResources().getColor(R.color.myHoloRedDark));

        //add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        initViewPager();
        initTabHost();
    }

    private void initTabHost() {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        ArrayList<String> tabNames = locationTitles;

        for (int i = 0; i < tabNames.size(); i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames.get(i));
            tabHost.getTabWidget().setStripEnabled(true);
            tabHost.getTabWidget().setBackgroundColor(this.getResources().getColor(R.color.myHoloRedDark));
            tabSpec.setIndicator(tabNames.get(i));
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedItem) {

        tabHost.setCurrentTab(selectedItem);

    }

    // viewPager listener
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // tabHost listener
    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

        HorizontalScrollView hScrollView =  (HorizontalScrollView) findViewById(R.id.h_scroll_view);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft() - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);
    }

    public class FakeContent implements TabHost.TabContentFactory {
        Context context;
        public FakeContent(Context mcontext) {
            context = mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> listFragments = new ArrayList<Fragment>();

        ArrayList<Double> tabLocationLatitude = locationLatitudes;
        ArrayList<Double> tabLocationLongitude = locationLongitudes;

        ArrayList<String> tabLocationTitle = locationTitles;
        ArrayList<LatLng> tabLocationStops = locationStops;

        for (int i = 0; i < tabLocationTitle.size(); i++) {
            listFragments.add(Fragment1.newInstance(tabLocationTitle.get(i), tabLocationLatitude.get(i), tabLocationLongitude.get(i)));
        }

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();   //close this activity and return to previous.:)
        }
        return super.onOptionsItemSelected(item);
    }
}
