package com.ryderbelserion.redstonepvp.api.objects;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import org.bukkit.Location;
import java.util.UUID;

public class BeaconDrop {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private final String name;
    private final String rawLocation;
    private final int time;

    public BeaconDrop(final String name, final String rawLocation, final int time) {
        this.name = name;

        this.rawLocation = rawLocation;

        this.time = time;
    }

    public final String getName() {
        return this.name;
    }

    public final String getRawLocation() {
        return this.rawLocation;
    }

    public final int getTime() {
        return this.time;
    }
}