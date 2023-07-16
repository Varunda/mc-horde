package pw.honu.dvs;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HordeMap {

    private File folder;

    private String name;
    private String author;

    private int setupTicks;

    private Vector playerStart;
    private Vector monsterSpawn;
    private Vector monsterLobby;
    private Vector monsterTarget;

    private Vector chest;

    public static HordeMap loadFromFolder(File folder) throws IOException {
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

    private static void parseMapJson(HordeMap map, File parent) throws IOException {
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
    }

    private static void parseMapJsonVersion1(HordeMap map, JsonObject json) throws IOException {
        map.name = json.get("name").getAsString();
        map.author = json.get("author").getAsString();
        map.setupTicks = json.get("setup_ticks").getAsInt();

        map.chest = parseVector(json, "chest");

        JsonObject locations = json.get("locations").getAsJsonObject();
        map.monsterLobby = parseVector(locations, "monster_lobby");
        map.playerStart = parseVector(locations, "player_spawn");
        map.monsterSpawn = parseVector(locations, "monster_spawn");
        map.monsterTarget = parseVector(locations, "monster_target");
    }

    /**
     * Get a vector named from an object
     * @param parent Parent object that contains the field that contains the information about the vector
     * @param name Name of the field within the parent
     * @return A new Vector
     * @throws IOException If the elem is missing or not an object
     */
    private static Vector parseVector(JsonObject parent, String name) throws IOException {
        JsonElement elem = parent.get(name);

        if (elem.isJsonNull()) {
            throw new IOException("Missing element '" + name + "' from parent object");
        }

        if (!elem.isJsonObject()) {
            throw new IOException("This element is not an object!");
        }

        JsonObject obj = elem.getAsJsonObject();

        double x = obj.get("x").getAsDouble();
        double y = obj.get("y").getAsDouble();
        double z = obj.get("z").getAsDouble();

        return new Vector(x, y, z);
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

    public Vector getPlayerStart() {
        return playerStart;
    }

    public Vector getMonsterSpawn() {
        return monsterSpawn;
    }

    public Vector getMonsterLobby() {
        return monsterLobby;
    }

    public Vector getMonsterTarget() {
        return monsterTarget;
    }

    public Vector getChest() {
        return chest;
    }

    public File getFolder() {
        return folder;
    }

    public int getSetupTicks() {
        return setupTicks;
    }

    public String getAuthor() {
        return author;
    }
}
