package com.example.coronaliveupdates.api;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.example.coronaliveupdates.R;

public class Const {
    public static final String BASE_URL = "https://demourl/";
    public static final String DATA = "data";
    public static String MainBase = "";
    public static String CountryList = "data";
    public static String SearchedCountryData = "data";

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void RateUsApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void shareText(Context context) {
        try {
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            String shareText = "Coronavirus: A deadly virus has killed thousands in a global pandemic, Read latest news about affected people counts, countries affected and more... Download now https://play.google.com/store/apps/details?id=" + context.getPackageName();
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            intent.setType("text/*");
            context.startActivity(Intent.createChooser(intent, "Share"));
        } catch (Exception e) {
        }
    }
}