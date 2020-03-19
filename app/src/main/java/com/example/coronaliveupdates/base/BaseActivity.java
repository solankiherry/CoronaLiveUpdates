package com.example.coronaliveupdates.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.example.coronaliveupdates.R;
import com.example.coronaliveupdates.api.ApiClient;
import com.example.coronaliveupdates.api.ApiService;
import com.example.coronaliveupdates.api.Const;
import com.example.coronaliveupdates.utils.AdsDismissListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public abstract class BaseActivity extends AppCompatActivity {
    public ApiService apiService;
    private ProgressDialog progressDialog;
    ViewDataBinding bingObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(getContentView());
        bingObj = DataBindingUtil.setContentView(this, getContentView());
        apiService = ApiClient.getClient().create(ApiService.class);
        onViewReady(savedInstanceState, getIntent());
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
    }

    @CallSuper
    protected void onLocationEnableRequestSuccess() {
    }

    protected abstract int getContentView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void openActivity(Class destination) {
        startActivity(new Intent(this, destination));
    }

    protected void openActivity(Class destination, String data) {
        Intent intent = new Intent(this, destination);
        if (!TextUtils.isEmpty(data)) {
            intent.putExtra(Const.DATA, data);
            startActivity(intent);
        }
    }

    public <T> T fromJsonToGsonModel(String json, Type type) {
        return new Gson().fromJson(json, type);
    }

    protected void openActivityWithClearTask(Class destination) {
        Intent intent = new Intent(this, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public ViewDataBinding getBindObj() {
        return bingObj;
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    protected void showProgressDialog(String message, boolean isCancelabe) {
        try {
            if (progressDialog == null) {
                progressDialog = Const.getProgressDialog(BaseActivity.this, message, isCancelabe);
            }
            progressDialog.show();
        } catch (Exception e) {
        }
    }

    public void setStatusBarBG(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    protected void hideDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    protected boolean isNetworkAvailable(Context context) {
        return Const.isNetworkAvailable(context);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showErrorMSG(String errorMsg) {
        Snackbar.make(findViewById(android.R.id.content), "" + errorMsg, Snackbar.LENGTH_LONG).setDuration(BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public void hideKeyboard(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public void loadAdsAndThanDisplay(final AdsDismissListener adsDismissListener) {
        if (!isNetworkAvailable(this)) {
            adsDismissListener.sendCloseListener();
            return;
        }

        showProgressDialog("Please wait...", false);
        final InterstitialAd fbInterstitialAd = new InterstitialAd(this, this.getString(R.string.fb_interstitialId));
        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                adsDismissListener.sendCloseListener();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                adsDismissListener.sendCloseListener();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                    fbInterstitialAd.show();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        });
        fbInterstitialAd.loadAd();
    }
}