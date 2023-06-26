package pw.honu.dvs.managers;

import org.bukkit.block.data.type.Fire;
import org.bukkit.block.data.type.Snow;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import pw.honu.dvs.DvS;
import pw.honu.dvs.DvSLogger;
import pw.honu.dvs.ability.*;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AbilityManager {

    public static AbilityManager instance;
    static {
        instance = new AbilityManager();

        Reflections ref = new Reflections("pw.honu.dvs.ability");
        Set<Class<? extends Ability>> abilities = ref.getSubTypesOf(Ability.class);

        for (Class<? extends Ability> ability : abilities) {
            try {
                Constructor<?> ctor = ability.getConstructor();
                Ability a = (Ability) ctor.newInstance();

                instance.add(a);
                DvSLogger.info("Adding ability " + a.name() + ": " + a.description());
            } catch (NoSuchMethodException e) {
                DvSLogger.warn("missing ctor for " + ability.getCanonicalName());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                DvSLogger.error("failed ctor call for " + ability.getCanonicalName() + ": " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    private Map<String, Ability> abilities = new HashMap<>();

    public @Nullable Ability get(@NotNull String name) {
        return abilities.getOrDefault(name.toLowerCase(), null);
    }

    /**
     * Add a new ability
     * @param ability Ability to add
     * @return True if the ability was added, false if that ability already exists with the same name
     */
    public boolean add(@NotNull Ability ability) {
        if (abilities.containsKey(ability.name().toLowerCase())) {
            DvS.instance.getLogger().warning("not adding duplicate ability " + ability.name().toLowerCase() + ". which already exists");
            return false;
        }

        abilities.put(ability.name().toLowerCase(), ability);
        return true;
    }

    /**
     * Get the names of the abilities
     */
    public List<String> getNames() {
        return abilities.keySet().stream().toList();
    }

}
