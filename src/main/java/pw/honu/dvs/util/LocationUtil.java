package pw.honu.dvs.util;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationUtil {

    public static Location create(World world, Vector vec) {
        return vec.toLocation(world);
    }

    public static List<Location> create(World world, Collection<Vector> vecs) {
        List<Location> locs = new ArrayList<>(vecs.size());

        for (Vector v : vecs) {
            locs.add(v.toLocation(world));
        }

        return locs;
    }

}
