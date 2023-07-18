package pw.honu.dvs.managers;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.MobGoals;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import pw.honu.dvs.AbilityCaster;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.ability.Ability;
import pw.honu.dvs.item.AbilityItem;
import pw.honu.dvs.mob_goals.MonsterTargetMobGoal;
import pw.honu.dvs.monster.*;
import ru.xezard.glow.data.glow.Glow;

import javax.crypto.spec.DESedeKeySpec;
import java.util.*;

public class MonsterManager {

    public static MonsterManager instance;

    private AbilityCaster caster = new AbilityCaster();

    /**
     * List of mob goals to keep when spawning a monster
     */
    private static final Collection<String> KEPT_KEYS = List.of(
            "nearest_attackable", // let them attack
            "hurt_by" // let them retaliate
    );

    static {
        instance = new MonsterManager();

        ArmoredZombies.init();
        BuffMonsters.init();
        DefaultMonsters.init();
        PlayerMonsters.init();
    }

    private Map<String, MonsterTemplate> monsters = new HashMap<>();
    private Set<LivingEntity> aliveMonsters = new HashSet<>();

    /**
     * Add a new monster template, returning false if the template with that name already exists
     * @param template Template to add
     * @return False if a template with that name already exists
     */
    public boolean add(MonsterTemplate template) {
        if (monsters.containsKey(template.getName().toLowerCase())) {
            DvS.instance.getLogger().warning("not adding duplicate monster template" + template.getName().toLowerCase());
            return false;
        }

        monsters.put(template.getName().toLowerCase(), template);
        DvS.instance.getLogger().info("added monster template " + template.getName().toLowerCase());

        return true;
    }

    public @Nullable MonsterTemplate get(String name) {
        return monsters.getOrDefault(name.toLowerCase(), null);
    }

    /**
     * Get the templates that start with the name
     * @param name Name to find the matching templates for
     * @return A list of all templates that match
     */
    public @NotNull List<String> getMatchingTemplateNames(String name) {
        return monsters.keySet().stream()
                .filter(monsterTemplate -> monsterTemplate.toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public @NotNull List<String> getNames() {
        return monsters.keySet().stream().toList();
    }

    /**
     * Spawn a monster template at a specific location
     *
     * @param template  Template used to spawn the monster
     * @param loc       Where the monster will be spawned
     *
     * @return If the spawn was successful or not. On false, a warning is logged
     */
    public boolean spawn(MonsterTemplate template, Location loc) {
        Entity entity = loc.getWorld().spawnEntity(loc, template.getEntityType(), false);
        if (!(entity instanceof LivingEntity)) {
            DvS.instance.getLogger().warning(template.getName() + " is not for a living entity: " + template.getEntityType().name());
            return false;
        }

        if (entity instanceof PiglinAbstract) {
            ((PiglinAbstract) entity).setImmuneToZombification(true);
        }
        if (entity instanceof Hoglin) {
            ((Hoglin)entity).setImmuneToZombification(true);
        }

        LivingEntity le = (LivingEntity) entity;
        le.setMetadata("dvs-spawned", new FixedMetadataValue(DvS.instance, true));
        setInventory(le, template);

        if (template.getAbilityName() != null && template.getAbilityCooldown() > 0 && template.getAbilityCount() > 0) {
            Ability ab = AbilityManager.instance.get(template.getAbilityName());
            if (ab == null) {
                DvSLogger.warn("Missing ability '" + template.getAbilityName() + "' when spawning template " + template.getName());
            } else {
                caster.addCaster(le, ab, template.getAbilityCount(), template.getAbilityCooldown());
            }
        }

        if (template.getGlowColor() != null) {
            //Glow glow = Glow.builder().color(template.getGlowColor()).name("Boss." + template.getName()).build();
            //glow.addHolders(le);
            for (Player p : Bukkit.getOnlinePlayers()) {
                //glow.display(p);
            }
        }

        if (le instanceof Mob) {
            Mob m = (Mob) le;

            MobGoals mg = Bukkit.getMobGoals();
            mg.addGoal(m, 1, new MonsterTargetMobGoal(DvS.instance, m));
        }

        if (le instanceof Slime) {
            Slime s = (Slime) le;
            s.setWander(false);
        }

        if (le instanceof Spellcaster && template.getAbilityName() != null) {
            le.setMetadata("dvs-spell-ability", new FixedMetadataValue(DvS.instance, template.getAbilityName()));
        }

        aliveMonsters.add(le);

        return true;
    }

    /**
     * Set the inventory of a living entity to match that of a template. This will also clear the inventory
     *
     * @param entity Entity to set the inventory of
     * @param template Template the items will be pulled from
     * @return If the operation was successful or not
     */
    public boolean setInventory(LivingEntity entity, MonsterTemplate template) {
        EntityEquipment eq = entity.getEquipment();

        if (eq == null) {
            DvSLogger.warn("cannot set inventory, equipment is null");
            return false;
        }

        eq.clear();

        if (template.getMainHand() != null) { eq.setItemInMainHand(template.getMainHand()); }
        if (template.getOffHand() != null) { eq.setItemInOffHand(template.getOffHand()); }
        if (template.getHelmet() != null) { eq.setHelmet(template.getHelmet()); }
        if (template.getChest() != null) { eq.setChestplate(template.getChest()); }
        if (template.getLegs() != null) { eq.setLeggings(template.getLegs()); }
        if (template.getBoots() != null) { eq.setBoots(template.getBoots()); }

        if (entity instanceof  Player) {
            Player p = (Player) entity;

            for (ItemStack i : template.getInventory()) {
                p.getInventory().addItem(i);
            }
        }

        return true;
    }

    /**
     * Remove a monster from the tracked set
     * @param le Living entity that will be removed
     */
    public boolean removeAliveMonster(LivingEntity le) {
        caster.removeCaster(le);
        return aliveMonsters.remove(le);
    }

    /**
     * Get all monsters that are currently alive
     */
    public Set<LivingEntity> getAliveMonsters() {
        return aliveMonsters;
    }

    /**
     * Check if the UUID of a living entity is a tracked monster or not
     * @param id UUID of the LivingEntity to check
     * @return A boolean value
     */
    public boolean isTrackedMonster(UUID id) {
        for (LivingEntity le : aliveMonsters) {
            if (le.getUniqueId() == id) {
                return true;
            }
        }

        if (PlayerManager.instance.getMonsters().contains(id)) {
            return true;
        }

        return false;
    }

    /**
     * Check if an entity is a tracked monster
     * @param e Entity to perform the check on
     */
    public boolean isTrackedMonster(Entity e) {
        if (!(e instanceof LivingEntity)) {
            return false;
        }

        LivingEntity le = (LivingEntity) e;
        return isTrackedMonster(le.getUniqueId());
    }

    /**
     * Get how many monster are still alive
     */
    public int getMonstersAlive() {
        return aliveMonsters.size();
    }

    public void killAllMonsters() {
        Iterator<LivingEntity> monsters = getAliveMonsters().iterator();
        while (monsters.hasNext()) {
            LivingEntity le = monsters.next();
            monsters.remove();
            le.setHealth(0);
        }
    }

}
