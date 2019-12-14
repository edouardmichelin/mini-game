package ch.epfl.cs107.play.game.rpg.equipment;


public interface InventoryContentAccessor {

    Inventory.InventoryItem getItem(int id);

    Inventory.InventoryItem[] getItems();

    int getFortune();

    int getWealth();
}
