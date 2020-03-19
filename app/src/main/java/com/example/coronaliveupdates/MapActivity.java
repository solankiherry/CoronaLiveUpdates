package com.example.coronaliveupdates;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.coronaliveupdates.api.Const;
import com.example.coronaliveupdates.base.BaseActivity;
import com.example.coronaliveupdates.databinding.MapLayoutBinding;
import com.example.coronaliveupdates.model.MainApiResponse;
import com.example.coronaliveupdates.model.MapDataModel;
import com.example.coronaliveupdates.utils.AdsDismissListener;
import com.example.coronaliveupdates.utils.CustomInfoWindow;
import com.example.coronaliveupdates.utils.ListAdapter;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MapActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {
    MainApiResponse model;
    ArrayList<MapDataModel> listOfMarkers = new ArrayList<>();
    public GoogleMap googleMap;
    MapLayoutBinding binding;
    AdView adView;

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.map_layout;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        binding = (MapLayoutBinding) getBindObj();
        loadBannerAds();

        if (getIntent().getExtras() != null) {
            try {
                String jsonData = getIntent().getStringExtra(Const.DATA);
                model = fromJsonToGsonModel(jsonData, new TypeToken<MainApiResponse>() {
                }.getType());

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap);
                mapFragment.getMapAsync(this);
            } catch (Exception e) {
                finish();
            }
        } else finish();

        binding.fabSwitchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.mapLayout.getVisibility() == View.VISIBLE) {
                    binding.mapLayout.setVisibility(View.GONE);
                    binding.listLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.mapLayout.setVisibility(View.VISIBLE);
                    binding.listLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        getListOfMarkerData(model.getConfirmed().getDetail());

        //getDateWiseData("2-14-2020");
    }

    private void getListOfMarkerData(String url) {
        showProgressDialog("Please wait...", false);
        apiService.getMapData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<MapDataModel>>() {
                    @Override
                    public void onSuccess(ArrayList<MapDataModel> result) {
                        try {
                            if (result != null) {
                                listOfMarkers.clear();
                                listOfMarkers.addAll(result);

                                new showMarkersFromList().execute();
                            } else showErrorMSG("Something went wrong");
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                    }
                });
    }

    private class showMarkersFromList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch (model.getSortedPos()) {
                case 1:
                    Collections.sort(listOfMarkers, new Comparator<MapDataModel>() {
                        public int compare(MapDataModel obj1, MapDataModel obj2) {
                            return obj1.getConfirmedAsInt().compareTo(obj1.getConfirmedAsInt());

                            // ## Descending order
                            // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                            // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                        }
                    });
                    break;
                case 2:
                    Collections.sort(listOfMarkers, new Comparator<MapDataModel>() {
                        public int compare(MapDataModel obj1, MapDataModel obj2) {
                            return obj1.getRecoveredAsInt().compareTo(obj1.getRecoveredAsInt());
                        }
                    });
                    break;
                case 3:
                    Collections.sort(listOfMarkers, new Comparator<MapDataModel>() {
                        public int compare(MapDataModel obj1, MapDataModel obj2) {
                            return obj1.getDeathsAsInt().compareTo(obj1.getDeathsAsInt());
                        }
                    });
                    break;
            }


            for (int i = 0; i < listOfMarkers.size(); i++) {
                try {
                    final MapDataModel model = listOfMarkers.get(i);
                    final MarkerOptions markerOpt = new MarkerOptions();
                    markerOpt.position(new LatLng(model.getLatAsDouble(), model.getLongAsDouble()))
                            .title(model.getCountryRegion())
                            .snippet(model.toJson())
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pinred));

                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            googleMap.addMarker(markerOpt);

                            if (finalI == 0) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(model.getLatAsDouble(), model.getLongAsDouble())));
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            hideDialog();
            CustomInfoWindow adapter = new CustomInfoWindow(MapActivity.this);
            googleMap.setInfoWindowAdapter(adapter);

            ListAdapter listAdapter = new ListAdapter(MapActivity.this, listOfMarkers);
            binding.setListAdapter(listAdapter);
            switch (model.getSortedPos()) {
                case 1:
                    binding.txtSortedby.setText(getString(R.string.soarted_by, getString(R.string.confirmed)));
                    break;
                case 2:
                    binding.txtSortedby.setText(getString(R.string.soarted_by, getString(R.string.recovered)));
                    break;
                case 3:
                    binding.txtSortedby.setText(getString(R.string.soarted_by, getString(R.string.deaths)));
                    break;
            }
        }
    }

  /*  private void getDateWiseData(final String selDate) {
        showProgressDialog("Please wait...", false);
        apiService.getSelDateWiseData("https://covid19.mathdro.id/api/daily/" + selDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<MapDataModel>>() {
                    @Override
                    public void onSuccess(ArrayList<MapDataModel> result) {
                        try {
                            if (result != null) {
                                listOfMarkers.clear();
                                listOfMarkers.addAll(result);
                                new showMarkersFromList().execute();
                            } else showErrorMSG("Something went wrong");
                        } catch (Exception e) {
                        }
                        hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                    }
                });
    }*/

    public void loadBannerAds() {
        adView = new AdView(this, getString(R.string.fb_bannerId), AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            loadAdsAndThanDisplay(new AdsDismissListener() {
                @Override
                public void sendCloseListener() {
                    finish();
                }
            });
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}