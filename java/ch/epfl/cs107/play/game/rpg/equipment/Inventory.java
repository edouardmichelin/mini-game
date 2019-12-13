package ch.epfl.cs107.play.game.rpg.equipment;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;

import java.util.function.BiConsumer;

public interface Inventory extends InventoryContentAccessor {

    boolean contains(InventoryItem item);

    private boolean addItem(InventoryItem item) {
        return false;
    }

    private boolean removeItem(InventoryItem item) {
        return false;
    }

    interface InventoryItem {

        String getTitle();

        float getWeight();

        int getPrice();

        String getSpriteName();

        BiConsumer<AreaEntity, Area> getConsumeMethod();
    }
}
