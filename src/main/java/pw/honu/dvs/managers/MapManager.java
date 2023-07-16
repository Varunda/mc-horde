package pw.honu.dvs.managers;

import org.jetbrains.annotations.Nullable;
import pw.honu.dvs.DvS;
import pw.honu.dvs.HordeMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

    public static MapManager instance;

    static {
        instance = new MapManager();
    }

    private List<HordeMap> maps = new ArrayList<>();

    private @Nullable HordeMap nextMap = null;

    public void setMaps(List<HordeMap> maps) {
        this.maps = maps;
    }

    /**
     * Get an array of all maps that can be picked
     */
    public List<HordeMap> getMaps() {
        return maps;
    }

    public @Nullable HordeMap getMap(String mapName) {
        for (HordeMap map : this.getMaps()) {
            if (mapName.equalsIgnoreCase(map.getName())) {
                return map;
            }
        }

        return null;
    }

    public boolean isValidMap(String mapName) {
        return getMap(mapName) != null;
    }

    public @Nullable HordeMap getNextMap() {
        return this.nextMap;
    }

    public void setNextMap(HordeMap map) {
        this.nextMap = map;
    }

}
