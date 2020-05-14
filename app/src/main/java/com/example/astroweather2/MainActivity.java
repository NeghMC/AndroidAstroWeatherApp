package com.example.astroweather2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.astroweather2.databinding.ActivityMainBinding;
import com.example.astroweather2.fragments.CurrentWeatherFragment;
import com.example.astroweather2.fragments.ForecastWeatherFragment;
import com.example.astroweather2.fragments.MoonFragment;
import com.example.astroweather2.fragments.SunFragment;
import com.example.astroweather2.locatinactivity.locationsActivity;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String SETTINGS_UNIT = "unit";
    public static final String SETTINGS_LOCATION = "location_id";
    public static final String ACTIVITY_BACKPRESS = "backpress";

    ActivityMainBinding mBind;
    ApplicationViewModel appData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // bind view
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set action bar
        setSupportActionBar((Toolbar)mBind.toolbar);

        // setting viewmodel
        mBind.setLifecycleOwner(this);

        // setting pager
        mBind.container.setAdapter(
                new FragmentStatePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

                    @Override
                    public Fragment getItem(int position) {
                        switch(position) {
                            case 0:
                                return new CurrentWeatherFragment();
                            case 1:
                                return new ForecastWeatherFragment();
                            case 2:
                                return new SunFragment();
                            case 3:
                                return new MoonFragment();
                            default:
                                return null;
                        }
                    }

                    @Override
                    public int getCount() {
                        return 4;
                    }
                });

        // get viewmodel
        appData = new ViewModelProvider(this).get(ApplicationViewModel.class);

        //
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        int id = prefs.getInt(SETTINGS_LOCATION, 0);
        if(id == 0) {
            openFavoriteListActivity(false);
        } else {
            appData.refreshWeatherData(id, prefs.getInt(SETTINGS_UNIT, 0));
            appData.refreshAstronomicData(id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // create menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                appData.refreshWeatherData(prefs.getInt(SETTINGS_LOCATION, 0), prefs.getInt(SETTINGS_UNIT, 0));
                return true;
            case R.id.menu_metrics:
                if(item.isChecked()) {
                    item.setChecked(false);
                    getPreferences(Context.MODE_PRIVATE).edit().putInt(SETTINGS_UNIT, Consts.UNIT_METRICS).apply();
                    Log.d("MENU", "onOptionsItemSelected: metrics");
                } else {
                    item.setChecked(true);
                    getPreferences(Context.MODE_PRIVATE).edit().putInt(SETTINGS_UNIT, Consts.UNIT_IMPERIAL).apply();
                    Log.d("MENU", "onOptionsItemSelected: imperial");
                }
                SharedPreferences prefs1 = getPreferences(Context.MODE_PRIVATE);
                appData.refreshWeatherData(prefs1.getInt(SETTINGS_LOCATION, 0), prefs1.getInt(SETTINGS_UNIT, 0));
                return true;
            case R.id.menu_favorite:
                openFavoriteListActivity(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            if(resultCode != 0) {
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                prefs.edit().putInt(SETTINGS_LOCATION, resultCode).apply();
                appData.refreshWeatherData(resultCode, prefs.getInt(SETTINGS_UNIT, 0));
                appData.refreshAstronomicData(resultCode);
            }

        }
    }

    private void openFavoriteListActivity(boolean allowBackpress) {
        Intent favoriteActivity = new Intent(this, locationsActivity.class);
        favoriteActivity.putExtra(ACTIVITY_BACKPRESS, allowBackpress);
        startActivityForResult(favoriteActivity, REQUEST_CODE);
    }
}
