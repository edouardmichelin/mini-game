package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.rpg.equipment.Inventory;

import java.util.HashMap;

public class ARPGInventory implements Inventory {
    private Entity holder;
    private int wealth;
    private int inventoryValue;
    private int remainingCapacity;
    private HashMap<InventoryItem, Integer> content;

    ARPGInventory(int capacity, Entity holder) {
        this.remainingCapacity = capacity;
        this.holder = holder;
        this.content = new HashMap<>();
    }

    public int size() {
        return this.content.size();
    }

    public ARPGItem getItem(int id) {
        return this.getItems()[id];
    }

    public ARPGItem[] getItems() {
        return this.content.keySet().toArray(ARPGItem[]::new).clone();
    }

    private int getItemQuantity(InventoryItem item) {
        return this.content.getOrDefault(item, 0);
    }

    private int alterMoney(int amount) {
        this.wealth = this.wealth + amount;
        return this.wealth;
    }

    public int addMoney(int amount) {
        if (amount < 0) return this.wealth;

        return this.alterMoney(amount);
    }

    public int withdrawMoney(int amount) {
        if (amount < 0) return this.wealth;

        return this.alterMoney(amount * -1);
    }

    public int getFortune() {
        return this.wealth + this.inventoryValue;
    }

    public int getWealth() {
        return this.wealth;
    }

    @Override
    public boolean contains(InventoryItem item) {
        return this.getItemQuantity(item) > 0;
    }

    boolean addItem(InventoryItem item, int quantity) {
        if (this.remainingCapacity < quantity) return false;

        int qt = this.getItemQuantity(item);
        this.content.put(item, qt + quantity);
        this.remainingCapacity -= item.getWeight() * quantity;
        this.inventoryValue += item.getPrice() * quantity;

        return true;
    }

    boolean addSingleItem(InventoryItem item) {
        return this.addItem(item, 1);
    }

    boolean removeItem(InventoryItem item, int quantity) {
        int qt = this.getItemQuantity(item) - quantity;

        if (qt < 0) return false;
        else if (qt == 0) this.content.remove(item);
        else this.content.put(item, qt);

        this.remainingCapacity += item.getWeight() * quantity;
        this.inventoryValue -= item.getPrice() * quantity;

        return true;
    }

    protected boolean removeSingleItem(InventoryItem item) {
        return this.removeItem(item, 1);
    }

    public enum ARPGItem implements Inventory.InventoryItem {
        ARROW(0, 5, "zelda/arrow.icon"),
        BOW(0, 15, "zelda/bow.icon"),
        SWORD(0, 20, "zelda/sword.icon"),
        STAFF(0, 200, "zelda/staff_water.icon"),
        BOMB(0, 50, "zelda/bomb"),
        CASTLE_KEY(0, 100, "zelda/key")
        ;

        private String title;
        private float weight;
        private int price;
        private String spriteName;

        ARPGItem(float weight, int price, String spriteName) {
            this.title = this.name();
            this.weight = weight;
            this.price = price;
            this.spriteName = spriteName;
        }

        public static ARPGItem getFromTitle(String title) {
            for (ARPGItem item : ARPGItem.values()) {
                if (item.title == title) return item;
            }

            return null;
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public float getWeight() {
            return this.weight;
        }

        @Override
        public int getPrice() {
            return this.price;
        }

        public String getSpriteName() {
            return this.spriteName;
        }
    }
}
