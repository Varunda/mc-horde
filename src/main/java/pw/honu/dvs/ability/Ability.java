package pw.honu.dvs.ability;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface Ability {

    @NotNull String name();

    @NotNull String description();

    /**
     * Execute the ability, returning if the ability met the conditions to cast (such as proximity to a player)
     * @param caster Living entity that is performed the cast
     * @return If the ability was successfully cast or not
     */
    boolean execute(@NotNull LivingEntity caster);

}
