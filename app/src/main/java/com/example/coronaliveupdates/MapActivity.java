package com.example.coronaliveupdates;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.coronaliveupdates.api.Const;
import com.example.coronaliveupdates.base.BaseActivity;
import com.example.coronaliveupdates.databinding.MapLayoutBinding;
import com.example.coronaliveupdates.model.MainApiResponse;
import com.example.coronaliveupdates.model.MapDataModel;
import com.example.coronaliveupdates.utils.AdsDismissListener;
import com.example.coronaliveupdates.utils.CustomInfoWindow;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MapActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {
    MainApiResponse model;
    ArrayList<MapDataModel> listOfMarkers = new ArrayList<>();
    public GoogleMap googleMap;
    MapLayoutBinding binding;

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
                        hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                    }
                });
    }

    public class showMarkersFromList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
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
            CustomInfoWindow adapter = new CustomInfoWindow(MapActivity.this);
            googleMap.setInfoWindowAdapter(adapter);
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
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
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