package ru.otus.tegneryadnov.web.server.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Storage {
    private static List<Item> items;
    private static final Logger logger = LogManager.getLogger(Storage.class.getName());

    public static void init() {
        logger.info("Хранилище проинициализировано");
        items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int) (Math.random() * 1000)));
        }
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static void save(Item item) {
        item.setId(UUID.randomUUID());
        items.add(item);
    }

    public static void add(Item item) {
        items.add(item);
    }

    public static boolean replaceItem(Item itemForReplace) {
        boolean replaced = false;
        int i = 0;
        Iterator<Item> iterator = items.iterator();
        do {
            Item item = iterator.next();
            if (item.getId().equals(itemForReplace.getId())) {
                items.set(i, itemForReplace);
                replaced = true;
                break;
            }
            i++;
        }
        while (iterator.hasNext());
        return replaced;
    }
}
