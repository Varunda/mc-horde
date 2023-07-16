package pw.honu.dvs.util;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class LocationUtil {

    public static Location create(World world, Vector vec) {
        return new Location(world, vec.getX(), vec.getY(), vec.getZ());
    }

}
