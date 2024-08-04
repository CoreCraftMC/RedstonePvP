package com.ryderbelserion.redstonepvp.api.objects.beacons;

import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import java.util.Calendar;

public class Beacon {

    private final String rawLocation;
    private final BeaconDrop drop;
    private final String name;

    private Calendar calendar;
    private String time;

    private boolean isActive = false;
    private boolean isBroken = false;

    public Beacon(final String name, final String rawLocation, final String time) {
        this.name = name;

        this.drop = new BeaconDrop(this.name);

        this.rawLocation = rawLocation;

        this.time = time;

        setCalendar(MiscUtils.getTimeFromString(time));
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

        setCalendar(MiscUtils.getTimeFromString(time));
    }

    public void setCalendar(final Calendar calendar) {
        this.calendar = calendar;
    }

    public final Calendar getCalendar() {
        return this.calendar;
    }

    public void setActive(final boolean active) {
        this.isActive = active;
    }

    public final boolean isActive() {
        return this.isActive;
    }

    public void setBroken(final boolean broken) {
        this.isBroken = broken;
    }

    public final boolean isBroken() {
        return this.isBroken;
    }
}