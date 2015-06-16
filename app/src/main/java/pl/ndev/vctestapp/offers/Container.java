package pl.ndev.vctestapp.offers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Container {

    public static List<Item> ITEMS = new LinkedList<>();

    public static Map<String, Item> ITEM_MAP = new HashMap<>();

    public static Item mapItem(int position) {
        Item item;
        try {
            item = ITEMS.get(position);
            ITEM_MAP.put(item.getId(), item);
        } catch (ArrayIndexOutOfBoundsException exception) {
            item = null;
        }

        return item;
    }

    public static Item getItem(String key) {
        return ITEM_MAP.containsKey(key) ? ITEM_MAP.get(key) : null;
    }
}
