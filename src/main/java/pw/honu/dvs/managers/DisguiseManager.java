package pw.honu.dvs.managers;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisguiseManager {

    static {
        instance = new DisguiseManager();
    }

    private Map<UUID, MobDisguise> disguises = new HashMap<>();

    public static DisguiseManager instance;

    public void setDisguise(Player p, DisguiseType type) {
        MobDisguise previousDisguise = getDisguise(p);
        if (null != previousDisguise) {
            previousDisguise.stopDisguise();
            disguises.remove(p.getUniqueId());
        }

        MobDisguise disguise = new MobDisguise(type);
        disguise.setEntity(p);
        disguise.startDisguise();

        disguises.put(p.getUniqueId(), disguise);
    }

    public MobDisguise getDisguise(Player p) {
        return disguises.get(p.getUniqueId());
    }

    public void removeDisguise(Player p) {
        MobDisguise disguise = getDisguise(p);
        if (disguise == null) {
            return;
        }

        disguise.stopDisguise();
        disguises.remove(p.getUniqueId());
    }

}
