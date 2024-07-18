package com.ryderbelserion.redstonepvp.api.objects.beacons;

import com.ryderbelserion.redstonepvp.RedstonePvP;

public class Beacon {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private final String rawLocation;
    private final BeaconDrop drop;
    private final String name;
    private final int time;

    public Beacon(final String name, final String rawLocation, final int time) {
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

    public final int getTime() {
        return this.time;
    }
}