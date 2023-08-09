package pw.honu.dvs.map;

import com.google.gson.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.honu.dvs.DvS;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class HordeMap {

    private File folder;

    private String name;
    private String author;

    private int setupTicks;

    private Vector playerStart;
    private Vector monsterLobby;

    private List<HordeMapPhase> phases = new ArrayList<>();

    private Vector chest;

    public static HordeMap loadFromFolder(File folder) throws IOException, InvalidMapJsonException {
        if (!folder.isDirectory()) {
            throw new IOException("Folder " + folder.getAbsolutePath() + " is not a directory!");
        }

        HordeMap map = new HordeMap();
        map.folder = folder;

        parseMapJson(map, folder);

        return map;
    }

    private static File loadFile(File parent, String relative) {
        DvS.instance.getLogger().info("Loading file " + parent.getAbsolutePath() + "/" + relative);
        return new File(parent.getAbsoluteFile() + "/" + relative);
    }

    private static String readFile(File file) {
        try {
            Path path = Paths.get(file.getAbsolutePath());
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            DvS.instance.getLogger().severe("Failed to read " + file.getAbsolutePath() + ":\n" + e.getLocalizedMessage());
        }

        return null;
    }

    private static void parseMapJson(HordeMap map, File parent) throws IOException, InvalidMapJsonException {
        File jsonFile = loadFile(parent, "map.json");
        if (!jsonFile.exists()) {
            throw new IOException("Missing map.json in " + parent.getAbsolutePath());
        }

        if (!jsonFile.isFile()) {
            throw new IOException("map.json is not a file! Located in " + parent.getAbsolutePath());
        }

        String mapJsonContents = readFile(jsonFile);
        if (null == mapJsonContents) {
            throw new IOException("map.json has no contents! Located in " + parent.getAbsolutePath());
        }

        JsonObject mapJson = null;
        try {
            mapJson = JsonParser.parseString(mapJsonContents).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new IOException("Invalid json encountered in " + jsonFile.getAbsolutePath() + ": " + e.getLocalizedMessage(), e);
        }

        assert mapJson != null;

        JsonElement versionElem = mapJson.get("version");
        if (!versionElem.isJsonPrimitive()) {
            throw new IOException("Unexpected contents of field 'version': expected a number, had a " + getJsonElementType(versionElem));
        }

        int version = versionElem.getAsInt();
        if (version == 1) {
            DvS.instance.getLogger().info("Parsing map as version " + version);
            parseMapJsonVersion1(map, mapJson);
        } else {
            throw new IOException("Unchecked version number: " + version);
        }

        if (map.playerStart == null) {
            throw new InvalidMapJsonException("missing playerStart");
        }

        if (map.monsterLobby == null) {
            throw new InvalidMapJsonException("missing monsterLobby");
        }

        if (map.phases == null || map.phases.size() == 0) {
            throw new InvalidMapJsonException("missing phases, or no phases provided");
        }
    }

    private static void parseMapJsonVersion1(HordeMap map, JsonObject json) throws IOException {
        map.name = json.get("name").getAsString();
        map.author = json.get("author").getAsString();
        map.setupTicks = json.get("setup_ticks").getAsInt();

        map.chest = parseVector(json.get("chest"));

        JsonObject locations = json.get("locations").getAsJsonObject();
        map.monsterLobby = parseVector(locations.get("monster_lobby"));
        map.playerStart = parseVector(locations.get("player_spawn"));
        map.phases = parsePhases(json.get("phases"));
    }

    /**
     * Turn a JSON object into a vector
     * @param elem object to be turned into a vector
     * @return a new Vector that uses the fields x, y and z from the passed element
     * @throws IOException if the passed JsonElement is not a JsonObject
     */
    private static Vector parseVector(JsonElement elem) throws IOException {
        if (!elem.isJsonObject()) {
            throw new IOException("failed to parse vector: expected element to be an object, was a " + getJsonElementType(elem) + " instead");
        }

        JsonObject obj = elem.getAsJsonObject();

        double x = obj.get("x").getAsDouble();
        double y = obj.get("y").getAsDouble();
        double z = obj.get("z").getAsDouble();

        return new Vector(x, y, z);
    }

    /**
     * Parse a JSON array into a list of vectors
     * @param elem element that is an array to turn into a list of vectors
     * @return a new list of vectors
     * @throws IOException if the element was not an array
     */
    private static List<Vector> parseVectors(JsonElement elem) throws IOException {
        if (!elem.isJsonArray()) {
            throw new IOException("failed to parse vectors: expected element to be an array, was a " + getJsonElementType(elem) + " instead");
        }

        JsonArray arr = elem.getAsJsonArray();

        ArrayList<Vector> vecs = new ArrayList<>();

        for (JsonElement i : arr) {
            vecs.add(parseVector(i));
        }

        return vecs;
    }

    private static List<HordeMapPhase> parsePhases(JsonElement elem) throws IOException {
        if (!elem.isJsonArray()) {
            throw new IOException("failed to parse phases: expected element to be an array, was a " + getJsonElementType(elem) + " instead");
        }

        JsonArray arr = elem.getAsJsonArray();
        List<HordeMapPhase> phases = new ArrayList<>();

        for (JsonElement iter : arr) {
            phases.add(parsePhase(iter));
        }

        return phases;
    }

    private static HordeMapPhase parsePhase(JsonElement elem) throws IOException {
        if (!elem.isJsonObject()) {
            throw new IOException("failed to parse phase: expected element to be an object, was a " + getJsonElementType(elem) + " instead");
        }

        JsonObject obj = elem.getAsJsonObject();

        HordeMapPhase phase = new HordeMapPhase();

        phase.setPhase(obj.get("phase").getAsInt());
        phase.setMonsterTarget(parseVector(obj.get("monster_target")));
        phase.setMonsterSpawns(parseVectors(obj.get("monster_spawns")));

        return phase;
    }

    private static String getJsonElementType(JsonElement elem) {
        if (elem.isJsonPrimitive()) {
            return "primitive";
        }
        if (elem.isJsonArray()) {
            return "array";
        }
        if (elem.isJsonNull()) {
            return "null";
        }
        if (elem.isJsonObject()) {
            return "object";
        }

        return "unknown";
    }

    public String getName() {
        return name;
    }

    public @NotNull Vector getPlayerStart() {
        return playerStart;
    }

    public @NotNull Vector getMonsterLobby() {
        return monsterLobby;
    }

    public @NotNull Vector getChest() {
        return chest;
    }

    public @NotNull File getFolder() {
        return folder;
    }

    public int getSetupTicks() {
        return setupTicks;
    }

    public String getAuthor() {
        return author;
    }

    public List<HordeMapPhase> getPhases() {
        return phases;
    }

    public @Nullable HordeMapPhase getPhase(int phaseNumber) {
        for (HordeMapPhase phase : getPhases()) {
            if (phase.getPhase() == phaseNumber) {
                return phase;
            }
        }

        return null;
    }

}
