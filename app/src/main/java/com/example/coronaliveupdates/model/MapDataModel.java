package com.example.coronaliveupdates.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapDataModel implements Serializable {
    private String countryRegion;

    private String active;

    private String confirmed;

    private String provinceState;

    @SerializedName("long")
    private String lng;

    private String recovered;

    private String lat;

    private String deaths;

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public Integer getConfirmedAsInt() {
        try {
            return Integer.parseInt(confirmed);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public String getLong() {
        return lng;
    }

    public Double getLongAsDouble() {
        try {
            return Double.parseDouble(lng);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void setLong(String lng) {
        this.lng = lng;
    }

    public String getRecovered() {
        return recovered;
    }

    public Integer getRecoveredAsInt() {
        try {
            return Integer.parseInt(recovered);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getLat() {
        return lat;
    }

    public Double getLatAsDouble() {
        try {
            return Double.parseDouble(lat);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDeaths() {
        return deaths;
    }

    public Integer getDeathsAsInt() {
        try {
            return Integer.parseInt(deaths);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    @Override
    public String toString() {
        return "MapDataModel{" +
                "countryRegion='" + countryRegion + '\'' +
                ", active='" + active + '\'' +
                ", confirmed='" + confirmed + '\'' +
                ", provinceState='" + provinceState + '\'' +
                ", lng='" + lng + '\'' +
                ", recovered='" + recovered + '\'' +
                ", lat='" + lat + '\'' +
                ", deaths='" + deaths + '\'' +
                '}';
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}