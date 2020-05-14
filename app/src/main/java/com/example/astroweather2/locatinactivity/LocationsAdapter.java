package com.example.astroweather2.locatinactivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.astroweather2.R;
import com.example.astroweather2.databinding.LocationCardBinding;
import com.example.astroweather2.datatypes.GeoLocation;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder> {

    private ArrayList<GeoLocation> locationList;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // pass card layout
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { // method goes when view is binded to element
        GeoLocation location = locationList.get(position);

        holder.mBind.name.setText(location.country);
        holder.mBind.description.setText(location.name);
        Log.d("aaaaa", "onBindViewHolder: uploaded item");
    }

    @Override
    public int getItemCount() {
        if(locationList == null)
            return 0;
        else
            return locationList.size();
    }

    public void add(GeoLocation location) {
        locationList.add(location);
    }

    public void add(ArrayList<GeoLocation> list) {
        locationList.addAll(list);
    }

    public void set(ArrayList<GeoLocation> list) {
        locationList = list;
        notifyDataSetChanged();
    }

    public GeoLocation get(int index) {
        return locationList.get(index);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LocationCardBinding mBind;

        ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mBind = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onItemClick(position);
                }
            });
            mBind.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onDeleteClick(position);
                }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
}
