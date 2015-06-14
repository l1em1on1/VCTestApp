package pl.ndev.vctestapp.offers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Container {

    public static List<Item> ITEMS = new LinkedList<Item>();

    public static Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    private static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static Item getItem(String key) {
        return ITEM_MAP.containsKey(key) ? ITEM_MAP.get(key) : null;
    }
}
