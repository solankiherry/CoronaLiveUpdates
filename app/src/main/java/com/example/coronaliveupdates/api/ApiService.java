package com.example.coronaliveupdates.api;

import com.example.coronaliveupdates.model.MainApiResponse;
import com.example.coronaliveupdates.model.MapDataModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("{dataUrl}")
    Single<MainApiResponse> getMainApi(@Path(value = "dataUrl", encoded = true) String dataUrl);

    @GET("{countryUrl}")
    Single<JsonObject> getCountry(@Path(value = "countryUrl", encoded = true) String countryUrl);

    @GET("{countryNameWithUrl}")
    Single<MainApiResponse> getCountryData(@Path(value = "countryNameWithUrl", encoded = true) String countryNameWithUrl);

   /* @GET("{selDate}")
    Single<ArrayList<MapDataModel>> getSelDateWiseData(@Path(value = "selDate", encoded = true) String selDate);*/

    @GET("{mapDataUrl}")
    Single<ArrayList<MapDataModel>> getMapData(@Path(value = "mapDataUrl", encoded = true) String mapDataUrl);

}