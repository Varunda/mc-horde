package pw.honu.dvs.managers;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Location;
import pw.honu.dvs.DvS;

import javax.annotation.Nullable;

public class HologramManager {


    public static HologramManager instance = new HologramManager();

    public HologramManager() {
    }

    public @Nullable Hologram create(Location loc) {
        if (DvS.holograms == null) {
            return null;
        }

        return DvS.holograms.createHologram(loc);
    }

    public @Nullable Hologram create(Location loc, String text) {
        @Nullable Hologram h = create(loc);
        if (null == h) {
            return null;
        }

        h.getLines().appendText(text);

        return h;
    }


}
