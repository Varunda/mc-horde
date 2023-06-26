package pw.honu.dvs.ability;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.managers.LocationManager;
import pw.honu.dvs.managers.MonsterManager;
import pw.honu.dvs.monster.MonsterTemplate;

public class SpawnMonsterTemplateAbility implements Ability {

    private String name;
    private String templateName;
    private int count;

    public SpawnMonsterTemplateAbility(String name, String templateName, int count) {
        this.name = name;
        this.templateName = templateName;
        this.count = count;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String description() {
        return "Spawn " + count + " mobs using the template " + templateName;
    }

    @Override
    public boolean execute(@NotNull LivingEntity caster) {
        if (!(caster instanceof Player)) {
            return false;
        }

        Player p = (Player) caster;
        Block b = p.getTargetBlock(100);

        if (b == null || b.getType() == Material.AIR) {
            return false;
        }

        MonsterTemplate template = MonsterManager.instance.get(templateName);
        if (template == null) {
            return false;
        }

        Location loc = b.getLocation();
        loc.setY(loc.getBlockY() + 1);

        for (int i = 0; i < this.count; ++ i) {
            MonsterManager.instance.spawn(template, loc);
        }

        return true;
    }
}
