package pw.honu.dvs.config;

import com.google.gson.Gson;

public class MonsterConfig {

    public static MonsterConfig parse(String input) {
        return (new Gson()).fromJson(input, MonsterConfig.class);
    }

    public static String id;

}
