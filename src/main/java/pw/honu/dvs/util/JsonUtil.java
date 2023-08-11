package pw.honu.dvs.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JsonUtil {

    public static ItemStack parseItemStack(JsonElement elem) throws JsonFieldException {
        if (elem.isJsonNull()) {
            return null;
        }

        ItemStack item = new ItemStack(Material.AIR);
        item.setAmount(1);

        if (elem.isJsonPrimitive()) {
            String str = elem.getAsString();

            if (str == null || str.isEmpty()) {
                throw new MissingRequiredJsonFieldException("json string is blank or null, cannot parse type");
            }

            String[] parts = str.split(",");

            if (parts.length >= 1) {
                String type = parts[0].trim();
                Material m = Material.valueOf(type.toUpperCase());
                item.setType(m);
            }

            if (parts.length >= 2) {
                String countStr = parts[1].trim();
                int count = Integer.parseInt(countStr);
                item.setAmount(count);
            }
        } else if (elem.isJsonObject()) {
            JsonObject obj = elem.getAsJsonObject();

            JsonElement type = obj.get("type");
            if (type == null || type.isJsonNull()) {
                throw new MissingRequiredJsonFieldException("missing required field: type");
            }

            item.setType(Material.valueOf(type.getAsString().toUpperCase()));

            JsonElement count = obj.get("count");
            if (count != null && !count.isJsonNull()) {
                item.setAmount(count.getAsInt());
            } else {
                item.setAmount(1);
            }

            ItemMeta meta = item.getItemMeta();

            JsonElement unbreakable = obj.get("unbreakable");
            if (unbreakable != null && !unbreakable.isJsonNull()) {
                meta.setUnbreakable(unbreakable.getAsBoolean());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static class JsonFieldException extends Exception {

        public JsonFieldException(String msg) {
            super(msg);
        }

    }

    public static class MissingRequiredJsonFieldException extends JsonFieldException {

        public MissingRequiredJsonFieldException(String msg) {
            super(msg);
        }
    }

    public static class IncorrectJsonFieldTypeException extends JsonFieldException {

        public IncorrectJsonFieldTypeException(String msg) {
            super(msg);
        }
    }

}
