package ch.epfl.cs107.play.game.rpg.equipment;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;

import java.util.function.BiConsumer;

public interface Inventory extends InventoryContentAccessor {

    boolean contains(InventoryItem item);

    boolean addItem(InventoryItem item, int quantity);

    boolean removeItem(InventoryItem item, int quantity);

    interface InventoryItem {

        String getTitle();

        float getWeight();

        int getPrice();

        String getSpriteName();

        BiConsumer<AreaEntity, Area> getConsumeMethod();
    }
}
