package com.example.coronaliveupdates;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.coronaliveupdates.api.Const;
import com.example.coronaliveupdates.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        getData();
    }

    private void getData() {
        if (isNetworkAvailable(this)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("CoronaUrlList")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Const.MainBase = document.get("MainBase").toString().trim();
                                    Const.CountryList = document.get("CountryList").toString().trim();
                                    Const.SearchedCountryData = document.get("SearchedCountryData").toString().trim();

                                    handleSplashTimer();
                                }
                            } else {
                                handleError(getString(R.string.something_wrong), true);
                            }
                        }
                    });
        } else {
            handleError(getString(R.string.label_connection_error), true);
        }
    }

    private void handleError(String msg, final boolean isError) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "" + msg, Snackbar.LENGTH_LONG);
        snackbar.setDuration(9999999);
        snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();

                if (isError)
                    getData();
                else handleSplashTimer();
            }
        });
        snackbar.show();
    }

    private void handleSplashTimer() {
        if (isNetworkAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openActivityWithClearTask(MainActivity.class);
                }
            }, 2000);
        } else {
            handleError(getString(R.string.label_connection_error), false);
        }
    }
}