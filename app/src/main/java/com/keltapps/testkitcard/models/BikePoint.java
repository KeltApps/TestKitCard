package com.keltapps.testkitcard.models;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by sergio on 6/04/16 for KelpApps.
 */
public class BikePoint {
    @SerializedName("id")
    private String idBikePoint;
    @SerializedName("commonName")
    private String commonName;
    @SerializedName("content")
    private LinkedList<AdditionalProperty> additionalPropertyLinkedList;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    private transient int availableBikes;
    private transient int totalDocks;

    public class AdditionalProperty {
        @SerializedName("key")
        private String key;
        @SerializedName("value")
        private int value;//PROBABLY CRASH CHANGE TO OBJECT

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }


    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getIdBikePoint() {
        return idBikePoint;
    }

    public void setIdBikePoint(String idBikePoint) {
        this.idBikePoint = idBikePoint;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public LinkedList<AdditionalProperty> getAdditionalPropertyLinkedList() {
        return additionalPropertyLinkedList;
    }

    public void setAdditionalPropertyLinkedList(LinkedList<AdditionalProperty> additionalPropertyLinkedList) {
        this.additionalPropertyLinkedList = additionalPropertyLinkedList;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(int availableBikes) {
        this.availableBikes = availableBikes;
    }

    public int getTotalDocks() {
        return totalDocks;
    }

    public void setTotalDocks(int totalDocks) {
        this.totalDocks = totalDocks;
    }
}
