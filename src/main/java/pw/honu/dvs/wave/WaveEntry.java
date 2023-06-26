package pw.honu.dvs.wave;

import org.jetbrains.annotations.NotNull;
import pw.honu.dvs.monster.MonsterTemplate;

public class WaveEntry {

    private int count;

    private @NotNull MonsterTemplate template;

    public WaveEntry(int count, @NotNull MonsterTemplate template) {
        this.count = count;
        this.template = template;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count)  {
        this.count = count;
    }

    public @NotNull MonsterTemplate getTemplate() {
        return template;
    }
}
