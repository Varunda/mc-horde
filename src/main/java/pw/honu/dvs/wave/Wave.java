package pw.honu.dvs.wave;

import java.util.ArrayList;
import java.util.List;

public class Wave {

    private List<WaveEntry> entries = new ArrayList<>();

    public void addEntry(WaveEntry entry) {
        entries.add(entry);
    }

    public void clear() {
        entries.clear();
    }

    public List<WaveEntry> getEntries() {
        return entries;
    }

}
