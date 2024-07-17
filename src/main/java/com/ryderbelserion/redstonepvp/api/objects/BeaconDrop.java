package com.ryderbelserion.redstonepvp.api.objects;

import com.ryderbelserion.redstonepvp.utils.MiscUtils;
import org.bukkit.Location;
import java.util.UUID;

public class BeaconDrop {

    private final UUID uuid;
    private final String key;
    private final Location location;

    public BeaconDrop(final UUID uuid, final String location) {
        this.uuid = uuid;

        this.key = location;
        this.location = MiscUtils.location(location);
    }

    public final Location getLocation() {
        return this.location;
    }

    public final UUID getUUID() {
        return this.uuid;
    }

    public final String getKey() {
        return this.key;
    }
}