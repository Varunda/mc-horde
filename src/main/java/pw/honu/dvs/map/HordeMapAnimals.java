package pw.honu.dvs.map;

import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HordeMapAnimals {

    public static class HordeMapAnimalHerd {

        public EntityType type;

        public int amount;

    }

    public List<Vector> locations = new ArrayList<>();

    public List<HordeMapAnimalHerd> herds = new ArrayList<>();

}
