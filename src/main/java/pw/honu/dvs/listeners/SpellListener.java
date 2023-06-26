package pw.honu.dvs.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpellCastEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.MatchState;
import pw.honu.dvs.ability.Ability;
import pw.honu.dvs.managers.AbilityManager;
import pw.honu.dvs.managers.MatchManager;

import java.util.List;

public class SpellListener implements Listener {

    @EventHandler
    public void spellCast(EntitySpellCastEvent event) {
        if (MatchManager.instance.getMatchState() != MatchState.RUNNING) {
            return;
        }

        event.setCancelled(true);

        Spellcaster caster = event.getEntity();
        LivingEntity target = caster.getTarget();
        if (target == null) {
            return;
        }

        List<MetadataValue> abilities = caster.getMetadata("dvs-spell-ability");
        for (MetadataValue v : abilities) {
            Ability ability = AbilityManager.instance.get(v.asString());
            if (ability == null) {
                continue;
            }

            ability.execute(caster);
        }
    }

}
