package com.example.astroweather2.locatinactivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.astroweather2.MainActivity;
import com.example.astroweather2.R;
import com.example.astroweather2.databinding.ActivityFavoriteLocationsBinding;
import com.example.astroweather2.datatypes.GeoLocation;
import com.example.astroweather2.room.ApplicationDatabase;
import com.example.astroweather2.utils.ResultCallback;
import com.example.astroweather2.webservice.WeatherApiService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class locationsActivity extends AppCompatActivity {
    private ActivityFavoriteLocationsBinding mBind;
    private LocationsAdapter mAdapter;

    ArrayList<GeoLocation> favoriteLocations;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBind = DataBindingUtil.setContentView(this, R.layout.activity_favorite_locations);
        mBind.recyclerView.setHasFixedSize(true); // wont change in size
        mBind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LocationsAdapter();
        mBind.recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new LocationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GeoLocation clicked = mAdapter.get(position);
                if(favoriteLocations.contains(clicked)) {
                    setResult(clicked.id);
                    finish();
                    /*new DeleteFavoriteFromDatabase().execute(clicked);
                    favoriteLocations.remove(clicked);
                    mAdapter.notifyDataSetChanged();*/
                } else {
                    new InsertFavoriteToDatabase().execute(clicked);
                    favoriteLocations.add(clicked);
                    mAdapter.set(favoriteLocations);
                    mBind.searchText.setText("");
                }
            }

            @Override
            public void onDeleteClick(int position) {
                new DeleteFavoriteFromDatabase().execute(mAdapter.get(position));
                favoriteLocations.remove(mAdapter.get(position));
                mAdapter.notifyDataSetChanged();
            }
        });

        new GetFavoriteFromDatabase(mAdapter, new WeakReference<locationsActivity>(this)).execute(); // update favorite list

        mBind.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = mBind.searchText.getText().toString();

                if(search.length() == 0) {
                    mAdapter.set(favoriteLocations);
                } else {
                    mBind.progressBar.setVisibility(ProgressBar.VISIBLE);
                    WeatherApiService.getSearchList(search, new ResultCallback<ArrayList<GeoLocation>>() {
                        @Override
                        public void onSuccess(ArrayList<GeoLocation> result, String warning) {
                            mBind.progressBar.setVisibility(ProgressBar.INVISIBLE);
                            mAdapter.set((ArrayList)result);
                        }

                        @Override
                        public void onFailture(String msg) {
                            mBind.progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Toast.makeText(locationsActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!getIntent().getBooleanExtra(MainActivity.ACTIVITY_BACKPRESS, false)) {
            Toast.makeText(this, "You have to chose localisation first time", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private static class GetFavoriteFromDatabase extends AsyncTask<Void, Void, ArrayList<GeoLocation>> {
        LocationsAdapter adapter;
        WeakReference<locationsActivity> activity;

        public GetFavoriteFromDatabase(LocationsAdapter adapter, WeakReference<locationsActivity> activity) {
            this.adapter = adapter;
            this.activity = activity;
        }

        @Override
        protected ArrayList<GeoLocation> doInBackground(Void... voids) {
            return (ArrayList<GeoLocation>)ApplicationDatabase.getInstance().locationDao().getAll();
        }

        @Override
        protected void onPostExecute(ArrayList<GeoLocation> geoLocations) {
            activity.get().favoriteLocations = geoLocations;
            adapter.set(geoLocations);
        }
    }

    private static class InsertFavoriteToDatabase extends AsyncTask<GeoLocation, Void, Void> {
        @Override
        protected Void doInBackground(GeoLocation... geoLocations) {
            ApplicationDatabase.getInstance().locationDao().insert(geoLocations[0]);
            return null;
        }
    }

    private static class DeleteFavoriteFromDatabase extends AsyncTask<GeoLocation, Void, Void> {
        @Override
        protected Void doInBackground(GeoLocation... geoLocations) {
            ApplicationDatabase.getInstance().locationDao().delete(geoLocations[0]);
            return null;
        }
    }
}
