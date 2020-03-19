package com.example.coronaliveupdates;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.coronaliveupdates.api.Const;
import com.example.coronaliveupdates.base.BaseActivity;
import com.example.coronaliveupdates.databinding.MainLayoutBinding;
import com.example.coronaliveupdates.model.MainApiResponse;
import com.example.coronaliveupdates.utils.AdsDismissListener;
import com.example.coronaliveupdates.utils.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    MainLayoutBinding binding;
    SessionManager sessionManager;
    ArrayList<String> countryList = new ArrayList<>();
    MainApiResponse mainApiResponse;
    int counter = 1;

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.main_layout;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        Toolbar toolbar = findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        binding = (MainLayoutBinding) getBindObj();
        sessionManager = new SessionManager(this);

        binding.htabAppbar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -binding.htabCollapseToolbar.getHeight() + binding.htabToolbar.getHeight()) {
                    binding.htabToolbar.setTitleTextColor(getResources().getColor(R.color.white));
                } else {
                    binding.htabToolbar.setTitleTextColor(getResources().getColor(R.color.appcolor));
                }
            }
        });

        binding.iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCountryWiseSearch();
            }
        });

        binding.resetFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.countryListView.setText("");
                getMainApiData();
            }
        });

        binding.imgSeftyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SeftyActivity.class);
            }
        });

        binding.cardConfirm.setOnClickListener(this);
        binding.cardRecovered.setOnClickListener(this);
        binding.cardDeaths.setOnClickListener(this);

        if (isNetworkAvailable(this)) {
            getMainApiData();
        } else {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + getString(R.string.label_connection_error), Snackbar.LENGTH_LONG);
            snackbar.setDuration(9999999);
            snackbar.setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    getMainApiData();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rateas:
                Const.RateUsApp(MainActivity.this);
                return true;
            case R.id.menu_shareapp:
                Const.shareText(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleCountryWiseSearch() {
        hideKeyboard(this);
        final String countryName = binding.countryListView.getText().toString();
        if (!TextUtils.isEmpty(countryName)) {
            counter++;
            if (counter % 2 == 0) {
                try {
                    loadAdsAndThanDisplay(new AdsDismissListener() {
                        @Override
                        public void sendCloseListener() {
                            counter = 0;
                            getSearchCountryData(countryName);
                        }
                    });
                } catch (Exception e) {
                    getSearchCountryData(countryName);
                }
            } else getSearchCountryData(countryName);
        } else {
            showErrorMSG("Please enter country");
        }
    }

    private void getSearchCountryData(final String countryName) {
        showProgressDialog("Please wait...", false);
        apiService.getCountryData("https://covid19.mathdro.id/api/countries/" + countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<MainApiResponse>() {
                    @Override
                    public void onSuccess(MainApiResponse response) {
                        try {
                            if (response != null) {
                                mainApiResponse = response;
                                binding.txtConfirmCounter.setText(mainApiResponse.getConfirmed().getValue());
                                binding.txtRecoverCounter.setText(mainApiResponse.getRecovered().getValue());
                                binding.txtDeathsCounter.setText(mainApiResponse.getDeaths().getValue());
                                binding.txtLastUpdate.setText(getResources().getString(R.string.label_last_update, mainApiResponse.getLastUpdate()));

                                binding.resetFilterLayout.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                        }

                        hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        showErrorMSG("No data found on " + countryName + " country");
                    }
                });
    }

    public void getMainApiData() {
        showProgressDialog("Please wait...", false);
        apiService.getMainApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<MainApiResponse>() {
                    @Override
                    public void onSuccess(MainApiResponse result) {
                        try {
                            if (result != null) {
                                sessionManager.setStoredData(SessionManager.APP_MAINRESPONSE, result.toJson());
                                handleMainResponse(result);
                            } else {
                                getMainResponseFromLocal();
                            }
                        } catch (Exception e) {
                            hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        getMainResponseFromLocal();
                    }
                });
    }

    private void getMainResponseFromLocal() {
        try {
            String json = sessionManager.getStoredData(SessionManager.APP_MAINRESPONSE);
            MainApiResponse response = fromJsonToGsonModel(json, new TypeToken<MainApiResponse>() {
            }.getType());
            handleMainResponse(response);
        } catch (Exception e) {
        }
    }

    private void handleMainResponse(MainApiResponse result) {
        mainApiResponse = result;
        binding.resetFilterLayout.setVisibility(View.INVISIBLE);
        binding.txtConfirmCounter.setText(mainApiResponse.getConfirmed().getValue());
        binding.txtRecoverCounter.setText(mainApiResponse.getRecovered().getValue());
        binding.txtDeathsCounter.setText(mainApiResponse.getDeaths().getValue());
        binding.txtLastUpdate.setText(getResources().getString(R.string.label_last_update, mainApiResponse.getLastUpdate()));

        if (TextUtils.isEmpty(sessionManager.getStoredData(SessionManager.APP_COUNTRIES))) {
            getCountryList(mainApiResponse.getCountries());
        } else {
            hideDialog();
            fillAutoCompletedList();
        }
    }

    public void getCountryList(String countryListUrl) {
        apiService.getCountry(countryListUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject response) {
                        try {
                            if (response != null) {
                                ArrayList<String> list = new ArrayList<>();
                                JsonObject countryJson = response.getAsJsonObject("countries");
                                for (String entry : countryJson.keySet()) {
                                    list.add(entry.trim());
                                }

                                String json = new Gson().toJson(list);
                                sessionManager.setStoredData(SessionManager.APP_COUNTRIES, json);
                                fillAutoCompletedList();
                            }
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

    private void fillAutoCompletedList() {
        String json = sessionManager.getStoredData(SessionManager.APP_COUNTRIES);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        countryList = new Gson().fromJson(json, type);
        if (countryList.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>
                    (this, android.R.layout.select_dialog_item, countryList);
            binding.countryListView.setAdapter(adapter);
        } else {
            getCountryList("https://covid19.mathdro.id/api/countries");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardConfirm:
            case R.id.cardRecovered:
            case R.id.cardDeaths:
                openActivity(MapActivity.class, mainApiResponse.toJson());
                break;
        }
    }
}