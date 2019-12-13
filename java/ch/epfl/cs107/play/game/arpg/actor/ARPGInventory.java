package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.rpg.equipment.Inventory;
import com.sun.jdi.VoidType;
import com.sun.jdi.VoidValue;

import java.lang.invoke.LambdaMetafactory;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        if (id >= this.content.size()) return null;

        return this.getItems()[id];
    }

    public ARPGItem[] getItems() {
        return this.content.keySet().toArray(ARPGItem[]::new);
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

    public boolean addItem(InventoryItem item, int quantity) {
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

    public boolean removeItem(InventoryItem item, int quantity) {
        int qt = this.getItemQuantity(item) - quantity;

        if (qt < 0) return false;
        else if (qt == 0) this.content.remove(item);
        else this.content.put(item, qt);

        this.remainingCapacity += item.getWeight() * quantity;
        this.inventoryValue -= item.getPrice() * quantity;

        return true;
    }

    boolean removeSingleItem(InventoryItem item) {
        return this.removeItem(item, 1);
    }

    public enum ARPGItem implements Inventory.InventoryItem {
        ARROW("Arrow", 0, 5, "zelda/arrow.icon"),
        BOW("Bow", 0, 15, "zelda/bow.icon"),
        SWORD("Sword", 0, 20, "zelda/sword.icon"),
        STAFF("Staff", 0, 200, "zelda/staff_water.icon"),
        BOMB("Bomb", 0, 50, "zelda/bomb", Bomb::consume),
        CASTLE_KEY("CastleKey", 0, 100, "zelda/key")
        ;

        private String title;
        private float weight;
        private int price;
        private String spriteName;
        private BiConsumer<AreaEntity, Area> consumeMethod;

        ARPGItem(String title, float weight, int price, String spriteName) {
            this.title = title;
            this.weight = weight;
            this.price = price;
            this.spriteName = spriteName;
            this.consumeMethod = null;
        }

        ARPGItem(String title, float weight, int price, String spriteName, BiConsumer<AreaEntity, Area> consumeMethod) {
            this(title, weight, price, spriteName);
            this.consumeMethod = consumeMethod;
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

        public BiConsumer<AreaEntity, Area> getConsumeMethod() {
            return this.consumeMethod;
        }
    }
}
