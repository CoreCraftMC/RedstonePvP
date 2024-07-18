package com.ryderbelserion.redstonepvp.api.objects;

import com.ryderbelserion.redstonepvp.RedstonePvP;
import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import org.bukkit.Location;
import java.util.UUID;

public class BeaconDrop {

    private final RedstonePvP plugin = RedstonePvP.getPlugin();

    private final UUID uuid;
    private final String rawLocation;
    //private final Location location;
    private final int time;

    public BeaconDrop(final UUID uuid, final String rawLocation, final int time) {
        this.uuid = uuid;

        this.rawLocation = rawLocation;

        //this.location = MiscUtils.location(rawLocation);

        this.time = time;
    }

    //public final Location getLocation() {
    //    return this.location;
    //}

    public final UUID getUUID() {
        return this.uuid;
    }

    public final String getRawLocation() {
        return this.rawLocation;
    }

    public final int getTime() {
        return this.time;
    }
}