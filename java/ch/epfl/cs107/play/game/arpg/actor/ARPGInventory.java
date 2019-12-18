package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.ARPG;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.items.*;
import ch.epfl.cs107.play.game.rpg.equipment.Inventory;
import ch.epfl.cs107.play.math.RegionOfInterest;
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
        if (item == null) return false;
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
        if (item == null) return false;
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
        ARROW(ArrowItem.TITLE, ArrowItem.WEIGHT, ArrowItem.PRICE, false, SpriteNames.ARROW_ITEM, null, ArrowItem.ITEM_TO_CONSUME, false, null),
        BOW(BowItem.TITLE, BowItem.WEIGHT, BowItem.PRICE, true, SpriteNames.BOW_ITEM, ArrowItem::consume, BowItem.ITEM_TO_CONSUME, false,null),
        SWORD(SwordItem.TITLE, SwordItem.WEIGHT, SwordItem.PRICE, true, SpriteNames.SWORD_ITEM, SwordSlashItem::consume, SwordItem.ITEM_TO_CONSUME, false,null),
        STAFF(StaffItem.TITLE, StaffItem.WEIGHT, StaffItem.PRICE, true, SpriteNames.STAFF_ITEM, MagicWaterProjectileItem::consume, StaffItem.ITEM_TO_CONSUME, false,null),
        BOMB(BombItem.TITLE, BombItem.WEIGHT, BombItem.PRICE, false, SpriteNames.BOMB_ITEM, BombItem::consume, BombItem.ITEM_TO_CONSUME, true, new RegionOfInterest(0,0,16,16)),
        CASTLE_KEY(CastleKeyItem.TITLE, CastleKeyItem.WEIGHT, CastleKeyItem.PRICE, false, SpriteNames.CASTLE_KEY_ITEM, null, CastleKeyItem.ITEM_TO_CONSUME, false,null)
        ;

        final String title;
        final float weight;
        final int price;
        final boolean requiresAnimations;
        final String spriteName;
        final BiFunction<AreaEntity, Area, ARPGItem> consumeMethod;
        final ARPGItem itemToConsume;
        final boolean selfConsumable;
        final RegionOfInterest roi;

        ARPGItem(String title, float weight, int price, boolean requiresAnimations, String spriteName, BiFunction<AreaEntity, Area, ARPGItem> consumeMethod, ARPGItem itemToConsume, boolean selfConsumable, RegionOfInterest roi) {
            this.title = title;
            this.weight = weight;
            this.price = price;
            this.requiresAnimations = requiresAnimations;
            this.spriteName = spriteName;
            this.consumeMethod = consumeMethod;
            this.itemToConsume = itemToConsume;
            this.selfConsumable = selfConsumable;
            this.roi = roi == null ? new RegionOfInterest(0,0,32,32) : roi;
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

        @Override
        public boolean getRequiresAnimations() {
            return this.requiresAnimations;
        }

        @Override
        public String getSpriteName() {
            return this.spriteName;
        }

        @Override
        public BiFunction<AreaEntity, Area, ARPGItem> getConsumeMethod() {
            return this.consumeMethod;
        }

        @Override
        public ARPGItem getItemToConsume() {
            return this.itemToConsume;
        }

        @Override
        public boolean getSelfConsumable() {
            return this.selfConsumable;
        }

        @Override
        public RegionOfInterest getTextureRoi() {
            return this.roi;
        }
    }
}
