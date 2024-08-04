package com.ryderbelserion.redstonepvp.api.objects.beacons;

public class Beacon {

    private final String rawLocation;
    private final BeaconDrop drop;
    private final String name;
    private String time;

    public Beacon(final String name, final String rawLocation, final String time) {
        this.name = name;

        this.drop = new BeaconDrop(this.name);

        this.rawLocation = rawLocation;

        this.time = time;
    }

    public final String getRawLocation() {
        return this.rawLocation;
    }

    public final BeaconDrop getDrop() {
        return this.drop;
    }

    public final String getName() {
        return this.name;
    }

    public final String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }
}