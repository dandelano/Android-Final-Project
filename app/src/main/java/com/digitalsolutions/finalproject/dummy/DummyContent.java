package com.digitalsolutions.finalproject.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        //TODO: Get this list from persistance
        addItem(new DummyItem("1", "Mr. Peabody and Sherman"));
        addItem(new DummyItem("2", "Big Hero 6"));
        addItem(new DummyItem("3", "Frozen"));
        addItem(new DummyItem("4", "Toy Story"));
        addItem(new DummyItem("5", "Toy Story 2"));
        addItem(new DummyItem("6", "Ice Age"));
        addItem(new DummyItem("7", "Megamind"));
        addItem(new DummyItem("8", "Cars"));
        addItem(new DummyItem("9", "Hotel Transylvania"));
        addItem(new DummyItem("10", "Hop"));
        addItem(new DummyItem("11", "Shrek"));
        addItem(new DummyItem("12", "Cars 2"));
        addItem(new DummyItem("13", "Finding Nemo"));
        addItem(new DummyItem("14", "Sharks Tale"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
