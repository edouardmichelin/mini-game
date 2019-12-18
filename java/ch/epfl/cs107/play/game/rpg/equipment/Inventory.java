package ch.epfl.cs107.play.game.rpg.equipment;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory;
import ch.epfl.cs107.play.math.RegionOfInterest;

import java.util.function.BiFunction;

public interface Inventory extends InventoryContentAccessor {

    boolean contains(InventoryItem item);

    boolean addItem(InventoryItem item, int quantity);

    boolean removeItem(InventoryItem item, int quantity);

    interface InventoryItem {

        String getTitle();

        float getWeight();

        int getPrice();

        boolean getRequiresAnimations();

        String getSpriteName();

        BiFunction<AreaEntity, Area, ARPGInventory.ARPGItem> getConsumeMethod();

        ARPGInventory.ARPGItem getItemToConsume();

        boolean getSelfConsumable();

        RegionOfInterest getTextureRoi();
    }
}
