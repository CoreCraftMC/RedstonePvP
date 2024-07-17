package com.ryderbelserion.redstonepvp.api.objects;

import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import org.bukkit.Location;
import java.util.UUID;

public class BeaconDrop {

    private final UUID uuid;
    private final String key;
    private final Location location;
    private final int time;

    public BeaconDrop(final UUID uuid, final String location, final int time) {
        this.uuid = uuid;

        this.key = location;
        this.location = MiscUtils.location(location);
        this.time = time;
    }

    public final Location getLocation() {
        return this.location;
    }

    public final String getKey() {
        return this.key;
    }

    public final UUID getUUID() {
        return this.uuid;
    }

    public final int getTime() {
        return this.time;
    }
}