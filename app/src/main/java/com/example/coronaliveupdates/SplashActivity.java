package com.example.coronaliveupdates;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import com.example.coronaliveupdates.base.BaseActivity;
import com.google.android.material.snackbar.Snackbar;

public class SplashActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.splash_layout;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        handleSplashTimer();
    }

    private void handleSplashTimer() {
        if (isNetworkAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openActivityWithClearTask(MainActivity.class);
                }
            }, 3400);
        } else {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + getString(R.string.label_connection_error), Snackbar.LENGTH_LONG);
            snackbar.setDuration(9999999);
            snackbar.setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    handleSplashTimer();
                }
            });
            snackbar.show();
        }
    }
}