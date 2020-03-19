package com.example.coronaliveupdates.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.coronaliveupdates.R;
import com.example.coronaliveupdates.model.MapDataModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindow(Activity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);
        MapDataModel merchant = null;
        TextView txtCountryName = view.findViewById(R.id.txtCountryName);
        TextView txtStateName = view.findViewById(R.id.txtStateName);
        TextView txtConfirmedValue = view.findViewById(R.id.txtConfirmedValue);
        TextView txtRecoveredValue = view.findViewById(R.id.txtRecoveredValue);
        TextView txtDeathsValue = view.findViewById(R.id.txtDeathsValue);
        TextView txtActiveValue = view.findViewById(R.id.txtActiveValue);

        try {
            merchant = new Gson().fromJson(marker.getSnippet(), new TypeToken<MapDataModel>() {
            }.getType());

            if (!TextUtils.isEmpty(merchant.getCountryRegion())) {
                txtCountryName.setText(merchant.getCountryRegion());
            }
            if (!TextUtils.isEmpty(merchant.getProvinceState())) {
                txtStateName.setText(merchant.getProvinceState());
            }
            if (!TextUtils.isEmpty(merchant.getConfirmed())) {
                txtConfirmedValue.setText(merchant.getConfirmed());
            }
            if (!TextUtils.isEmpty(merchant.getRecovered())) {
                txtRecoveredValue.setText(merchant.getRecovered());
            }
            if (!TextUtils.isEmpty(merchant.getDeaths())) {
                txtDeathsValue.setText(merchant.getDeaths());
            }
            if (!TextUtils.isEmpty(merchant.getActive())) {
                txtActiveValue.setText(merchant.getActive());
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}