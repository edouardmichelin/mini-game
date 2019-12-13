package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.config.Keys;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ARPGPlayer extends Player {
    private final static int ANIMATION_DURATION = 8;
    private final static float DEFAULT_HEALTH_POINTS = 5f;

    private float maxHp;
    private float hp;
    private int currentItemId = 0;

    private Keyboard keyboard;
    private Animation[] animations;
    private ARPGInventory inventory;
    private ARPGPlayerHandler interactionHandler;
    private ARPGPlayerStatusGUI GUI;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);

        this.keyboard = area.getKeyboard();
        this.maxHp = DEFAULT_HEALTH_POINTS;
        this.hp = this.maxHp;

        Sprite[][] sprites = RPGSprite.extractSprites(
                spriteName,
                4,
                1,
                2,
                this,
                16,
                32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT}
        );

        this.animations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);
        this.interactionHandler = new ARPGPlayerHandler();
        this.inventory = new ARPGInventory(30, this);

        this.inventory.addItem(ARPGInventory.ARPGItem.BOMB, 2);
        this.inventory.addItem(ARPGInventory.ARPGItem.BOW, 1);
        this.inventory.addItem(ARPGInventory.ARPGItem.ARROW, 5);
        this.inventory.addItem(ARPGInventory.ARPGItem.CASTLE_KEY, 1);
        this.inventory.addItem(ARPGInventory.ARPGItem.STAFF, 1);
        this.inventory.addItem(ARPGInventory.ARPGItem.SWORD, 1);

        this.inventory.addMoney(19);

        this.GUI = new ARPGPlayerStatusGUI(this.inventory, this.currentItemId, this.hp);
    }

    private Animation getAnimation() {
        return this.animations[this.getOrientation().ordinal()];
    }

    private void switchItem() {
        this.currentItemId = (this.currentItemId + 1) % this.inventory.size();
        this.GUI.setCurrentItem(this.currentItemId);
    }

    private boolean isInteractionKeyPressed() {
        return this.keyboard.get(Keys.INTERACTION_KEY)
                .isPressed();
    }

    private void move(Orientation orientation) {
        if (this.getOrientation().equals(orientation))
            if (keyboard.get(Keys.RUN).isDown())
                this.move(ANIMATION_DURATION / 2, 0);
            else
                this.move(ANIMATION_DURATION, 0);
        else
            this.orientate(orientation);
    }

    void damage(float damageAmount) {
        this.hp -= damageAmount;
        this.GUI.setHealthPoints(this.hp);
    }

    @Override
    public void update(float deltaTime) {
        if (this.keyboard.get(Keys.MOVE_UP).isDown()) this.move(Orientation.UP);
        if (this.keyboard.get(Keys.MOVE_DOWN).isDown()) this.move(Orientation.DOWN);
        if (this.keyboard.get(Keys.MOVE_LEFT).isDown()) this.move(Orientation.LEFT);
        if (this.keyboard.get(Keys.MOVE_RIGHT).isDown()) this.move(Orientation.RIGHT);

        if (this.keyboard.get(Keys.SWITCH_ITEM).isPressed()) this.switchItem();
        if (this.keyboard.get(Keys.SWITCH_COINS_DISPLAY).isPressed()) this.GUI.switchCoinsDisplay();
        if (this.keyboard.get(Keys.CONSUME_ITEM).isPressed()) this.consumeCurrentItem();

        this.getAnimation().update(deltaTime);

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        Animation currentAnimation = this.getAnimation();
        this.GUI.draw(canvas);

        if (this.isDisplacementOccurs()) {
            currentAnimation.draw(canvas);
        } else {
            currentAnimation.reset();
            currentAnimation.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    private void consumeCurrentItem() {
        ARPGInventory.ARPGItem item = this.inventory.getItem(this.currentItemId);
        if (item.getConsumeMethod() == null) return;

        item.getConsumeMethod().accept(this, this.getOwnerArea());
        this.inventory.removeSingleItem(item);

        ARPGInventory.ARPGItem newItem = this.inventory.getItem(this.currentItemId);
        if (newItem == null || !newItem.equals(item)) this.switchItem();
    }

    private class ARPGPlayerHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(ARPGBehavior.ARPGCell cell){
        }

        @Override
        public void interactWith(ARPGPlayer player){
        }

        @Override
        public void interactWith(Grass grass) {
            if (isInteractionKeyPressed())
                grass.cut();
        }

        @Override
        public void interactWith(Coin coin) {
            ARPGPlayer.this.inventory.addMoney(coin.getValue());
            coin.collect();
        }

        @Override
        public void interactWith(Heart heart) {
            if (ARPGPlayer.this.hp <= (ARPGPlayer.this.maxHp - 1))
                ARPGPlayer.this.hp++;
            else
                ARPGPlayer.this.hp = ARPGPlayer.this.maxHp;

            ARPGPlayer.this.GUI.setHealthPoints(ARPGPlayer.this.hp);
            heart.collect();
        }

    }
}
