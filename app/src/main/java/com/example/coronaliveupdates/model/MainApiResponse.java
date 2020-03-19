package com.example.coronaliveupdates.model;

import com.google.gson.Gson;

public class MainApiResponse {
    int sortedPos;

    private DailyTimeSeries dailyTimeSeries;

    private String image;

    private Recovered recovered;

    private String dailySummary;

    private String lastUpdate;

    private CountryDetail countryDetail;

    private String source;

    private String countries;

    private Confirmed confirmed;

    private Deaths deaths;

    public DailyTimeSeries getDailyTimeSeries() {
        return dailyTimeSeries;
    }

    public void setDailyTimeSeries(DailyTimeSeries dailyTimeSeries) {
        this.dailyTimeSeries = dailyTimeSeries;
    }

    public int getSortedPos() {
        return sortedPos;
    }

    public void setSortedPos(int sortedPos) {
        this.sortedPos = sortedPos;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recovered getRecovered() {
        return recovered;
    }

    public void setRecovered(Recovered recovered) {
        this.recovered = recovered;
    }

    public String getDailySummary() {
        return dailySummary;
    }

    public void setDailySummary(String dailySummary) {
        this.dailySummary = dailySummary;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public CountryDetail getCountryDetail() {
        return countryDetail;
    }

    public void setCountryDetail(CountryDetail countryDetail) {
        this.countryDetail = countryDetail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public Confirmed getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Confirmed confirmed) {
        this.confirmed = confirmed;
    }

    public Deaths getDeaths() {
        return deaths;
    }

    public void setDeaths(Deaths deaths) {
        this.deaths = deaths;
    }

    @Override
    public String toString() {
        return "ClassPojo [dailyTimeSeries = " + dailyTimeSeries + ", image = " + image + ", recovered = " + recovered + ", dailySummary = " + dailySummary + ", lastUpdate = " + lastUpdate + ", countryDetail = " + countryDetail + ", source = " + source + ", countries = " + countries + ", confirmed = " + confirmed + ", deaths = " + deaths + "]";
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public class CountryDetail {
        private String pattern;

        private String example;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        @Override
        public String toString() {
            return "ClassPojo [pattern = " + pattern + ", example = " + example + "]";
        }
    }

    public class DailyTimeSeries {
        private String pattern;

        private String example;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        @Override
        public String toString() {
            return "ClassPojo [pattern = " + pattern + ", example = " + example + "]";
        }
    }

    public class Deaths {
        private String detail;

        private String value;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [detail = " + detail + ", value = " + value + "]";
        }
    }

    public class Recovered {
        private String detail;

        private String value;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [detail = " + detail + ", value = " + value + "]";
        }
    }

    public class Confirmed {
        private String detail;

        private String value;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ClassPojo [detail = " + detail + ", value = " + value + "]";
        }
    }
}
