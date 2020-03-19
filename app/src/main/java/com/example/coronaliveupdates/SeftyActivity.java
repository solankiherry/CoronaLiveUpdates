package com.example.coronaliveupdates;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.coronaliveupdates.base.BaseActivity;
import com.example.coronaliveupdates.databinding.SeftyLayoutBinding;
import com.example.coronaliveupdates.utils.AdsDismissListener;

public class SeftyActivity extends BaseActivity {

    SeftyLayoutBinding binding;

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.sefty_layout;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        binding = (SeftyLayoutBinding) getBindObj();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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